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
import { NavComponent } from './component/nav/nav.component';

@NgModule({
   declarations: [
      AppComponent,
      PostContainerComponent,
      LoginPageComponent,
      NewPostComponent,
      NavComponent
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
      MatListModule
   ],

   providers: [],
   bootstrap: [AppComponent]
})
export class AppModule { }
