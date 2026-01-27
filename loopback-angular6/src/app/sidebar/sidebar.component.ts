import { Component, OnInit } from '@angular/core';
import { BASE_URL } from '../constantes/base.url';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  ambiente: string = '';

  constructor() { }

  ngOnInit() {
    if (BASE_URL.indexOf("21101")==-1) this.ambiente = ' Desen'; 
  }

}
