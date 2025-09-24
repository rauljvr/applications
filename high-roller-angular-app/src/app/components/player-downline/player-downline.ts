import { Component, inject, OnInit } from '@angular/core';
import { RestClientService } from '../../services/restclient';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-player-downline',
  imports: [FormsModule],
  templateUrl: './player-downline.html',
  styleUrl: './player-downline.css'
})
export class PlayerDownline implements OnInit {

  api =  inject(RestClientService);
  playerName: string = "";
  players: string[] = [];
  isOkHidden: boolean = true;
  isKoHidden: boolean = true;
  errorMessage: string = "";

  ngOnInit(): void {
    //this.getPlayerDownline();
  }

  getPlayerDownline() {
    this.api.getPlayerDownline(this.playerName).subscribe({
      next: (data) => {
        this.players = data;
        this.isOkHidden = false;
        this.isKoHidden = true;
      },
      error: (error) => {
        this.errorMessage = error.error.message == undefined ? "Error ocurred, try again." : error.error.message;
        this.isOkHidden = true;
        this.isKoHidden = false;
      },
    });
  }

}
