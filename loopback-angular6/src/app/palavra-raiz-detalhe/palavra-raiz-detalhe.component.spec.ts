import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PalavraRaizDetalheComponent } from './palavra-raiz-detalhe.component';

describe('PalavraRaizDetalheComponent', () => {
	let component: PalavraRaizDetalheComponent;
	let fixture: ComponentFixture<PalavraRaizDetalheComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ PalavraRaizDetalheComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(PalavraRaizDetalheComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
