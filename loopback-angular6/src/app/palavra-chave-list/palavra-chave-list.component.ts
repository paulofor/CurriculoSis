import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { PalavraChaveApi } from '../shared/sdk';
import { PalavraChaveListBaseComponent } from './palavra-chave-list-base.component';

@Component({
	selector: 'app-palavra-chave-list',
  	templateUrl: './palavra-chave-list.component.html',
  	styleUrls: ['./palavra-chave-list.component.css']
})
export class PalavraChaveListComponent extends PalavraChaveListBaseComponent {

	constructor(protected srv: PalavraChaveApi, protected router: ActivatedRoute, protected dialog: MatDialog) { 
		super(srv,router,dialog);
	}

	getFiltro(): {} {
		return {
			'order' : 'palavra'
		}
	}

}
