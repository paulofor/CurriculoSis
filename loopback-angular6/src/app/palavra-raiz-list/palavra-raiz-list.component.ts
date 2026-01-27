import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { PalavraRaizApi } from '../shared/sdk';
import { PalavraRaizListBaseComponent } from './palavra-raiz-list-base.component';

@Component({
	selector: 'app-palavra-raiz-list',
  	templateUrl: './palavra-raiz-list.component.html',
  	styleUrls: ['./palavra-raiz-list.component.css']
})
export class PalavraRaizListComponent extends PalavraRaizListBaseComponent {

	constructor(protected srv: PalavraRaizApi, protected router: ActivatedRoute, protected dialog: MatDialog) { 
		super(srv,router,dialog);
	}

	atualiza() {
		this.srv.AtualizaQuantidadeGeral()
			.subscribe((result) => {
				this.carregaTela();
			})
	}

}
