import { Component, inject, OnInit } from '@angular/core';
import { RestClientService } from '../../services/restclient';
import { FormsModule } from '@angular/forms';
import { IPlayerNew } from '../../models/interface/playerNew';
import { IPlayer } from '../../models/interface/player';

@Component({
  selector: 'app-player-new',
  imports: [FormsModule],
  templateUrl: './player-new.html',
  styleUrl: './player-new.css'
})
export class PlayerNew implements OnInit {

  api =  inject(RestClientService);
  playerName: string = "";
  referralPlayer: string = "";
  playerNew: IPlayerNew = { name: '', parentName: "" };
  player!: IPlayer;
  isOkHidden: boolean = true;
  isKoHidden: boolean = true;
  errorMessage: string = "";

  ngOnInit(): void {
    //this.postPlayer();
  }

  postPlayer() {
    this.playerNew.name = this.playerName;
    this.playerNew.parentName = this.referralPlayer;

    /*this.api.postPlayer(this.playerNew).subscribe(data => {
      this.player = data;
    });*/
     this.api.postPlayer(this.playerNew).subscribe({
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
