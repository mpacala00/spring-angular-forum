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
      return this.http.get<CategoryModel[]>(`${environment.BASE_URL_PUBLIC}/categories`);
   }

   getCategoryById(categoryId: number) {
      return this.http.get<CategoryModel>(`${environment.BASE_URL_PUBLIC}/category/${categoryId}`);
   }

   getCategoryByIdSecured(cateogryId: number) {
      return this.http.get<CategoryModel>(`${environment.BASE_URL}/category/${cateogryId}`);
   }

   followCategory(cateogryId: number) {
      return this.http.get<any>(`${environment.BASE_URL}/category/${cateogryId}/follow`);
   }

   unfollowCategory(cateogryId: number) {
      return this.http.get<any>(`${environment.BASE_URL}/category/${cateogryId}/unfollow`);
   }
}
