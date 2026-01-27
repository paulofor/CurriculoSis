import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { ExperienciaProfissionalLivreApi, LoopBackFilter } from '../shared/sdk';
import { ExperienciaProfissionalLivreListBaseComponent } from './experiencia-profissional-livre-list-base.component';

@Component({
	selector: 'app-experiencia-profissional-livre-list',
  	templateUrl: './experiencia-profissional-livre-list.component.html',
  	styleUrls: ['./experiencia-profissional-livre-list.component.css']
})
export class ExperienciaProfissionalLivreListComponent extends ExperienciaProfissionalLivreListBaseComponent {

	constructor(protected srv: ExperienciaProfissionalLivreApi, protected router: ActivatedRoute, protected dialog: MatDialog) { 
		super(srv,router,dialog);
	}

	getFiltro(): {} {
		return {
			'order' : 'dataInicio desc'
		}
	}
}
