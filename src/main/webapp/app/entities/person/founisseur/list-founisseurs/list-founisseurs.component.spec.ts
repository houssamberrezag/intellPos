import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListFounisseursComponent } from './list-founisseurs.component';

describe('ListFounisseursComponent', () => {
  let component: ListFounisseursComponent;
  let fixture: ComponentFixture<ListFounisseursComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListFounisseursComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListFounisseursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
