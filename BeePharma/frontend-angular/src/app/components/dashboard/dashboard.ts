import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProdutoService } from '../../services/produto';
import { LoteService } from '../../services/lote';
import { MovimentoEstoqueService } from '../../services/movimento-estoque';
import { OrdemProducaoService } from '../../services/ordem-producao';

interface DashboardStats {
  totalProdutos: number;
  totalLotes: number;
  totalMovimentos: number;
  totalOrdens: number;
}

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  private produtoService = inject(ProdutoService);
  private loteService = inject(LoteService);
  private movimentoService = inject(MovimentoEstoqueService);
  private ordemService = inject(OrdemProducaoService);
  
  protected stats = signal<DashboardStats>({
    totalProdutos: 0,
    totalLotes: 0,
    totalMovimentos: 0,
    totalOrdens: 0
  });
  
  protected loading = signal(true);

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    this.loading.set(true);
    
    Promise.all([
      this.produtoService.getAll().toPromise(),
      this.loteService.getAll().toPromise(),
      this.movimentoService.getAll().toPromise(),
      this.ordemService.getAll().toPromise()
    ]).then(([produtos, lotes, movimentos, ordens]) => {
      this.stats.set({
        totalProdutos: produtos?.length || 0,
        totalLotes: lotes?.length || 0,
        totalMovimentos: movimentos?.length || 0,
        totalOrdens: ordens?.length || 0
      });
      this.loading.set(false);
    }).catch(error => {
      console.error('Erro ao carregar dashboard:', error);
      this.loading.set(false);
    });
  }
}
