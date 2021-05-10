import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryModel } from 'src/app/model/category-model';
import { PostModel } from 'src/app/model/post-model';
import { ApiService } from 'src/app/service/api.service';


@Component({
   selector: 'app-posts-by-category-page',
   templateUrl: './posts-by-category-page.component.html',
   styleUrls: ['./posts-by-category-page.component.scss']
})
export class PostsByCategoryPageComponent implements OnInit {

   constructor(private apiService: ApiService, private router: Router, private activatedRoute: ActivatedRoute) { }

   //todo replace category & model with a DTO
   category: CategoryModel;
   posts: PostModel[];
   categoryId: number;

   ngOnInit(): void {
      //this solution is probably not worth it to save 2 lines of json response

      //simple check if history containes cateogry object
      if (history.state.hasOwnProperty('name')) {
         this.category = history.state;
         this.getPostsByCategory(history.state.id);
      } else {
         this.activatedRoute.params.subscribe(
            params => { this.categoryId = params['id']; }
         );
         this.getCategory(this.categoryId);
      }

   }

   //will contain its posts
   getCategory(categoryId: number) {
      this.apiService.getCategoryById(categoryId).subscribe(
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
      this.apiService.getPostsByCategory(categoryId).subscribe(
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

}
