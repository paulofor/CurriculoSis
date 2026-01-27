import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { OportunidadeLinkedinApi, PalavraChave, PalavraChaveApi, PalavraRaiz, PalavraRaizApi } from '../shared/sdk';
import { OportunidadeLinkedinListPorPalavraBaseComponent } from './oportunidade-linkedin-list-por-palavra-base.component';

@Component({
	selector: 'app-oportunidade-linkedin-list-por-palavra',
  	templateUrl: './oportunidade-linkedin-list-por-palavra.component.html',
  	styleUrls: ['./oportunidade-linkedin-list-por-palavra.component.css']
})
export class OportunidadeLinkedinListPorPalavraComponent extends OportunidadeLinkedinListPorPalavraBaseComponent {

	idPalavra:number;
	idRaiz:number;

	palavraRaiz:PalavraRaiz;
	palavraChave:PalavraChave;

	constructor(protected srv: OportunidadeLinkedinApi, private srvRaiz: PalavraRaizApi, private srvChave:PalavraChaveApi, protected router: ActivatedRoute, protected dialog: MatDialog) { 
		super(srv,router,dialog);
	}

	carregaTela() {
        this.router.params.subscribe((params) => {
            this.idPalavra = params['idPalavra'];
			this.idRaiz = params['idRaiz'];
			this.srv.ObtemPorChaveRaiz(this.idRaiz,this.idPalavra)
              .subscribe((result: any) => {
				console.log('result-oportunidade-palavra', result)
				this.listaBase = result;
              });
			this.srvRaiz.findById(this.idRaiz)
			  .subscribe((raiz:PalavraRaiz) => {
				this.palavraRaiz = raiz;
			  })
			this.srvChave.findById(this.idPalavra)
			  .subscribe((chave:PalavraChave) => {
				this.palavraChave = chave;
			  })
          })
        
    }

}
