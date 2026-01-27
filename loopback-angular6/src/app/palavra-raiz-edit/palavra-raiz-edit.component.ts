import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { PalavraRaizApi } from '../shared/sdk';
import { PalavraRaizEditBaseComponent } from './palavra-raiz-edit-base.component';

@Component({
	selector: 'app-palavra-raiz-edit',
  	templateUrl: './palavra-raiz-edit.component.html',
  	styleUrls: ['./palavra-raiz-edit.component.css']
})
export class PalavraRaizEditComponent extends PalavraRaizEditBaseComponent {

	 constructor(protected dialogRef: MatDialogRef<any>
	    , @Inject(MAT_DIALOG_DATA) protected data: any, protected servico: PalavraRaizApi
		  ) {
	   super(dialogRef,data,servico);
	}

}
