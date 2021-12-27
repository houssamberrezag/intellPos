import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { SweetAlertService } from 'app/core/util/sweet-alert.service';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { SettingsService } from '../service/settings.service';
import { ISettings, Settings } from '../settings.model';

@Component({
  selector: 'jhi-settings-update',
  templateUrl: './settings-update.component.html',
  styleUrls: ['./settings-update.component.scss'],
})
export class SettingsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    slogan: [null, [Validators.required, Validators.maxLength(150)]],
    address: [null, [Validators.required, Validators.maxLength(350)]],
    email: [null, [Validators.required, Validators.email]],
    phone: [null, [Validators.required, Validators.maxLength(20)]],
  });

  constructor(protected settingsService: SettingsService, protected fb: FormBuilder, protected alertService: SweetAlertService) {}

  ngOnInit(): void {
    this.settingsService.getCurrentSettings().subscribe(settings => {
      if (settings) {
        this.updateForm(settings);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const settings = this.createFromForm();
    if (settings.id !== undefined && settings.id !== null) {
      this.subscribeToSaveResponse(this.settingsService.update(settings));
    } else {
      this.subscribeToSaveResponse(this.settingsService.create(settings));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISettings>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res.body),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(settings: ISettings | null): void {
    this.settingsService.shareNewSettings(settings);
    this.alertService.create('', 'modification éffectuée avec succées', 'success');
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(settings: ISettings): void {
    this.editForm.patchValue({
      id: settings.id,
      name: settings.name,
      slogan: settings.slogan,
      address: settings.address,
      email: settings.email,
      phone: settings.phone,
    });
  }

  protected createFromForm(): ISettings {
    return {
      ...new Settings(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      slogan: this.editForm.get(['slogan'])!.value,
      address: this.editForm.get(['address'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
    };
  }
}
