import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrdemProducao } from '../models/ordem-producao.model';

@Injectable({
  providedIn: 'root',
})
export class OrdemProducaoService {
  private http = inject(HttpClient);
  private apiUrl = '/api/ordens-producao';

  getAll(): Observable<OrdemProducao[]> {
    return this.http.get<OrdemProducao[]>(this.apiUrl);
  }

  getById(id: string): Observable<OrdemProducao> {
    return this.http.get<OrdemProducao>(`${this.apiUrl}/${id}`);
  }

  create(ordem: OrdemProducao): Observable<OrdemProducao> {
    return this.http.post<OrdemProducao>(this.apiUrl, ordem);
  }

  update(id: string, ordem: OrdemProducao): Observable<OrdemProducao> {
    return this.http.put<OrdemProducao>(`${this.apiUrl}/${id}`, ordem);
  }
}
