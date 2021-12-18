import { Component, OnInit } from '@angular/core';
declare let $: any;

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  constructor() {
    console.log('');
  }

  ngOnInit(): void {
    console.log('init');
    // $('[data-widget="treeview"]').Treeview('init');
  }
  ngAfterViewInit(): void {
    //$('[data-widget="treeview"]').Treeview('init');
    //$('[data-widget="treeview"]').Treeview('init');
    console.log('dkqj');
  }
}
