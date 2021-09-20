import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPerson } from '../person.model';

@Component({
  selector: 'jhi-detail-client',
  templateUrl: './detail-client.component.html',
  styleUrls: ['./detail-client.component.scss'],
})
export class DetailClientComponent implements OnInit {
  person: IPerson | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
