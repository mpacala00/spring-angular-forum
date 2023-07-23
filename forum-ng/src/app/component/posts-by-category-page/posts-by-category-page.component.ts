import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryModel } from 'src/app/model/category-model';
import { PostModel } from 'src/app/model/post-model';
import { CategoryApiService } from 'src/app/service/category-api.service';
import { AuthService } from 'src/app/service/auth.service';
import { SubSink } from 'subsink';
import { NewPostDialogComponent } from '../shared/new-post-dialog/new-post-dialog.component';
import { PostApiService } from 'src/app/service/post-api.service';


@Component({
   selector: 'app-posts-by-category-page',
   templateUrl: './posts-by-category-page.component.html',
   styleUrls: ['./posts-by-category-page.component.scss']
})
export class PostsByCategoryPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   category: CategoryModel;
   isUserFollowingCategory: boolean = false;
   posts: PostModel[];
   categoryId: number;

   newPost: PostModel;

   constructor(
      private categoryApiService: CategoryApiService,
      private postApiService: PostApiService,
      private router: Router,
      private activatedRoute: ActivatedRoute,
      public dialog: MatDialog,
      public authService: AuthService) { }

   ngOnInit(): void {
      this.activatedRoute.params.subscribe(
         params => { this.categoryId = params['id']; console.log('param: ' + params['id']); }
      );

      //make a call to a different endpoint depending on user being logged in
      if (this.authService.isTokenSet()) {
         this.getCategoryLoggedIn(this.categoryId);
      } else {
         this.getCategory(this.categoryId);
      }

   }

   openDialog(): void {
      const dialogRef = this.dialog.open(NewPostDialogComponent, {
         width: '450px',
         // to pass data to the dialog:
         data: { post: { title: '', body: '' }, dialogTitle: 'New post' }
      });

      dialogRef.afterClosed().subscribe(result => {
         if (result != null) {
            this.newPost = result.post;
            this.publishPost(this.newPost);
         }
      });
   }

   publishPost(post: PostModel) {
      this.subs.sink = this.postApiService.postPost(this.categoryId, post).subscribe(
         res => {
            //refresh post list
            this.getPostsByCategory(this.categoryId);
         },
         err => {
            alert("An error occured while publishing a post");
         },
         () => { this.newPost = null; }
      )
   }

   //will contain its posts
   getCategory(categoryId: number) {
      this.subs.sink = this.categoryApiService.getCategoryById(categoryId).subscribe(
         res => {
            this.category = res;
            this.posts = res.posts;
         },
         err => {
            alert("An error occured while fetching category");
         }
      )
   }

   getCategoryLoggedIn(categoryId: number) {
      this.subs.sink = this.categoryApiService.getCategoryById(categoryId).subscribe(
         res => {
            this.category = res;
            this.posts = res.posts;
            this.isUserFollowingCategory = res.userFollowing;
         },
         err => {
            alert("An error occured while fetching category");
         }
      )
   }

   //make a call when category is known
   getPostsByCategory(categoryId: number) {
      this.subs.sink = this.postApiService.getPostsByCategory(categoryId).subscribe(
         res => {
            this.posts = res;
         },
         err => {
            alert('An error occured while fetching posts');
         }
      )
   }

   //this function follows or unfollows category depending on the current state
   switchFollowingCategory() {
      if (!this.isUserFollowingCategory) {
         this.subs.sink = this.categoryApiService.followCategory(this.categoryId).subscribe(
            res => {
               this.isUserFollowingCategory = true;
            },
            err => {
               console.error(err);
            }
         )
      }

      else if (this.isUserFollowingCategory) {
         this.subs.sink = this.categoryApiService.unfollowCategory(this.categoryId).subscribe(
            res => {
               this.isUserFollowingCategory = false;
            },
            err => {
               console.error(err);
            }
         )
      }
   }

   public navigateToPost(postId: number) {
      //dot in front of url is mandatory in order for it to be relative 
      let url = './posts/' + postId;
      this.router.navigate([url], { relativeTo: this.activatedRoute });
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
