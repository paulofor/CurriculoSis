import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { OportunidadeLinkedinApi, PalavraRaizApi } from '../shared/sdk';
import { PalavraRaizDetalheBaseComponent } from './palavra-raiz-detalhe-base.component';

@Component({
	selector: 'app-palavra-raiz-detalhe',
  	templateUrl: './palavra-raiz-detalhe.component.html',
  	styleUrls: ['./palavra-raiz-detalhe.component.css']
})
export class PalavraRaizDetalheComponent extends PalavraRaizDetalheBaseComponent {

	constructor(protected srv: PalavraRaizApi, protected router: ActivatedRoute, protected dialog: MatDialog, private srvOportunidade: OportunidadeLinkedinApi) { 
		super(srv,router,dialog);
	}

	getFiltro(): {} {
		return {
			'include' : {'relation' : 'oportunidadeLinkedins' , 'scope' : {'where' : {'maisRecente' : 1},'order' : 'volume'}}
		}
	}

	envia(rel) {
		this.srvOportunidade.RegistraEnvio(rel.id)
			.subscribe((result) => {
				this.carregaTela();
			})
	}

}
