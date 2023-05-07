import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';

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
import { MatLegacyFormFieldModule as MatFormFieldModule }
   from '@angular/material/legacy-form-field';
import { MatLegacyInputModule as MatInputModule } from '@angular/material/legacy-input';
import { MatLegacyTableModule as MatTableModule } from '@angular/material/legacy-table';
import { MatLegacyButtonModule as MatButtonModule } from '@angular/material/legacy-button';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatLegacyListModule as MatListModule } from '@angular/material/legacy-list';
import { MatLegacyCardModule as MatCardModule } from '@angular/material/legacy-card';
import { MatLegacyTabsModule as MatTabsModule } from '@angular/material/legacy-tabs';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatLegacyDialogModule as MatDialogModule } from '@angular/material/legacy-dialog';
import { MatDividerModule } from '@angular/material/divider';
import { HasAuthorityDirective } from './directive/has-authority.directive';
import { NewCategoryDialogComponent } from './component/shared/new-category-dialog/new-category-dialog.component';
import { MatLegacySelectModule as MatSelectModule } from '@angular/material/legacy-select';

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
      AdminPanelPageComponent
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
      MatSelectModule
   ],

   providers: [
      { provide: HTTP_INTERCEPTORS, useClass: AddTokenInterceptor, multi: true }
   ],
   bootstrap: [AppComponent]
})
export class AppModule { }
