import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PalavraChaveEditComponent } from './palavra-chave-edit.component';

describe('PalavraChaveEditComponent', () => {
	let component: PalavraChaveEditComponent;
	let fixture: ComponentFixture<PalavraChaveEditComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ PalavraChaveEditComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(PalavraChaveEditComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
