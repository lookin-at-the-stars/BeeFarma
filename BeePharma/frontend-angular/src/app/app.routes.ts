import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { Produtos } from './components/produtos/produtos';
import { Lotes } from './components/lotes/lotes';
import { Estoque } from './components/estoque/estoque';
import { Inventario } from './components/inventario/inventario';
import { Producao } from './components/producao/producao';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: Dashboard },
  { path: 'produtos', component: Produtos },
  { path: 'lotes', component: Lotes },
  { path: 'estoque', component: Estoque },
  { path: 'inventario', component: Inventario },
  { path: 'producao', component: Producao }
];

