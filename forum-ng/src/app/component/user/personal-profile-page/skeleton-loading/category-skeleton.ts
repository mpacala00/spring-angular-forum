import { Component } from '@angular/core';

@Component({
   selector: 'category-skeleton',
   template: `
      <ngx-skeleton-loader
         count="3"
         appearance="line"
         animation="progress"
         [theme]="{
            height: '100px',
            'border-radius': '4px'
         }"
      >
      </ngx-skeleton-loader>
   `,
   styles: [],
})
export class CategorySkeletonComponent {}
