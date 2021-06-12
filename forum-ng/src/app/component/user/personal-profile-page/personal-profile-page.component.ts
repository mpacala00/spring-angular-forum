import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserApiService } from 'src/app/service/user-api.service';
import { UserModel } from 'src/app/model/user-model';
import { SubSink } from 'subsink';
import { PostModel } from 'src/app/model/post-model';
import { CommentModel } from 'src/app/model/comment-model';
import { CategoryModel } from 'src/app/model/category-model';

@Component({
   selector: 'app-personal-profile-page',
   templateUrl: './personal-profile-page.component.html',
   styleUrls: ['./personal-profile-page.component.scss']
})
export class PersonalProfilePageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   user: UserModel;
   userPosts: PostModel[];
   userComments: CommentModel[];
   userFollowedCategories: CategoryModel[];

   //is currently logged in user owner of the profile
   isOwner: boolean = false;

   constructor(private userApiService: UserApiService) { }

   ngOnInit(): void {
      this.getCurrentUser();
   }

   public getCurrentUser() {
      this.subs.sink = this.userApiService.getCurrentUser().subscribe(
         res => {
            this.user = res;
         },
         err => {
            alert("Could not retrieve current user");
         }
      )
   }

   public getUserPosts() {
      this.subs.sink = this.userApiService.getPostsByUsername(this.user.username).subscribe(
         res => {
            this.userPosts = res;
         },
         err => {
            alert("An error occured while fetching user posts");
         }
      )
   }

   public getUserComments() {
      this.subs.sink = this.userApiService.getCommentsByUsername(this.user.username).subscribe(
         res => {
            this.userComments = res;
         },
         err => {
            alert("An error occured while fetching user comments");
         }
      )
   }

   public getUserFollowedCategories() {
      this.subs.sink = this.userApiService.getFollowedCategoriesByUsername(this.user.username).subscribe(
         res => {
            this.userFollowedCategories = res;
         },
         err => {
            alert("An error occured while fetching followed categories");
         }
      )
   }

   public createPostUrl(categoryId: number, postId: number) {
      return `/categories/${categoryId}/posts/${postId}`;
   }

   public createCategoryUrl(categoryId: number) {
      return `/categories/${categoryId}`;
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
