import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserApiService } from 'src/app/service/user-api.service';
import { UserModel } from 'src/app/model/user-model';
import { SubSink } from 'subsink';
import { PostModel } from 'src/app/model/post-model';
import { CommentModel } from 'src/app/model/comment-model';
import { CategoryModel } from 'src/app/model/category-model';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';
import { CategoryApiService } from 'src/app/service/category-api.service';
import { MatLegacyDialog as MatDialog } from '@angular/material/legacy-dialog';
import { ConfirmationDialogComponent, ConfirmationDialogModel } from '../../shared/confirmation-dialog/confirmation-dialog.component';

@Component({
   selector: 'app-personal-profile-page',
   templateUrl: './personal-profile-page.component.html',
   styleUrls: ['./personal-profile-page.component.scss']
})
export class PersonalProfilePageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   private profileUsername: string;

   user: UserModel;
   userPosts: PostModel[];
   userComments: CommentModel[];
   userFollowedCategories: CategoryModel[];

   //is currently logged in user owner of the profile
   isOwner: boolean = false;

   constructor(private userApiService: UserApiService,
      private activatedRoute: ActivatedRoute,
      private authService: AuthService,
      private categoryApiService: CategoryApiService,
      public dialog: MatDialog) { }

   ngOnInit(): void {
      this.activatedRoute.params.subscribe(
         params => {
            this.profileUsername = params['username'];
            console.log(this.profileUsername);
         }
      )
      this.getUserProfileInfo(this.profileUsername);
      this.isOwner = this.authService.checkIfEntityIsOwned(this.profileUsername);
   }

   public getUserProfileInfo(username: string) {
      this.subs.sink = this.userApiService.getUserProfileInfo(username).subscribe(
         res => {
            this.user = res;
         },
         err => {
            alert("Could not retrieve user profile info");
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

   unfollowCategory(index: number) {
      let categoryId = this.userFollowedCategories[index].id;

      this.categoryApiService.unfollowCategory(categoryId).subscribe(
         res => {
            //refresh category list
            this.getUserFollowedCategories();
         },
         err => {
            alert('An error occured');
         }
      )
   }

   openBlockUserDialog() {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
         width: '400px',
         data: new ConfirmationDialogModel('Are you sure?', 'This action cannot be undone')
      });

      let deletePost: boolean;

      dialogRef.afterClosed().subscribe(dialogResult => {
         if (dialogResult == true) {
            this.blockUser();
         }
      });
   }

   blockUser() {
      this.subs.sink = this.userApiService.blockUser(this.user.id).subscribe(
         res => {
            this.getUserProfileInfo(this.profileUsername);
         },
         err => {
            alert("An error occured while blocking user");
         }
      );
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
