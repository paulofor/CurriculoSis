import { OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { PERCENTUAL_AVISO } from "../constantes/base.url";

import { BaseLoopBackApi } from "../shared/sdk";

export class BaseListComponent implements OnInit {

    listaBase: any[];

    constructor(protected dialog: MatDialog, protected srv:BaseLoopBackApi) { }


    ngOnInit() {
        this.preCarregaTela();
        this.carregaTela();
    }

    preCarregaTela() {}
    posCarregaLista() {}

    carregaTela() {
        this.srv.find(this.getFiltro())
        .subscribe((result:any[]) => {
            console.log('result: ' , result);
            this.listaBase = result;
            this.posCarregaLista();
        })
    }

    edita(edicao?) {
        this.dialog.afterAllClosed.subscribe(result => {
            this.carregaTela();
        });
        this.dialog.open(this.getComponente(), {
            width: '800px',
            data: {
                item: edicao
            }
        });
    }

    dialogo1(dados) {
        console.log('dialogo1.dados:' , dados);
        this.dialog.afterAllClosed.subscribe(result => {
            this.carregaTela();
        });
        this.dialog.open(this.getDialogo1(), {
            width: '800px',
            data: dados
        });
    }
    getDialogo1() : any {

    }

    dialogo2(dados) {
        console.log('dialogo2.dados:' , dados);
        this.dialog.afterAllClosed.subscribe(result => {
            this.carregaTela();
        });
        this.dialog.open(this.getDialogo2(), {
            width: '800px',
            data: dados
        });
    }
    getDialogo2() : any {

    }


    getComponente():any {}

    getFiltro() {
        return {}
    }

    percentual(valorPerc,valor) {
        let result = ((valorPerc - valor) / valor) * 100;
        return result.toFixed(1);
    }
    verificaMarcaTexto(valorPerc,valor):string {
        let perc =  ((valorPerc - valor) / valor) * 100;
        if (perc<=PERCENTUAL_AVISO && perc>=(PERCENTUAL_AVISO*-1)) {
          return "dgc-marcatexto"
        } else {
          return "";
        }
      }
}