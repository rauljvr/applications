import { Component, inject, OnInit } from '@angular/core';
import { RestClientService } from '../../services/restclient';
import { FormsModule } from '@angular/forms';
import { IPlayer } from '../../models/interface/player'

@Component({
  selector: 'app-player-name',
  imports: [FormsModule],
  templateUrl: './player-name.html',
  styleUrl: './player-name.css'
})
export class PlayerName implements OnInit {

  api =  inject(RestClientService);
  playerName: string = "";
  player!: IPlayer;
  isOkHidden: boolean = true;
  isKoHidden: boolean = true;
  errorMessage: string = "";

  ngOnInit(): void {
    //this.getPlayerByName();
  }

  getPlayerByName() {
    this.api.getPlayerByName(this.playerName).subscribe({
      next: (data) => {
        this.player = data;
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
