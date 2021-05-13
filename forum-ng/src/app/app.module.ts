import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PostContainerComponent } from './component/post-container/post-container.component';
import { LoginPageComponent } from './component/login-page/login-page.component';
import { NewPostComponent } from './component/new-post/new-post.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { BrowserAnimationsModule } from
   '@angular/platform-browser/animations';
import { MatFormFieldModule }
   from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatDialogModule } from '@angular/material/dialog';

import { NavComponent } from './component/nav/nav.component';
import { RegisterPageComponent } from './component/register-page/register-page.component';
import { CategoryPageComponent } from './component/category-page/category-page.component';
import { PostsByCategoryPageComponent } from './component/posts-by-category-page/posts-by-category-page.component';
import { CommentsByPostPageComponent } from './component/comments-by-post-page/comments-by-post-page.component';
import { PersonalProfilePageComponent } from './component/user/personal-profile-page/personal-profile-page.component';
import { NewPostDialogComponent } from './component/posts-by-category-page/new-post-dialog/new-post-dialog.component';

@NgModule({
   declarations: [
      AppComponent,
      PostContainerComponent,
      LoginPageComponent,
      NewPostComponent,
      NavComponent,
      RegisterPageComponent,
      CategoryPageComponent,
      PostsByCategoryPageComponent,
      CommentsByPostPageComponent,
      PersonalProfilePageComponent,
      NewPostDialogComponent
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
      MatDialogModule
   ],

   providers: [],
   bootstrap: [AppComponent]
})
export class AppModule { }
