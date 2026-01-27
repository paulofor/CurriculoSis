import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { PalavraChaveApi } from '../shared/sdk';
import { PalavraChaveEditBaseComponent } from './palavra-chave-edit-base.component';

@Component({
	selector: 'app-palavra-chave-edit',
  	templateUrl: './palavra-chave-edit.component.html',
  	styleUrls: ['./palavra-chave-edit.component.css']
})
export class PalavraChaveEditComponent extends PalavraChaveEditBaseComponent {

	 constructor(protected dialogRef: MatDialogRef<any>
	    , @Inject(MAT_DIALOG_DATA) protected data: any, protected servico: PalavraChaveApi
		  ) {
	   super(dialogRef,data,servico);
	}

}
