import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CategoryApiService } from 'src/app/service/category-api.service';
import { SubSink } from 'subsink';
import { CategoryModel } from '../../model/category-model';

@Component({
   selector: 'app-category-page',
   templateUrl: './category-page.component.html',
   styleUrls: ['./category-page.component.scss']
})
export class CategoryPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   categories: CategoryModel[] = [];

   constructor(private categoryApiService: CategoryApiService, private router: Router) { }

   ngOnInit(): void {
      this.getAllCategories();
   }

   private getAllCategories() {
      this.subs.sink = this.categoryApiService.getAllCategories().subscribe(
         res => {
            this.categories = res;
         },
         err => {
            alert('Some error occured while fetching categories');
         }
      );

   }

   //todo use route ID instead of sending state, it causes problems upon refreshing on child route
   public navigateToCategory(categoryId: number, category: CategoryModel) {
      this.router.navigateByUrl('/categories/' + categoryId, { state: category });
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
