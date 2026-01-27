import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { OportunidadeLinkedinApi } from '../shared/sdk';
import { OportunidadeLinkedinDetalheBaseComponent } from './oportunidade-linkedin-detalhe-base.component';

@Component({
	selector: 'app-oportunidade-linkedin-detalhe',
  	templateUrl: './oportunidade-linkedin-detalhe.component.html',
  	styleUrls: ['./oportunidade-linkedin-detalhe.component.css']
})
export class OportunidadeLinkedinDetalheComponent extends OportunidadeLinkedinDetalheBaseComponent {

	constructor(protected srv: OportunidadeLinkedinApi, protected router: ActivatedRoute, protected dialog: MatDialog) { 
		super(srv,router,dialog);
	}

	getFiltro(): {} {
		return {
			'include' : {'relation' : 'oportunidadePalavras' , 'scope' : {'include' : 'palavraChave'}}
		}
	}
}
