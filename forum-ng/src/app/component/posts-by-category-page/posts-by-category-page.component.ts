import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryModel } from 'src/app/model/category-model';
import { PostModel } from 'src/app/model/post-model';
import { ApiService } from 'src/app/service/api.service';
import { SubSink } from 'subsink';
import { NewPostDialogComponent } from './new-post-dialog/new-post-dialog.component';


@Component({
   selector: 'app-posts-by-category-page',
   templateUrl: './posts-by-category-page.component.html',
   styleUrls: ['./posts-by-category-page.component.scss']
})
export class PostsByCategoryPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   category: CategoryModel;
   posts: PostModel[];
   categoryId: number;

   newPost: PostModel;

   constructor(private apiService: ApiService,
               private router: Router,
               private activatedRoute: ActivatedRoute,
               public dialog: MatDialog) { }

   ngOnInit(): void {
      this.activatedRoute.params.subscribe(
         params => { this.categoryId = params['id']; }
      );
      this.getCategory(this.categoryId);
   }

   openDialog(): void {
      const dialogRef = this.dialog.open(NewPostDialogComponent, {
         width: '450px',
         // to pass data to the dialog:
         data: {post: {title: '', body: ''}}
       });

       dialogRef.afterClosed().subscribe(result => {
         if(result != null) {
            this.newPost = result.post;
            this.publishPost(this.newPost);
         }
       });
   }

   publishPost(post: PostModel) {
      this.subs.sink = this.apiService.postPost(this.categoryId, post).subscribe(
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
      this.subs.sink = this.apiService.getCategoryById(categoryId).subscribe(
         res => {
            this.category = res;
            this.posts = res.posts;
         },
         err => {
            alert("An error occured while fetching category");
         }
      )
   }

   //make a call when category is known
   getPostsByCategory(categoryId: number) {
      this.subs.sink = this.apiService.getPostsByCategory(categoryId).subscribe(
         res => {
            this.posts = res;
         },
         err => {
            alert('An error occured while fetching posts');
         }
      )
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
