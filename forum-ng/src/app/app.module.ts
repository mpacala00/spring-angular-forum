import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { NgxSkeletonLoaderModule } from 'ngx-skeleton-loader';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginPageComponent } from './component/login-page/login-page.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AddTokenInterceptor } from './interceptor/add-token.interceptor';
import { NavComponent } from './component/nav/nav.component';
import { RegisterPageComponent } from './component/register-page/register-page.component';
import { CategoryPageComponent } from './component/category-page/category-page.component';
import { PostsByCategoryPageComponent } from './component/posts-by-category-page/posts-by-category-page.component';
import { CommentsByPostPageComponent } from './component/comments-by-post-page/comments-by-post-page.component';
import { PersonalProfilePageComponent } from './component/user/personal-profile-page/personal-profile-page.component';
import { NewPostDialogComponent } from './component/shared/new-post-dialog/new-post-dialog.component';
import { ConfirmationDialogComponent } from './component/shared/confirmation-dialog/confirmation-dialog.component';
import { AdminPanelPageComponent } from './component/admin-panel-page/admin-panel-page.component';

import { BrowserAnimationsModule } from
   '@angular/platform-browser/animations';

import { MatFormFieldModule }from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatListModule } from '@angular/material/list';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';

import { LayoutModule } from '@angular/cdk/layout';
import { TextFieldModule } from '@angular/cdk/text-field';
import { HasAuthorityDirective } from './directive/has-authority.directive';
import { NewCategoryDialogComponent } from './component/shared/new-category-dialog/new-category-dialog.component';

import { CategorySkeletonComponent } from './component/user/personal-profile-page/skeleton-loading/category-skeleton';
import { PostSkeletonComponent } from './component/user/personal-profile-page/skeleton-loading/post-skeleton';
import { CommentSkeletonComponent } from './component/user/personal-profile-page/skeleton-loading/comment-skeleton';
import { CommentComponent } from './component/comments-by-post-page/comment/comment.component';

@NgModule({
   declarations: [
      AppComponent,
      LoginPageComponent,
      NavComponent,
      RegisterPageComponent,
      CategoryPageComponent,
      PostsByCategoryPageComponent,
      CommentsByPostPageComponent,
      PersonalProfilePageComponent,
      NewPostDialogComponent,
      ConfirmationDialogComponent,
      HasAuthorityDirective,
      NewCategoryDialogComponent,
      AdminPanelPageComponent,

      CategorySkeletonComponent,
      PostSkeletonComponent,
      CommentSkeletonComponent,
      CommentComponent
   ],
   imports: [
      BrowserModule,
      AppRoutingModule,
      HttpClientModule,
      FormsModule,
      ReactiveFormsModule,
      NgxWebstorageModule.forRoot(),
      NgbModule,
      BrowserAnimationsModule,
      NgxSkeletonLoaderModule,

      MatFormFieldModule,
      MatInputModule,
      MatTableModule,
      MatButtonModule,
      LayoutModule,
      MatToolbarModule,
      MatSidenavModule,
      MatIconModule,
      MatListModule,
      MatCardModule,
      MatTabsModule,
      TextFieldModule,
      MatDialogModule,
      MatDividerModule,
      MatSelectModule,
   ],

   providers: [
      { provide: HTTP_INTERCEPTORS, useClass: AddTokenInterceptor, multi: true }
   ],
   bootstrap: [AppComponent]
})
export class AppModule { }
