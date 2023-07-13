import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { CategoryModel } from '../model/category-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

@Injectable({
   providedIn: 'root'
})
export class CategoryApiService {

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   //todo change cat entity to include description
   getAllCategories() {
      return this.http.get<CategoryModel[]>(`${environment.BASE_URL}/categories`);
   }

   getCategoryById(categoryId: number) {
      return this.http.get<CategoryModel>(`${environment.BASE_URL}/categories/${categoryId}`);
   }

   postCategory(category: CategoryModel) {
      return this.http.post<CategoryModel>(`${environment.BASE_URL}/categories`, category);
   }

   //todo accept response entity as return type
   followCategory(cateogryId: number) {
      return this.http.put<any>(`${environment.BASE_URL}/categories/${cateogryId}/follow`, null);
   }

   unfollowCategory(cateogryId: number) {
      return this.http.put<any>(`${environment.BASE_URL}/categories/${cateogryId}/unfollow`, null);
   }
}
