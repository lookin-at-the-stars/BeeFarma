import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InventarioItem } from '../models/inventario.model';

@Injectable({
  providedIn: 'root',
})
export class InventarioService {
  private http = inject(HttpClient);
  private apiUrl = '/api/inventario';

  getAll(): Observable<InventarioItem[]> {
    return this.http.get<InventarioItem[]>(this.apiUrl);
  }

  create(item: InventarioItem): Observable<InventarioItem> {
    return this.http.post<InventarioItem>(this.apiUrl, item);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
