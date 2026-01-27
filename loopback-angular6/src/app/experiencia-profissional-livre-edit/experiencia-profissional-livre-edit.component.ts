import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { ExperienciaProfissionalLivreApi } from '../shared/sdk';
import { ExperienciaProfissionalLivreEditBaseComponent } from './experiencia-profissional-livre-edit-base.component';

@Component({
	selector: 'app-experiencia-profissional-livre-edit',
  	templateUrl: './experiencia-profissional-livre-edit.component.html',
  	styleUrls: ['./experiencia-profissional-livre-edit.component.css']
})
export class ExperienciaProfissionalLivreEditComponent extends ExperienciaProfissionalLivreEditBaseComponent {

	 constructor(protected dialogRef: MatDialogRef<any>
	    , @Inject(MAT_DIALOG_DATA) protected data: any, protected servico: ExperienciaProfissionalLivreApi
		  ) {
	   super(dialogRef,data,servico);
	}

}
