import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PalavraRaizEditComponent } from './palavra-raiz-edit.component';

describe('PalavraRaizEditComponent', () => {
	let component: PalavraRaizEditComponent;
	let fixture: ComponentFixture<PalavraRaizEditComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ PalavraRaizEditComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(PalavraRaizEditComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
