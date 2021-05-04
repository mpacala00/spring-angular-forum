import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PostContainerComponent } from './component/post-container/post-container.component';
import { LoginPageComponent } from './component/login-page/login-page.component';
import { NewPostComponent } from './component/new-post/new-post.component';
import { RegisterPageComponent } from './component/register-page/register-page.component';

const routes: Routes = [
   {
      path: '',
      component: PostContainerComponent,
      pathMatch: 'full'
   },
   {
      path: 'posts',
      component: PostContainerComponent
   },
   {
      path: 'post',
      children: [
         { path: 'new', component: NewPostComponent }
         // { path: ':id', component: PostByIdComponent }
      ]
   },
   {
      path: 'login',
      component: LoginPageComponent
   },
   {
      path: 'register',
      component: RegisterPageComponent
   }
];

@NgModule({
   imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })], //, {enableTracing: true} <- put this line after routes for feedback in console
   exports: [RouterModule]
})
export class AppRoutingModule { }
