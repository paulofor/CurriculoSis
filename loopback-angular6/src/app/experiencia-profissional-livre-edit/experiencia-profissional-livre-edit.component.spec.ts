import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperienciaProfissionalLivreEditComponent } from './experiencia-profissional-livre-edit.component';

describe('ExperienciaProfissionalLivreEditComponent', () => {
	let component: ExperienciaProfissionalLivreEditComponent;
	let fixture: ComponentFixture<ExperienciaProfissionalLivreEditComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ ExperienciaProfissionalLivreEditComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ExperienciaProfissionalLivreEditComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
