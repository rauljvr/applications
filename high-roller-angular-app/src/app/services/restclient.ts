import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IPlayerNew } from '../models/interface/playerNew';
import { IPlayerTransfer } from '../models/interface/playerTrasnfer';

@Injectable({
  providedIn: 'root'
})
export class RestClientService {

  apiURL: string = 'http://localhost:8080/highrollernetwork/player';
  //apiURL: string = 'http://localhost:8081/highrollernetworkreactive/player';
  
  http = inject(HttpClient);

  //getPlayerById(id: string): Observable<Player> {
    //return this.http.get<IPlayer>(`${this.apiUrl}/${id}`);
    //return this.http.get<IPlayer[]>(this.apiUrl);
  //}

  getPlayerById(id: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.get(`${this.apiURL}/${id}`, { headers });
  }

  getPlayerByName(playerName: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    const params = new HttpParams().set('name', playerName);

    return this.http.get(this.apiURL, { headers, params });
  }

  getPlayerReferral(playerName: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.get(`${this.apiURL}/${playerName}/referrer`, { headers });
  }

  getPlayerDownline(playerName: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.get(`${this.apiURL}/${playerName}/downline`, { headers });
  }

  postPlayer(player: IPlayerNew): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.post(this.apiURL, player, { headers });
  }

  putPlayerTransfer(playerName: string, player: IPlayerTransfer): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.put(`${this.apiURL}/${playerName}/transfer`, player, { headers });
  }

  putPlayerExit(playerName: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.put(`${this.apiURL}/${playerName}/exit`, { headers });
  }
  
}
