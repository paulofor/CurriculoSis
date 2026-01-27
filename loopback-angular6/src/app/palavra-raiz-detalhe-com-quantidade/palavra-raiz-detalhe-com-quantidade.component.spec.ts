import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PalavraRaizDetalheComQuantidadeComponent } from './palavra-raiz-detalhe-com-quantidade.component';

describe('PalavraRaizDetalheComQuantidadeComponent', () => {
	let component: PalavraRaizDetalheComQuantidadeComponent;
	let fixture: ComponentFixture<PalavraRaizDetalheComQuantidadeComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ PalavraRaizDetalheComQuantidadeComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(PalavraRaizDetalheComQuantidadeComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
