export interface Lote {
  id?: string;
  numeroLote: string;
  produtoId: string;
  quantidade: number;
  dataFabricacao: string;
  dataValidade: string;
  fornecedor?: string;
  numeroNotaFiscal?: string;
  observacoes?: string;
  status?: 'QUARENTENA' | 'APROVADO' | 'REPROVADO' | 'VENCIDO' | 'EM_USO' | 'ESGOTADO';
  criadoEm?: string;
}
