import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PostContainerComponent } from './component/post-container/post-container.component';
import { LoginPageComponent } from './component/login-page/login-page.component';
import { NewPostComponent } from './component/new-post/new-post.component';
import { RegisterPageComponent } from './component/register-page/register-page.component';
import { CategoryPageComponent } from './component/category-page/category-page.component';
import { PostsByCategoryPageComponent } from './component/posts-by-category-page/posts-by-category-page.component';
import { CommentsByPostPageComponent } from './component/comments-by-post-page/comments-by-post-page.component';
import { PersonalProfilePageComponent } from './component/user/personal-profile-page/personal-profile-page.component';

const routes: Routes = [
   {
      //routes are done this done in order to have nested routes without sub router-outlets
      //in child components - completly replace parent's component with a new one
      path: 'categories',
      children: [
         {
            path: '',
            component: CategoryPageComponent
         },
         {
            path: ':id',
            children: [
               {
                  path: '',
                  component: PostsByCategoryPageComponent
               },
               {
                  path: 'posts/:id',
                  component: CommentsByPostPageComponent
               }

            ]
         }
      ]
   },
   {
      path: 'user',
      children: [
         {
            path: 'current',
            component: PersonalProfilePageComponent
         }
      ]
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
