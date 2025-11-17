import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MovimentoEstoque } from '../models/movimento-estoque.model';

@Injectable({
  providedIn: 'root',
})
export class MovimentoEstoqueService {
  private http = inject(HttpClient);
  private apiUrl = '/api/movimentos-estoque';

  getAll(): Observable<MovimentoEstoque[]> {
    return this.http.get<MovimentoEstoque[]>(this.apiUrl);
  }

  create(movimento: MovimentoEstoque): Observable<MovimentoEstoque> {
    return this.http.post<MovimentoEstoque>(this.apiUrl, movimento);
  }
}
