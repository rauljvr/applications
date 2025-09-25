import { Component, signal } from '@angular/core';
import { Highroller } from './components/highroller/highroller';

@Component({
  selector: 'app-root',
  imports: [Highroller],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('high-roller-angular-app');
}
