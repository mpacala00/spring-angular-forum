import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PostContainerComponent } from './component/post-container/post-container.component';
import { LoginPageComponent } from './component/login-page/login-page.component';
import { NewPostComponent } from './component/new-post/new-post.component';
import { RegisterPageComponent } from './component/register-page/register-page.component';
import { CategoryPageComponent } from './component/category-page/category-page.component';
import { PostsByCategoryPageComponent } from './component/posts-by-category-page/posts-by-category-page.component';

const routes: Routes = [
   {
      path: 'categories',
      component: CategoryPageComponent,
   },
   {
      path: 'categories/:id',
      component: PostsByCategoryPageComponent
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
   },
   {
      path: '',
      component: CategoryPageComponent,
      pathMatch: 'full'
   }
];

@NgModule({
   imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })], //, {enableTracing: true} <- put this line after routes for feedback in console
   exports: [RouterModule]
})
export class AppRoutingModule { }
