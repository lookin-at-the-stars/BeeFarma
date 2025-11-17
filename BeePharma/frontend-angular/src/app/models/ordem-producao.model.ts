export interface OrdemProducao {
  id?: string;
  numeroOP: string;
  produtoId: string;
  quantidadePlanejada: number;
  quantidadeProduzida?: number;
  dataInicio?: string;
  dataFimPrevista?: string;
  dataFimReal?: string;
  status?: 'PLANEJADA' | 'EM_ANDAMENTO' | 'PAUSADA' | 'CONCLUIDA' | 'CANCELADA';
  prioridade?: 'BAIXA' | 'MEDIA' | 'ALTA' | 'URGENTE';
  observacoes?: string;
}
