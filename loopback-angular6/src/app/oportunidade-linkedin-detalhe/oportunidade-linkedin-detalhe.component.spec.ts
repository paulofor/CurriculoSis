import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OportunidadeLinkedinDetalheComponent } from './oportunidade-linkedin-detalhe.component';

describe('OportunidadeLinkedinDetalheComponent', () => {
	let component: OportunidadeLinkedinDetalheComponent;
	let fixture: ComponentFixture<OportunidadeLinkedinDetalheComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ OportunidadeLinkedinDetalheComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(OportunidadeLinkedinDetalheComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
