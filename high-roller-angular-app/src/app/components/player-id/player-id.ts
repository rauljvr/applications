import { Component, inject, OnInit} from '@angular/core';
import { RestClientService } from '../../services/restclient';
import { FormsModule } from '@angular/forms';
import { IPlayer } from '../../models/interface/player'

@Component({
  selector: 'app-player-id',
  imports: [FormsModule],
  templateUrl: './player-id.html',
  styleUrl: './player-id.css'
})
export class PlayerId implements OnInit {

  api = inject(RestClientService);
  playerId: string = "";
  player!: IPlayer;
  isOkHidden: boolean = true;
  isKoHidden: boolean = true;
  errorMessage: string = "";

  ngOnInit(): void {
    //this.getPlayerById();
  }

  getPlayerById() {
    this.api.getPlayerById(this.playerId).subscribe({
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
