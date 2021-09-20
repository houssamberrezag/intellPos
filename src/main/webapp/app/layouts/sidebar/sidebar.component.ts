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
    /* $('li').Treeview('init');
    $('.sidebar-submenu').Treeview('init');
    $('ul').Treeview('init'); */
    console.log('init');
  }
  ngAfterViewInit(): void {
    //$('[data-widget="treeview"]').Treeview('init');
    console.log('init');
  }
}
