import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { PostModel } from '../../model/post-model';
import { ApiService } from '../../service/api.service';

@Component({
   selector: 'app-post-container',
   templateUrl: './post-container.component.html',
   styleUrls: ['./post-container.component.scss']
})
export class PostContainerComponent implements OnInit {

   constructor(private apiService: ApiService, private http: HttpClient) { }

   ngOnInit(): void {
      this.getAllPosts();
   }

   posts: PostModel[] = [];

   getAllPosts() {
      this.apiService.getAllPosts().subscribe(
         res => { this.posts = res },
         err => { alert("An Error occured while fetching posts") }
      );
   }

}
