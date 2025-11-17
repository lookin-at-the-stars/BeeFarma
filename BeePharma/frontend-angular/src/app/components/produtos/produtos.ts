import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Produto } from '../../models/produto.model';
import { ProdutoService } from '../../services/produto';

@Component({
  selector: 'app-produtos',
  imports: [CommonModule, FormsModule],
  templateUrl: './produtos.html',
  styleUrl: './produtos.scss'
})
export class Produtos implements OnInit {
  private produtoService = inject(ProdutoService);
  
  protected produtos = signal<Produto[]>([]);
  protected loading = signal(true);
  protected showModal = signal(false);
  protected editMode = signal(false);
  
  protected formData = signal<Produto>({
    nome: '',
    unidade: '',
    principioAtivo: '',
    codigoAnvisa: '',
    classeTerapeutica: '',
    descricao: ''
  });

  ngOnInit(): void {
    this.loadProdutos();
  }

  private loadProdutos(): void {
    this.loading.set(true);
    this.produtoService.getAll().subscribe({
      next: (data) => {
        this.produtos.set(data);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
        this.loading.set(false);
      }
    });
  }

  protected openModal(): void {
    this.editMode.set(false);
    this.resetForm();
    this.showModal.set(true);
  }

  protected closeModal(): void {
    this.showModal.set(false);
    this.resetForm();
  }

  protected resetForm(): void {
    this.formData.set({
      nome: '',
      unidade: '',
      principioAtivo: '',
      codigoAnvisa: '',
      classeTerapeutica: '',
      descricao: ''
    });
  }

  protected onSubmit(): void {
    const produto = this.formData();
    
    if (this.editMode() && produto.id) {
      this.produtoService.update(produto.id, produto).subscribe({
        next: () => {
          alert('Produto atualizado com sucesso!');
          this.closeModal();
          this.loadProdutos();
        },
        error: (error) => {
          console.error('Erro ao atualizar produto:', error);
          alert('Erro ao atualizar produto');
        }
      });
    } else {
      this.produtoService.create(produto).subscribe({
        next: () => {
          alert('Produto cadastrado com sucesso!');
          this.closeModal();
          this.loadProdutos();
        },
        error: (error) => {
          console.error('Erro ao criar produto:', error);
          alert('Erro ao cadastrar produto');
        }
      });
    }
  }

  protected editProduto(produto: Produto): void {
    this.editMode.set(true);
    this.formData.set({ ...produto });
    this.showModal.set(true);
  }

  protected deleteProduto(id: string): void {
    if (!confirm('Tem certeza que deseja excluir este produto?')) return;
    
    this.produtoService.delete(id).subscribe({
      next: () => {
        alert('Produto excluído com sucesso!');
        this.loadProdutos();
      },
      error: (error) => {
        console.error('Erro ao excluir produto:', error);
        alert('Erro ao excluir produto');
      }
    });
  }

  protected formatDate(dateString?: string): string {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  }
}
