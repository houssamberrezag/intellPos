import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class SweetAlertService {
  //constructor() { }

  create(title: string, msg: string, icon: any): void {
    Swal.fire({
      title,
      text: msg,
      icon,
    });
  }
}
