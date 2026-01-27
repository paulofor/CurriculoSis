import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperienciaProfissionalLivreListComponent } from './experiencia-profissional-livre-list.component';

describe('ExperienciaProfissionalLivreListComponent', () => {
	let component: ExperienciaProfissionalLivreListComponent;
	let fixture: ComponentFixture<ExperienciaProfissionalLivreListComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ ExperienciaProfissionalLivreListComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(ExperienciaProfissionalLivreListComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
