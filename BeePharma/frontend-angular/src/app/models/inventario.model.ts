export interface InventarioItem {
  id?: string;
  produtoId: string;
  loteId: string;
  localizacao: string;
  quantidadeFisica: number;
  quantidadeSistema: number;
  dataContagem: string;
  responsavel?: string;
  observacoes?: string;
}
