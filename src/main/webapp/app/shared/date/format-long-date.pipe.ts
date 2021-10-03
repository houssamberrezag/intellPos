import { Pipe, PipeTransform } from '@angular/core';

import * as dayjs from 'dayjs';
@Pipe({
  name: 'formatLongDate',
})
export class FormatLongDatePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('DD MMMM YYYY') : '';
  }
}
