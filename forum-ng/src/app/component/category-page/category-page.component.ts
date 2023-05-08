import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CategoryApiService } from 'src/app/service/category-api.service';
import { SubSink } from 'subsink';
import { CategoryModel } from '../../model/category-model';
import { NewCategoryDialogComponent } from '../shared/new-category-dialog/new-category-dialog.component';

@Component({
   selector: 'app-category-page',
   templateUrl: './category-page.component.html',
   styleUrls: ['./category-page.component.scss']
})
export class CategoryPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   categories: CategoryModel[] = [];

   constructor(private categoryApiService: CategoryApiService,
      private router: Router,
      public dialog: MatDialog,) { }

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

   public openAddCategoryDialog() {
      const dialogRef = this.dialog.open(NewCategoryDialogComponent, {
         width: '450px',
         // to pass data to the dialog:
         data: { category: { name: '', description: '' }, dialogTitle: 'Add new category' }
      });

      dialogRef.afterClosed().subscribe(result => {
         if (result != null) {
            let newCat = result.category;
            this.onCategoryPost(newCat);
         }
      });
   }

   public onCategoryPost(category: CategoryModel) {
      console.log(category);
      this.subs.sink = this.categoryApiService.postCategory(category).subscribe(
         res => {
            this.getAllCategories();
         },
         err => {
            alert("An error occured during category posting");
         }
      )
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
