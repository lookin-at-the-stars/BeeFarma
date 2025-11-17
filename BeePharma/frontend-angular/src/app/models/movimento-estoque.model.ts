export interface MovimentoEstoque {
  id?: string;
  tipo: 'ENTRADA' | 'SAIDA' | 'AJUSTE' | 'TRANSFERENCIA' | 'DEVOLUCAO';
  produtoId: string;
  loteId: string;
  quantidade: number;
  motivo: string;
  observacoes?: string;
  dataHora?: string;
}
