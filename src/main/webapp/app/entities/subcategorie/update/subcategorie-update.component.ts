import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISubcategorie, Subcategorie } from '../subcategorie.model';
import { SubcategorieService } from '../service/subcategorie.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

@Component({
  selector: 'jhi-subcategorie-update',
  templateUrl: './subcategorie-update.component.html',
})
export class SubcategorieUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategorie[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(191)]],
    createdAt: [],
    updatedAt: [],
    deletedAt: [],
    categorie: [],
  });

  constructor(
    protected subcategorieService: SubcategorieService,
    protected categorieService: CategorieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subcategorie }) => {
      if (subcategorie.id === undefined) {
        const today = dayjs().startOf('day');
        subcategorie.createdAt = today;
        subcategorie.updatedAt = today;
        subcategorie.deletedAt = today;
      }

      this.updateForm(subcategorie);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subcategorie = this.createFromForm();
    if (subcategorie.id !== undefined) {
      this.subscribeToSaveResponse(this.subcategorieService.update(subcategorie));
    } else {
      this.subscribeToSaveResponse(this.subcategorieService.create(subcategorie));
    }
  }

  trackCategorieById(index: number, item: ICategorie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubcategorie>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(subcategorie: ISubcategorie): void {
    this.editForm.patchValue({
      id: subcategorie.id,
      name: subcategorie.name,
      createdAt: subcategorie.createdAt ? subcategorie.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: subcategorie.updatedAt ? subcategorie.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: subcategorie.deletedAt ? subcategorie.deletedAt.format(DATE_TIME_FORMAT) : null,
      categorie: subcategorie.categorie,
    });

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      subcategorie.categorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): ISubcategorie {
    return {
      ...new Subcategorie(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      deletedAt: this.editForm.get(['deletedAt'])!.value ? dayjs(this.editForm.get(['deletedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      categorie: this.editForm.get(['categorie'])!.value,
    };
  }
}
