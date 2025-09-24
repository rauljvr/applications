import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PlayerId } from "../player-id/player-id";
import { PlayerName } from "../player-name/player-name";
import { Referral } from '../player-referral/player-referral';
import { PlayerDownline } from "../player-downline/player-downline";
import { PlayerNew } from "../player-new/player-new";
import { PlayerTransfer } from "../player-transfer/player-transfer";
import { PlayerExit } from "../player-exit/player-exit";

@Component({
  selector: 'app-highroller',
  imports: [FormsModule, CommonModule, PlayerId, PlayerName, Referral, PlayerDownline, PlayerNew, PlayerTransfer, PlayerExit],
  templateUrl: './highroller.html',
  styleUrl: './highroller.css'
})
export class Highroller {

  currentComponent: string = "playerId";

  swapTab(tabName: string) {
    this.currentComponent = tabName;
  }

}
