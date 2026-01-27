import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PalavraChaveListComponent } from './palavra-chave-list.component';

describe('PalavraChaveListComponent', () => {
	let component: PalavraChaveListComponent;
	let fixture: ComponentFixture<PalavraChaveListComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ PalavraChaveListComponent ]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(PalavraChaveListComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
