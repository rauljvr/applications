import { Component, inject, OnInit } from '@angular/core';
import { RestClientService } from '../../services/restclient';
import { FormsModule } from '@angular/forms';
import { IPlayer } from '../../models/interface/player';
import { IPlayerTransfer } from '../../models/interface/playerTrasnfer';

@Component({
  selector: 'app-player-transfer',
  imports: [FormsModule],
  templateUrl: './player-transfer.html',
  styleUrl: './player-transfer.css'
})
export class PlayerTransfer implements OnInit {

  api =  inject(RestClientService);
  playerName: string = "";
  referralPlayer: string = "";
  playerTransfer: IPlayerTransfer = { name: '' };
  player!: IPlayer;
  isOkHidden: boolean = true;
  isKoHidden: boolean = true;
  errorMessage: string = "";

  ngOnInit(): void {
    //this.getPlayerById();
  }

  putPlayerTransfer() {
    this.playerTransfer.name = this.referralPlayer;

    this.api.putPlayerTransfer(this.playerName, this.playerTransfer).subscribe({
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
