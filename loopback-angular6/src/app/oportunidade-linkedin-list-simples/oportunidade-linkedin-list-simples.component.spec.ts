import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OportunidadeLinkedinListSimplesComponent } from './oportunidade-linkedin-list-simples.component';

describe('OportunidadeLinkedinListSimplesComponent', () => {
	let component: OportunidadeLinkedinListSimplesComponent;
	let fixture: ComponentFixture<OportunidadeLinkedinListSimplesComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ OportunidadeLinkedinListSimplesComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(OportunidadeLinkedinListSimplesComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
