import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommentModel } from 'src/app/model/comment-model';
import { Post } from 'src/app/model/post';
import { ApiService } from 'src/app/service/api.service';

@Component({
   selector: 'app-comments-by-post-page',
   templateUrl: './comments-by-post-page.component.html',
   styleUrls: ['./comments-by-post-page.component.scss']
})
export class CommentsByPostPageComponent implements OnInit {

   constructor(private route: ActivatedRoute, private apiService: ApiService) { }

   private postId: number;
   public post: Post;
   public comments: CommentModel[];
   public commentForm: FormGroup;

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
      this.apiService.getPostComments(postId).subscribe(
         res => {
            this.post = res;
            this.comments = this.post.comments;
         },
         err => {
            alert('An error occured while fetching posts');
         }
      )
   }

   public onCommentPost() {
      if (this.commentForm.valid) {
         this.apiService.postComment(this.postId, this.commentForm.value).subscribe(
            res => {
               this.refreshComments();
            },
            err => {
               alert("An error occured while posting a comment");
            }
         );
         this.commentForm.reset();
      }

   }

   public refreshComments() {
      if (this.postId) {
         this.apiService.getCommentsByPost(this.postId).subscribe(
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

}