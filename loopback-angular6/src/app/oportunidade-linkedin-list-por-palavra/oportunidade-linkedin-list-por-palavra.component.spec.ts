import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OportunidadeLinkedinListPorPalavraComponent } from './oportunidade-linkedin-list-por-palavra.component';

describe('OportunidadeLinkedinListPorPalavraComponent', () => {
	let component: OportunidadeLinkedinListPorPalavraComponent;
	let fixture: ComponentFixture<OportunidadeLinkedinListPorPalavraComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ OportunidadeLinkedinListPorPalavraComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(OportunidadeLinkedinListPorPalavraComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
