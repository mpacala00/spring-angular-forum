import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryModel } from 'src/app/model/category-model';
import { Post } from 'src/app/model/post';
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
   posts: Post[];

   ngOnInit(): void {
      //this will not work upon site refreshing
      this.category = history.state;
      this.getPostsByCategory(history.state.id);
   }

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
