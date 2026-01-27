import { OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ActivatedRoute } from "@angular/router";
import { OportunidadeLinkedin , OportunidadeLinkedinApi } from "../shared/sdk";

export class OportunidadeLinkedinDetalheBaseComponent implements OnInit {
	principal: OportunidadeLinkedin;
	idPrincipal: number;
	constructor(protected srv: OportunidadeLinkedinApi, protected router: ActivatedRoute, protected dialog: MatDialog) { }

	posCarregaTela() {}

	ngOnInit() {
		this.carregaTela()
	}
	carregaTela() {
		this.router.params.subscribe((params) => {
		this.idPrincipal = params['id'];
		this.srv.findById(this.idPrincipal, this.getFiltro())
	 		.subscribe((result: OportunidadeLinkedin) => {
				this.principal = result;
				console.log('principal:', this.principal);
            	this.posCarregaTela();
			})
		})
	}
	getFiltro() {
		return {}
	}
}
