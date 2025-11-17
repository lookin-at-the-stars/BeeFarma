import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Lote } from '../models/lote.model';

@Injectable({
  providedIn: 'root',
})
export class LoteService {
  private http = inject(HttpClient);
  private apiUrl = '/api/lotes';

  getAll(): Observable<Lote[]> {
    return this.http.get<Lote[]>(this.apiUrl);
  }

  getById(id: string): Observable<Lote> {
    return this.http.get<Lote>(`${this.apiUrl}/${id}`);
  }

  create(lote: Lote): Observable<Lote> {
    return this.http.post<Lote>(this.apiUrl, lote);
  }

  update(id: string, lote: Lote): Observable<Lote> {
    return this.http.put<Lote>(`${this.apiUrl}/${id}`, lote);
  }

  aprovar(id: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/aprovar`, {});
  }

  reprovar(id: string, motivo?: string): Observable<void> {
    const url = motivo 
      ? `${this.apiUrl}/${id}/reprovar?motivo=${encodeURIComponent(motivo)}`
      : `${this.apiUrl}/${id}/reprovar`;
    return this.http.post<void>(url, {});
  }
}
