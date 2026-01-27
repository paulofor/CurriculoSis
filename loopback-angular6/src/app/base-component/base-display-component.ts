import { Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export class BaseDisplayComponent implements OnInit{

    item: any;
    origem: any;

    constructor(protected dialogRef: MatDialogRef<any>
        , @Inject(MAT_DIALOG_DATA) protected data: any, protected servico: any,
      ) {
      }
    

  ngOnInit() {
    
    console.log("Data(BaseDisplay):", this.data);
    this.origem = this.data;
    let filtro = this.getFiltro();
    console.log('Filtro:' , filtro);
    this.servico.find(filtro)
      .subscribe((resultado) => {
        this.item = resultado;
        console.log('Item:' , this.item);
        this.posCarrega();
      })
   
  }


  getFiltro() {
    return null;
  }

  posCarrega() {

  }



  closeDialog() {
    this.dialogRef.close('Pizza!');
  }



}