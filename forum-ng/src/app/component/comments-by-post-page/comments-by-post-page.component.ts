import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Post } from 'src/app/model/post';
import { ApiService } from 'src/app/service/api.service';

@Component({
   selector: 'app-comments-by-post-page',
   templateUrl: './comments-by-post-page.component.html',
   styleUrls: ['./comments-by-post-page.component.scss']
})
export class CommentsByPostPageComponent implements OnInit {

   constructor(private route: ActivatedRoute, private apiService: ApiService) { }

   //implement PostCommentDTO on backend?
   private postId: number;
   public post: Post;
   public comments: Comment[];
   public commentForm: FormGroup;

   ngOnInit(): void {
      this.commentForm = new FormGroup({
         comment: new FormControl('', [Validators.required, Validators.minLength(1)])
      })
      //get id from current route
      this.route.params.subscribe(
         params => { this.postId = params['id']; }
      );

      this.getPostById(this.postId);
   }

   private getPostById(postId: number): void {
      this.apiService.getCommentsByPost(postId).subscribe(
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
         alert(this.commentForm.value.comment);
      }

   }


}

export interface Comment {
   id: number;
   postDate: string;
   body: string;
}
