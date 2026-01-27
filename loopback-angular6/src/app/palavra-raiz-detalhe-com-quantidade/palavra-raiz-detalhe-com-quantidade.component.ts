import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { PalavraRaizApi } from '../shared/sdk';
import { PalavraRaizDetalheComQuantidadeBaseComponent } from './palavra-raiz-detalhe-com-quantidade-base.component';

@Component({
	selector: 'app-palavra-raiz-detalhe-com-quantidade',
  	templateUrl: './palavra-raiz-detalhe-com-quantidade.component.html',
  	styleUrls: ['./palavra-raiz-detalhe-com-quantidade.component.css']
})
export class PalavraRaizDetalheComQuantidadeComponent extends PalavraRaizDetalheComQuantidadeBaseComponent {

	constructor(protected srv: PalavraRaizApi, protected router: ActivatedRoute, protected dialog: MatDialog) { 
		super(srv,router,dialog);
	}

	getFiltro() {
		return {
			'include' : {'relation' : 'palavraQuantidades' , 'scope' : {
				'order' : 'quantidade desc',
				'include' : 'palavraChave'
			}}
		}
	}

}
