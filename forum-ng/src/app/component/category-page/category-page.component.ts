import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/service/api.service';
import { CategoryModel } from '../../model/category-model';

@Component({
   selector: 'app-category-page',
   templateUrl: './category-page.component.html',
   styleUrls: ['./category-page.component.scss']
})
export class CategoryPageComponent implements OnInit {

   categories: CategoryModel[] = [];

   constructor(private apiService: ApiService, private router: Router) { }

   ngOnInit(): void {
      this.getAllCategories();
   }

   private getAllCategories() {
      this.apiService.getAllCategories().subscribe(
         res => {
            this.categories = res;
         },
         err => {
            alert('Some error occured while fetching categories');
         }
      )
   }

   public navigateToPost(categoryId: number, category: CategoryModel) {

      this.router.navigateByUrl('/categories/' + categoryId, { state: category });
   }

}
