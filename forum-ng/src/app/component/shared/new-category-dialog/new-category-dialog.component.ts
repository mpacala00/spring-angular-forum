import { Inject } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CategoryModel } from '../../../model/category-model';

@Component({
   selector: 'app-new-category-dialog',
   templateUrl: './new-category-dialog.component.html',
   styleUrls: ['./new-category-dialog.component.scss']
})
export class NewCategoryDialogComponent {

   constructor(public dialogRef: MatDialogRef<NewCategoryDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: DialogCategoryData) { }

   onNoClick(): void {
      this.dialogRef.close();
   }

}

export interface DialogCategoryData {
   category: CategoryModel;
   dialogTitle: string;
}
