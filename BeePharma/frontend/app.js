// Configuração da API
// Usar URL relativa - o nginx fará o proxy
const API_BASE_URL = '/api';

// Estado global
let produtos = [];
let lotes = [];
let movimentos = [];
let inventario = [];
let ordens = [];

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    loadDashboardData();
    loadProdutos();
});

// Navegação entre seções
function initNavigation() {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const section = link.dataset.section;
            
            // Atualizar links ativos
            navLinks.forEach(l => l.classList.remove('active'));
            link.classList.add('active');
            
            // Mostrar seção
            document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
            document.getElementById(section).classList.add('active');
            
            // Carregar dados da seção
            loadSectionData(section);
        });
    });
}

// Carregar dados do dashboard
async function loadDashboardData() {
    try {
        const [produtosRes, lotesRes, movimentosRes, ordensRes] = await Promise.all([
            fetch(`${API_BASE_URL}/produtos`),
            fetch(`${API_BASE_URL}/lotes`),
            fetch(`${API_BASE_URL}/movimentos-estoque`),
            fetch(`${API_BASE_URL}/ordens-producao`)
        ]);
        
        const produtosData = await produtosRes.json();
        const lotesData = await lotesRes.json();
        const movimentosData = await movimentosRes.json();
        const ordensData = await ordensRes.json();
        
        document.getElementById('total-produtos').textContent = produtosData.length;
        document.getElementById('total-lotes').textContent = lotesData.length;
        document.getElementById('total-movimentos').textContent = movimentosData.length;
        document.getElementById('total-ordens').textContent = ordensData.length;
    } catch (error) {
        console.error('Erro ao carregar dashboard:', error);
    }
}

// Carregar dados por seção
function loadSectionData(section) {
    switch(section) {
        case 'produtos':
            loadProdutos();
            break;
        case 'lotes':
            loadLotes();
            break;
        case 'estoque':
            loadMovimentos();
            break;
        case 'inventario':
            loadInventario();
            break;
        case 'producao':
            loadOrdens();
            break;
    }
}

// PRODUTOS
async function loadProdutos() {
    const tbody = document.getElementById('produtos-table-body');
    tbody.innerHTML = '<tr><td colspan="7" class="loading"><div class="spinner"></div>Carregando produtos...</td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/produtos`);
        produtos = await response.json();
        
        if (produtos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 2rem;">Nenhum produto cadastrado</td></tr>';
            return;
        }
        
        tbody.innerHTML = produtos.map(p => `
            <tr>
                <td><strong>${p.nome}</strong></td>
                <td>${p.codigoAnvisa || '-'}</td>
                <td>${p.classeTerapeutica || '-'}</td>
                <td>${p.unidade}</td>
                <td>${p.descricao ? (p.descricao.substring(0, 50) + (p.descricao.length > 50 ? '...' : '')) : '-'}</td>
                <td>${formatDate(p.criadoEm)}</td>
                <td class="action-buttons">
                    <button class="btn btn-sm btn-primary" onclick="editarProduto('${p.id}')">✏️</button>
                    <button class="btn btn-sm btn-danger" onclick="excluirProduto('${p.id}')">🗑️</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
        showToast('Erro ao carregar produtos', 'error');
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 2rem; color: red;">Erro ao carregar produtos</td></tr>';
    }
}

async function salvarProduto(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const produto = {
        nome: formData.get('nome'),
        descricao: formData.get('descricao') || null,
        codigoAnvisa: formData.get('codigoAnvisa') || null,
        unidade: formData.get('unidade'),
        principioAtivo: formData.get('principioAtivo'),
        classeTerapeutica: formData.get('classeTerapeutica') || null
    };
    
    console.log('=== DEBUG FORMULÁRIO ===');
    console.log('FormData entries:');
    for (let [key, value] of formData.entries()) {
        console.log(`  ${key}: "${value}"`);
    }
    console.log('Objeto produto:', produto);
    console.log('JSON a enviar:', JSON.stringify(produto, null, 2));
    console.log('========================');
    
    try {
        const isEdit = editandoProduto !== null;
        const url = isEdit ? `${API_BASE_URL}/produtos/${editandoProduto.id}` : `${API_BASE_URL}/produtos`;
        const method = isEdit ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(produto)
        });
        
        if (response.ok) {
            showToast(`Produto ${isEdit ? 'atualizado' : 'cadastrado'} com sucesso!`, 'success');
            closeModal('modalProduto');
            form.reset();
            editandoProduto = null;
            // Resetar título do modal
            document.querySelector('#modalProduto .modal-header h3').textContent = 'Novo Produto';
            document.querySelector('#formProduto button[type="submit"]').textContent = 'Salvar';
            loadProdutos();
            loadDashboardData();
        } else {
            const errorData = await response.json();
            console.error('Erro do servidor:', errorData);
            showToast(`Erro: ${errorData.message || 'Erro ao salvar produto'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao salvar produto', 'error');
    }
}

async function excluirProduto(id) {
    if (!confirm('Tem certeza que deseja excluir este produto?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/produtos/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('Produto excluído com sucesso!', 'success');
            loadProdutos();
            loadDashboardData();
        } else {
            throw new Error('Erro ao excluir produto');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao excluir produto', 'error');
    }
}

// LOTES
async function loadLotes() {
    const tbody = document.getElementById('lotes-table-body');
    tbody.innerHTML = '<tr><td colspan="6" class="loading"><div class="spinner"></div>Carregando lotes...</td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/lotes`);
        lotes = await response.json();
        
        if (lotes.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 2rem;">Nenhum lote cadastrado</td></tr>';
            return;
        }
        
        tbody.innerHTML = lotes.map(l => {
            const produto = produtos.find(p => p.id === l.produtoId);
            const statusClass = {
                'QUARENTENA': 'badge-warning',
                'APROVADO': 'badge-success',
                'REPROVADO': 'badge-danger',
                'VENCIDO': 'badge-secondary',
                'EM_USO': 'badge-info',
                'ESGOTADO': 'badge-secondary'
            }[l.status] || 'badge-secondary';
            
            // Botões de ação baseados no status
            let actionButtons = '';
            if (l.status === 'QUARENTENA') {
                actionButtons = `
                    <button class="btn btn-sm btn-primary" onclick="editarLote('${l.id}')" title="Editar">✏️</button>
                    <button class="btn btn-sm btn-success" onclick="aprovarLote('${l.id}')" title="Aprovar">✔️</button>
                    <button class="btn btn-sm btn-danger" onclick="reprovarLote('${l.id}')" title="Reprovar">✖️</button>
                `;
            } else {
                actionButtons = `<span class="badge ${statusClass}">Não editável</span>`;
            }
            
            return `
                <tr>
                    <td><strong>${l.numeroLote}</strong></td>
                    <td>${produto ? produto.nome : 'N/A'}</td>
                    <td>${l.quantidade || 0}</td>
                    <td>${formatDate(l.dataValidade)}</td>
                    <td><span class="badge ${statusClass}">${l.status}</span></td>
                    <td class="action-buttons">
                        ${actionButtons}
                    </td>
                </tr>
            `;
        }).join('');
    } catch (error) {
        console.error('Erro ao carregar lotes:', error);
        showToast('Erro ao carregar lotes', 'error');
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 2rem; color: red;">Erro ao carregar lotes</td></tr>';
    }
}

// MOVIMENTOS
async function loadMovimentos() {
    const tbody = document.getElementById('estoque-table-body');
    tbody.innerHTML = '<tr><td colspan="5" class="loading"><div class="spinner"></div>Carregando movimentos...</td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/movimentos-estoque`);
        movimentos = await response.json();
        
        if (movimentos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 2rem;">Nenhum movimento registrado</td></tr>';
            return;
        }
        
        tbody.innerHTML = movimentos.slice(0, 50).map(m => {
            const produto = produtos.find(p => p.id === m.produtoId);
            const tipoClass = {
                'ENTRADA': 'badge-success',
                'SAIDA': 'badge-danger',
                'AJUSTE': 'badge-warning',
                'TRANSFERENCIA': 'badge-info',
                'DEVOLUCAO': 'badge-secondary'
            }[m.tipo] || 'badge-secondary';
            
            return `
                <tr>
                    <td>${formatDateTime(m.dataHora)}</td>
                    <td><span class="badge ${tipoClass}">${m.tipo}</span></td>
                    <td>${produto ? produto.nome : 'N/A'}</td>
                    <td>${m.quantidade}</td>
                    <td>${m.motivo || '-'}</td>
                </tr>
            `;
        }).join('');
    } catch (error) {
        console.error('Erro ao carregar movimentos:', error);
        showToast('Erro ao carregar movimentos', 'error');
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 2rem; color: red;">Erro ao carregar movimentos</td></tr>';
    }
}

// INVENTÁRIO
async function loadInventario() {
    const tbody = document.getElementById('inventario-table-body');
    tbody.innerHTML = '<tr><td colspan="6" class="loading"><div class="spinner"></div>Carregando inventário...</td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/inventario`);
        inventario = await response.json();
        
        if (inventario.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 2rem;">Nenhum item no inventário</td></tr>';
            return;
        }
        
        tbody.innerHTML = inventario.map(i => {
            const produto = produtos.find(p => p.id === i.produtoId);
            const diferenca = i.quantidadeFisica - i.quantidadeSistema;
            const diferencaClass = diferenca === 0 ? 'badge-success' : (diferenca > 0 ? 'badge-info' : 'badge-warning');
            
            return `
                <tr>
                    <td>${produto ? produto.nome : 'N/A'}</td>
                    <td>${i.localizacao}</td>
                    <td>${i.quantidadeFisica}</td>
                    <td>${i.quantidadeSistema}</td>
                    <td><span class="badge ${diferencaClass}">${diferenca > 0 ? '+' : ''}${diferenca}</span></td>
                    <td class="action-buttons">
                        <button class="btn btn-sm btn-danger" onclick="excluirInventario('${i.id}')">🗑️</button>
                    </td>
                </tr>
            `;
        }).join('');
    } catch (error) {
        console.error('Erro ao carregar inventário:', error);
        showToast('Erro ao carregar inventário', 'error');
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 2rem; color: red;">Erro ao carregar inventário</td></tr>';
    }
}

// ORDENS DE PRODUÇÃO
async function loadOrdens() {
    const tbody = document.getElementById('producao-table-body');
    tbody.innerHTML = '<tr><td colspan="6" class="loading"><div class="spinner"></div>Carregando ordens...</td></tr>';
    
    try {
        const response = await fetch(`${API_BASE_URL}/ordens-producao`);
        ordens = await response.json();
        
        if (ordens.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 2rem;">Nenhuma ordem cadastrada</td></tr>';
            return;
        }
        
        tbody.innerHTML = ordens.map(o => {
            const produto = produtos.find(p => p.id === o.produtoId);
            const statusClass = {
                'PLANEJADA': 'badge-secondary',
                'EM_ANDAMENTO': 'badge-info',
                'PAUSADA': 'badge-warning',
                'CONCLUIDA': 'badge-success',
                'CANCELADA': 'badge-danger'
            }[o.status] || 'badge-secondary';
            
            const prioridadeClass = {
                'BAIXA': 'badge-secondary',
                'MEDIA': 'badge-info',
                'ALTA': 'badge-warning',
                'URGENTE': 'badge-danger'
            }[o.prioridade] || 'badge-secondary';
            
            return `
                <tr>
                    <td><strong>${o.numeroOrdem}</strong></td>
                    <td>${produto ? produto.nome : 'N/A'}</td>
                    <td>${o.quantidadePlanejada || 0}</td>
                    <td><span class="badge ${prioridadeClass}">${o.prioridade}</span></td>
                    <td><span class="badge ${statusClass}">${o.status}</span></td>
                    <td class="action-buttons">
                        <button class="btn btn-sm btn-primary" onclick="editarOrdem('${o.id}')">✏️</button>
                    </td>
                </tr>
            `;
        }).join('');
    } catch (error) {
        console.error('Erro ao carregar ordens:', error);
        showToast('Erro ao carregar ordens', 'error');
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 2rem; color: red;">Erro ao carregar ordens</td></tr>';
    }
}

// Funções auxiliares
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '-';
    const date = new Date(dateTimeString);
    return date.toLocaleString('pt-BR');
}

// Modal
function showModal(modalId) {
    document.getElementById(modalId).classList.add('active');
    // Carregar produtos nos selects quando abrir modais
    if (modalId === 'modalLote' || modalId === 'modalMovimento' || modalId === 'modalInventario' || modalId === 'modalOrdemProducao') {
        popularSelectProdutos(modalId);
    }
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
    
    // Resetar estados de edição
    if (modalId === 'modalProduto') {
        editandoProduto = null;
        document.querySelector('#modalProduto .modal-header h3').textContent = 'Novo Produto';
        document.querySelector('#formProduto button[type="submit"]').textContent = 'Salvar';
        document.getElementById('formProduto').reset();
    } else if (modalId === 'modalLote') {
        editandoLote = null;
        document.querySelector('#modalLote .modal-header h3').textContent = 'Novo Lote';
        document.querySelector('#formLote button[type="submit"]').textContent = 'Salvar';
        document.getElementById('formLote').reset();
    } else if (modalId === 'modalOrdemProducao') {
        editandoOrdem = null;
        document.querySelector('#modalOrdemProducao .modal-header h3').textContent = 'Nova Ordem de Produção';
        document.querySelector('#formOrdemProducao button[type="submit"]').textContent = 'Salvar';
        document.getElementById('formOrdemProducao').reset();
    }
}

// Popular select de produtos
async function popularSelectProdutos(modalId) {
    if (produtos.length === 0) {
        await loadProdutos();
    }
    
    let selectId;
    switch(modalId) {
        case 'modalLote':
            selectId = 'loteProduto';
            break;
        case 'modalMovimento':
            selectId = 'movProduto';
            break;
        case 'modalInventario':
            selectId = 'invProduto';
            break;
        case 'modalOrdemProducao':
            selectId = 'opProduto';
            break;
    }
    
    const select = document.getElementById(selectId);
    select.innerHTML = '<option value="">Selecione...</option>' + 
        produtos.map(p => `<option value="${p.id}">${p.nome}</option>`).join('');
}

// Carregar lotes por produto
async function carregarLotesPorProduto(produtoId, selectId = 'movLote') {
    if (!produtoId) {
        document.getElementById(selectId).innerHTML = '<option value="">Selecione o produto primeiro...</option>';
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/lotes`);
        const todosLotes = await response.json();
        const lotesFiltered = todosLotes.filter(l => l.produtoId === produtoId && l.status === 'APROVADO');
        
        const select = document.getElementById(selectId);
        if (lotesFiltered.length === 0) {
            select.innerHTML = '<option value="">Nenhum lote aprovado disponível</option>';
        } else {
            select.innerHTML = '<option value="">Selecione...</option>' +
                lotesFiltered.map(l => `<option value="${l.id}">${l.numeroLote} - Qtd: ${l.quantidade}</option>`).join('');
        }
    } catch (error) {
        console.error('Erro ao carregar lotes:', error);
    }
}

// SALVAR LOTE
async function salvarLote(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const lote = {
        numeroLote: formData.get('numeroLote'),
        produtoId: formData.get('produtoId'),
        quantidade: parseFloat(formData.get('quantidade')),
        dataFabricacao: formData.get('dataFabricacao'),
        dataValidade: formData.get('dataValidade'),
        fornecedor: formData.get('fornecedor') || null,
        numeroNotaFiscal: formData.get('numeroNotaFiscal') || null,
        observacoes: formData.get('observacoes') || null
    };
    
    console.log('Enviando lote:', lote);
    
    try {
        const isEdit = editandoLote !== null;
        const url = isEdit ? `${API_BASE_URL}/lotes/${editandoLote.id}` : `${API_BASE_URL}/lotes`;
        const method = isEdit ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(lote)
        });
        
        if (response.ok) {
            showToast(`Lote ${isEdit ? 'atualizado' : 'cadastrado'} com sucesso!`, 'success');
            closeModal('modalLote');
            form.reset();
            editandoLote = null;
            // Resetar título do modal
            document.querySelector('#modalLote .modal-header h3').textContent = 'Novo Lote';
            document.querySelector('#formLote button[type="submit"]').textContent = 'Salvar';
            loadLotes();
            loadDashboardData();
        } else {
            const errorData = await response.json();
            console.error('Erro do servidor:', errorData);
            showToast(`Erro: ${errorData.message || 'Erro ao salvar lote'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao salvar lote', 'error');
    }
}

// SALVAR MOVIMENTO
async function salvarMovimento(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const movimento = {
        tipo: formData.get('tipo'),
        produtoId: formData.get('produtoId'),
        loteId: formData.get('loteId'),
        quantidade: parseInt(formData.get('quantidade')),
        motivo: formData.get('motivo'),
        observacoes: formData.get('observacoes') || null
    };
    
    console.log('Enviando movimento:', movimento);
    
    try {
        const response = await fetch(`${API_BASE_URL}/movimentos-estoque`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(movimento)
        });
        
        if (response.ok) {
            showToast('Movimento registrado com sucesso!', 'success');
            closeModal('modalMovimento');
            form.reset();
            loadMovimentos();
            loadDashboardData();
        } else {
            const errorData = await response.json();
            console.error('Erro do servidor:', errorData);
            showToast(`Erro: ${errorData.message || 'Erro ao registrar movimento'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao registrar movimento', 'error');
    }
}

// SALVAR INVENTÁRIO
async function salvarInventario(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const inventario = {
        produtoId: formData.get('produtoId'),
        loteId: formData.get('loteId'),
        localizacao: formData.get('localizacao'),
        quantidadeFisica: parseInt(formData.get('quantidadeFisica')),
        quantidadeSistema: parseInt(formData.get('quantidadeSistema')),
        dataContagem: formData.get('dataContagem'),
        responsavel: formData.get('responsavel') || null,
        observacoes: formData.get('observacoes') || null
    };
    
    console.log('Enviando inventário:', inventario);
    
    try {
        const response = await fetch(`${API_BASE_URL}/inventario`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(inventario)
        });
        
        if (response.ok) {
            showToast('Item de inventário cadastrado com sucesso!', 'success');
            closeModal('modalInventario');
            form.reset();
            loadInventario();
            loadDashboardData();
        } else {
            const errorData = await response.json();
            console.error('Erro do servidor:', errorData);
            showToast(`Erro: ${errorData.message || 'Erro ao cadastrar item'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao cadastrar item de inventário', 'error');
    }
}

// SALVAR ORDEM DE PRODUÇÃO
async function salvarOrdemProducao(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const ordem = {
        numeroOP: formData.get('numeroOrdem'),
        produtoId: formData.get('produtoId'),
        quantidadePlanejada: parseFloat(formData.get('quantidadePlanejada')),
        dataInicio: formData.get('dataPrevisaoInicio') || null,
        dataFimPrevista: formData.get('dataPrevisaoFim') || null
    };
    
    console.log('Enviando ordem:', ordem);
    
    try {
        const isEdit = editandoOrdem !== null;
        const url = isEdit ? `${API_BASE_URL}/ordens-producao/${editandoOrdem.id}` : `${API_BASE_URL}/ordens-producao`;
        const method = isEdit ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ordem)
        });
        
        if (response.ok) {
            showToast(`Ordem de produção ${isEdit ? 'atualizada' : 'cadastrada'} com sucesso!`, 'success');
            closeModal('modalOrdemProducao');
            form.reset();
            editandoOrdem = null;
            // Resetar título do modal
            document.querySelector('#modalOrdemProducao .modal-header h3').textContent = 'Nova Ordem de Produção';
            document.querySelector('#formOrdemProducao button[type="submit"]').textContent = 'Salvar';
            loadOrdens();
            loadDashboardData();
        } else {
            const errorData = await response.json();
            console.error('Erro do servidor:', errorData);
            showToast(`Erro: ${errorData.message || 'Erro ao salvar ordem'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao salvar ordem de produção', 'error');
    }
}

// Toast notifications
function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    const title = {
        success: '✅ Sucesso!',
        error: '❌ Erro!',
        warning: '⚠️ Atenção!',
        info: 'ℹ️ Informação'
    }[type];
    
    toast.innerHTML = `
        <div class="toast-title">${title}</div>
        <div class="toast-message">${message}</div>
    `;
    
    container.appendChild(toast);
    
    setTimeout(() => {
        toast.style.animation = 'slideInRight 0.3s reverse';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Placeholders para outras funções
let editandoProduto = null;
let editandoLote = null;
let editandoOrdem = null;

async function editarProduto(id) {
    editandoProduto = produtos.find(p => p.id === id);
    if (!editandoProduto) {
        showToast('Produto não encontrado', 'error');
        return;
    }
    
    // Preencher o formulário
    document.getElementById('nome').value = editandoProduto.nome;
    document.getElementById('codigoAnvisa').value = editandoProduto.codigoAnvisa || '';
    document.getElementById('unidade').value = editandoProduto.unidade;
    document.getElementById('principioAtivo').value = editandoProduto.principioAtivo || '';
    document.getElementById('classeTerapeutica').value = editandoProduto.classeTerapeutica || '';
    document.getElementById('descricao').value = editandoProduto.descricao || '';
    
    // Mudar título do modal
    document.querySelector('#modalProduto .modal-header h3').textContent = 'Editar Produto';
    document.querySelector('#formProduto button[type="submit"]').textContent = 'Atualizar';
    
    showModal('modalProduto');
}

async function editarLote(id) {
    editandoLote = lotes.find(l => l.id === id);
    if (!editandoLote) {
        showToast('Lote não encontrado', 'error');
        return;
    }
    
    // Verificar se o lote pode ser editado
    if (editandoLote.status !== 'QUARENTENA') {
        showToast(`Não é possível editar um lote com status ${editandoLote.status}. Apenas lotes em QUARENTENA podem ser editados.`, 'warning');
        return;
    }
    
    // Popular select de produtos se necessário
    await popularSelectProdutos('modalLote');
    
    // Preencher o formulário
    document.getElementById('loteNumero').value = editandoLote.numeroLote;
    document.getElementById('loteProduto').value = editandoLote.produtoId;
    document.getElementById('loteQuantidade').value = editandoLote.quantidade;
    document.getElementById('loteFornecedor').value = editandoLote.fornecedor || '';
    document.getElementById('loteDataFabricacao').value = editandoLote.dataFabricacao;
    document.getElementById('loteDataValidade').value = editandoLote.dataValidade;
    document.getElementById('loteNotaFiscal').value = editandoLote.numeroNotaFiscal || '';
    document.getElementById('loteObservacoes').value = editandoLote.observacoes || '';
    
    // Mudar título do modal
    document.querySelector('#modalLote .modal-header h3').textContent = 'Editar Lote';
    document.querySelector('#formLote button[type="submit"]').textContent = 'Atualizar';
    
    showModal('modalLote');
}

async function aprovarLote(id) {
    if (!confirm('Tem certeza que deseja aprovar este lote?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/lotes/${id}/aprovar`, {
            method: 'POST'
        });
        
        if (response.ok) {
            showToast('Lote aprovado com sucesso!', 'success');
            loadLotes();
            loadDashboardData();
        } else {
            const errorData = await response.json();
            showToast(`Erro: ${errorData.message || 'Erro ao aprovar lote'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao aprovar lote', 'error');
    }
}

async function reprovarLote(id) {
    const motivo = prompt('Informe o motivo da reprovação (opcional):');
    if (motivo === null) return; // Usuário cancelou
    
    try {
        const url = motivo ? `${API_BASE_URL}/lotes/${id}/reprovar?motivo=${encodeURIComponent(motivo)}` : `${API_BASE_URL}/lotes/${id}/reprovar`;
        const response = await fetch(url, {
            method: 'POST'
        });
        
        if (response.ok) {
            showToast('Lote reprovado com sucesso!', 'success');
            loadLotes();
            loadDashboardData();
        } else {
            const errorData = await response.json();
            showToast(`Erro: ${errorData.message || 'Erro ao reprovar lote'}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao reprovar lote', 'error');
    }
}

async function excluirInventario(id) {
    if (!confirm('Tem certeza que deseja excluir este item do inventário?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/inventario/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('Item excluído com sucesso!', 'success');
            loadInventario();
        } else {
            throw new Error('Erro ao excluir item');
        }
    } catch (error) {
        console.error('Erro:', error);
        showToast('Erro ao excluir item', 'error');
    }
}

function editarOrdem(id) {
    editandoOrdem = ordens.find(o => o.id === id);
    if (!editandoOrdem) {
        showToast('Ordem não encontrada', 'error');
        return;
    }
    
    // Verificar se a ordem pode ser editada
    if (editandoOrdem.status === 'CONCLUIDA' || editandoOrdem.status === 'CANCELADA') {
        showToast(`Não é possível editar uma ordem de produção com status ${editandoOrdem.status}.`, 'warning');
        return;
    }
    
    // Popular select de produtos se necessário
    popularSelectProdutos('modalOrdemProducao');
    
    // Preencher o formulário
    document.getElementById('opNumero').value = editandoOrdem.numeroOP;
    document.getElementById('opProduto').value = editandoOrdem.produtoId;
    document.getElementById('opQuantidade').value = editandoOrdem.quantidadePlanejada;
    document.getElementById('opDataInicio').value = editandoOrdem.dataInicio || '';
    document.getElementById('opDataFimPrevista').value = editandoOrdem.dataFimPrevista || '';
    
    // Mudar título do modal
    document.querySelector('#modalOrdemProducao .modal-header h3').textContent = 'Editar Ordem de Produção';
    document.querySelector('#formOrdemProducao button[type="submit"]').textContent = 'Atualizar';
    
    showModal('modalOrdemProducao');
}
