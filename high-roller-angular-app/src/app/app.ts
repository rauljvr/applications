import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Highroller } from './components/highroller/highroller';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Highroller],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('high-roller-angular-app');
}
