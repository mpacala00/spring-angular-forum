import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommentModel } from 'src/app/model/comment-model';
import { PostModel } from 'src/app/model/post-model';
import { PostApiService } from 'src/app/service/post-api.service';
import { CommentApiService } from 'src/app/service/comment-api.service';
import { SubSink } from 'subsink';
import { NewPostDialogComponent } from '../shared/new-post-dialog/new-post-dialog.component';
import { ConfirmationDialogModel, ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';
import { AuthService } from 'src/app/service/auth.service';
import { EditCommentEvent } from 'src/app/event/edit-comment-event';

@Component({
   selector: 'app-comments-by-post-page',
   templateUrl: './comments-by-post-page.component.html',
   styleUrls: ['./comments-by-post-page.component.scss']
})
export class CommentsByPostPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   private postId: number;
   private categoryId: number;

   public post: PostModel;
   public comments: CommentModel[];
   public commentForm: UntypedFormGroup;

   public edditedComments = [];

   public isOwnerOfPost = false;
   public commentToReplyTo: CommentModel;

   constructor(
      private route: ActivatedRoute,
      private router: Router,
      private commentApiService: CommentApiService,
      private postApiService: PostApiService,
      private authService: AuthService,
      public dialog: MatDialog,
      public changeDetection: ChangeDetectorRef) { }

   ngOnInit(): void {
      this.commentForm = new UntypedFormGroup({
         body: new UntypedFormControl('', [Validators.required, Validators.minLength(1)])
      })
      //get id from current route
      this.route.params.subscribe(
         params => { this.postId = params['id']}
      );

      this.route.parent.params.subscribe(
         params => { this.categoryId = params['id']}
      );

      this.getPostById(this.postId);
   }

   private getPostById(postId: number): void {
      // this.subs.sink = this.postApiService.getPostComments(postId).subscribe(
      //    res => {
      //       this.post = res;

      //       //checking ownership
      //       if (this.authService.isTokenSet()) {
      //          this.isOwnerOfPost = this.checkIfOwner(this.post.creator);
      //       }

      //       this.refreshComments();
      //    },
      //    err => {
      //       alert('An error occured while fetching posts');
      //       this.router.navigate(['../../'], { relativeTo: this.route });
      //    }
      // )
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
      this.subs.sink = this.postApiService.putPost(this.categoryId, this.postId, post).subscribe(
         res => {
            this.refreshComments();
         },
         err => {
            alert("An error occured while updating post");
            console.error(err);
         }
      )
   }

   public openDeleteCommentDialog(commentId: number) {
      console.log("hello?")
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

   public deletePost() {
      this.subs.sink = this.postApiService.deletePost(this.categoryId, this.postId).subscribe(
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
      if (!this.commentForm.valid) {
         return;
      }

      if (!this.commentToReplyTo) {
         this.subs.sink = this.commentApiService.postComment(this.postId, this.commentForm.value).subscribe(
            res => {
               this.refreshComments();
            },
            err => {
               alert("An error occured while posting a comment");
               console.error(err);
            }
         );
         
      } else {
         this.subs.sink = this.commentApiService.replyToComment(this.postId, this.commentToReplyTo.id, this.commentForm.value).subscribe(
            res => {
               this.cancelReply();
               this.refreshComments();
            },
            err => {
               alert("An error occured while posting a comment");
               console.error(err);
            }
         );
      }
      this.commentForm.reset();
   }

   public updateComment(editCommentEvent: EditCommentEvent) {
      if (editCommentEvent?.body == '') {
         return;
      }
      
      editCommentEvent.comment.body = editCommentEvent.body;

      this.subs.sink = this.commentApiService.putComment(editCommentEvent.comment).subscribe(
         (res) => {
            this.refreshComments();
         },
         (err) => {
            alert('An error occured');
            console.error(err);
         }
      );
   }

   replyToComment(comment: CommentModel) {
      if (!comment.id || !comment.creator) {
         return;
      }

      this.commentToReplyTo = comment;
   }

   cancelReply() {
      this.commentToReplyTo = undefined;
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

   public rateComment(commentId: number, isLike: boolean) {
      if (!commentId) {
         return;
      }

      this.subs.sink = this.commentApiService.likeComment(commentId, isLike).subscribe(
         res => {
            //todo use ChangeDetectionRef to update liked comment with backend res
            this.refreshComments();
         },
         err => {
            alert("Error occured while rating the comment.");
         }
      )
   }

   private replaceCommentInCommentsArray(commentId: number, newComment: CommentModel) {
      this.comments.map(comment => 
            comment.id === commentId ? newComment : comment
      );
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}