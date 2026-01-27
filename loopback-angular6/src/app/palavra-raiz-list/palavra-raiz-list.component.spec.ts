import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PalavraRaizListComponent } from './palavra-raiz-list.component';

describe('PalavraRaizListComponent', () => {
	let component: PalavraRaizListComponent;
	let fixture: ComponentFixture<PalavraRaizListComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ PalavraRaizListComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(PalavraRaizListComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
