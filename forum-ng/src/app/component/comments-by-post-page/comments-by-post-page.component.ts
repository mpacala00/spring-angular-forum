import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommentModel } from 'src/app/model/comment-model';
import { PostModel } from 'src/app/model/post-model';
import { PostApiService } from 'src/app/service/post-api.service';
import { CommentApiService } from 'src/app/service/comment-api.service';
import { SubSink } from 'subsink';
import { NewPostDialogComponent } from '../shared/new-post-dialog/new-post-dialog.component';
import { ConfirmationDialogModel, ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';
import { AuthService } from 'src/app/service/auth.service';

@Component({
   selector: 'app-comments-by-post-page',
   templateUrl: './comments-by-post-page.component.html',
   styleUrls: ['./comments-by-post-page.component.scss']
})
export class CommentsByPostPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   private postId: number;
   public post: PostModel;
   public comments: CommentModel[];
   public commentForm: FormGroup;

   public edditedComments = [];

   public isOwnerOfPost = false;

   constructor(
      private route: ActivatedRoute,
      private router: Router,
      private commentApiService: CommentApiService,
      private postApiService: PostApiService,
      private authService: AuthService,
      public dialog: MatDialog,) { }

   ngOnInit(): void {
      this.commentForm = new FormGroup({
         body: new FormControl('', [Validators.required, Validators.minLength(1)])
      })
      //get id from current route
      this.route.params.subscribe(
         params => { this.postId = params['id']; }
      );

      this.getPostById(this.postId);
   }

   private getPostById(postId: number): void {
      this.subs.sink = this.postApiService.getPostComments(postId).subscribe(
         res => {
            this.post = res;

            //checking ownership
            if (this.authService.isTokenSet()) {
               this.isOwnerOfPost = this.checkIfOwner(this.post.creator);
            }

            this.refreshComments();
         },
         err => {
            alert('An error occured while fetching posts');
            this.router.navigate(['../../'], { relativeTo: this.route });
         }
      )
   }

   //check if currently logged-in user is the owner of this post
   private checkIfOwner(usernameToCheck: string): boolean {
      return this.authService.checkIfEntityIsOwned(usernameToCheck);
   }

   openEditPostDialog(): void {
      const dialogRef = this.dialog.open(NewPostDialogComponent, {
         width: '450px',
         // to pass data to the dialog:
         data: { post: this.post, dialogTitle: 'Edit post' }
      });

      dialogRef.afterClosed().subscribe(result => {
         if (result != null) {

            //result is of type PostModel, but since PostUpdateDTO takes only id, title and body on the back-end
            //rest of the fields won't be mapped
            this.updatePost(result.post);
         }
      });
   }

   public updatePost(post: PostModel) {
      this.subs.sink = this.postApiService.putPost(post).subscribe(
         res => {
            this.refreshComments();
         },
         err => {
            alert("An error occured while updating post");
            console.error(err);
         }
      )
   }

   public updateComment(comment: CommentModel, body: string) {
      if (body == "") {
         return;
      }

      comment.body = body;

      this.subs.sink = this.commentApiService.putComment(comment).subscribe(
         res => {
            this.refreshComments();
         },
         err => {
            alert("An error occured");
            console.error(err);
         }
      )

      console.log(comment);
   }

   public openDeleteCommentDialog(commentId: number) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
         width: '400px',
         data: new ConfirmationDialogModel('Are you sure?', 'This action cannot be undone')
      });

      let deletePost: boolean;

      dialogRef.afterClosed().subscribe(dialogResult => {
         if (dialogResult == true) {
            this.deleteComment(commentId);
         }
      });
   }

   public openDeletePostDialog() {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
         width: '400px',
         data: new ConfirmationDialogModel('Are you sure?', 'This action cannot be undone')
      });

      let deletePost: boolean;

      dialogRef.afterClosed().subscribe(dialogResult => {
         if (dialogResult == true) {
            this.deletePost();
         }
      });
   }

   public editComment(index: number) {
      let input = document.getElementById('commentEditDiv' + index);
      if (input.classList.contains('d-inline-block')) {
         input.classList.remove('d-inline-block');
      } else {
         input.classList.add('d-inline-block');
      }
   }

   // public editComment(index: number) {
   //    if (this.edditedComments.includes(index)) {
   //       this.edditedComments.splice(index);
   //    }
   //    else {
   //       this.edditedComments.push(index);
   //    }
   // }

   // public isCommentEddited(index: number) {
   //    this.edditedComments.includes(index);
   // }

   public deletePost() {
      this.subs.sink = this.postApiService.deletePost(this.postId).subscribe(
         res => {
            // console.log(res);
            this.router.navigateByUrl('/');
         },
         err => {
            alert('An error occurred during post delete');
            console.error(err);
         }
      )
   }

   public deleteComment(commentId: number) {
      this.subs.sink = this.commentApiService.deleteComment(commentId).subscribe(
         res => {
            this.refreshComments();
         },
         err => {
            alert('An error occurred during comment delete');
            console.error(err);
         }
      )
   }

   public onCommentPost() {
      if (this.commentForm.valid) {
         this.subs.sink = this.commentApiService.postComment(this.postId, this.commentForm.value).subscribe(
            res => {
               this.refreshComments();
            },
            err => {
               alert("An error occured while posting a comment");
               console.error(err);
            }
         );
         this.commentForm.reset();
      }

   }

   public refreshComments() {
      if (this.postId) {
         this.subs.sink = this.commentApiService.getCommentsByPost(this.postId).subscribe(
            res => {
               this.comments = res;
            },
            err => {
               alert("Error occured while refreshing comments");
            }
         )
      } else {
         alert("Post id not set");
      }

   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}