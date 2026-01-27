import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { OportunidadeLinkedinApi } from '../shared/sdk';
import { OportunidadeLinkedinListSimplesBaseComponent } from './oportunidade-linkedin-list-simples-base.component';

@Component({
	selector: 'app-oportunidade-linkedin-list-simples',
  	templateUrl: './oportunidade-linkedin-list-simples.component.html',
  	styleUrls: ['./oportunidade-linkedin-list-simples.component.css']
})
export class OportunidadeLinkedinListSimplesComponent extends OportunidadeLinkedinListSimplesBaseComponent {

	constructor(protected srv: OportunidadeLinkedinApi, protected router: ActivatedRoute, protected dialog: MatDialog) { 
		super(srv,router,dialog);
	}

}
