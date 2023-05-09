import { Component } from '@angular/core';

@Component({
   selector: 'comment-skeleton',
   template: `<div class="skeleton-comments">
      <div class="my-2">
         <hr />
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
               width: '300px',
               height: '64'
            }"
         >
         </ngx-skeleton-loader
         ><br />
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
            height: '64',
            width: '1000px',}"
         >
         </ngx-skeleton-loader>
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
            height: '64',
            width: '400px',}"
         >
         </ngx-skeleton-loader>
      </div>

      <div class="my-2">
         <hr />
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
               width: '300px',
               height: '64'
            }"
         >
         </ngx-skeleton-loader
         ><br />
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
            height: '64',
            width: '1000px',}"
         >
         </ngx-skeleton-loader>
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
            height: '64',
            width: '400px',}"
         >
         </ngx-skeleton-loader>
      </div>
      <div class="my-2">
         <hr />
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
               width: '300px',
               height: '64'
            }"
         >
         </ngx-skeleton-loader
         ><br />
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
            height: '64',
            width: '1000px',}"
         >
         </ngx-skeleton-loader>
         <ngx-skeleton-loader
            count="1"
            appearance="line"
            animation="progress"
            [theme]="{
            height: '64',
            width: '400px',}"
         >
         </ngx-skeleton-loader>
      </div>
   </div>`,
   styles: [],
})
export class CommentSkeletonComponent {}
