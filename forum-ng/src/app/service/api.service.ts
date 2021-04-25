import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Post } from '../model/post';
import { Observable } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
   providedIn: 'root'
})
export class ApiService {

   private BASE_URL = "http://localhost:8080/public";
   private BASE_URL_NO_API = "http://localhost:8080";

   //@RequestBody UserRegistration = { username, password, passwordConfirm}
   private POST_REGISTER = `${this.BASE_URL}\\register`;
   private GET_POSTS = `${this.BASE_URL}\\posts`;
   private POST_POST_URL = `${this.BASE_URL_NO_API}\\post`;

   //send a post request with username and password
   private OAUTH_TOKEN = `${this.BASE_URL_NO_API}\\oauth\\token`;

   //private AUTH_HEADER_CONTENT = "Bearer: " + this.localStorage.retrieve('token');

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   getAllPosts() {
      return this.http.get<Post[]>(this.GET_POSTS);
   }

   private headersObj = new HttpHeaders({
      'Authorization': "Bearer " + this.cookieService.get('token'),
      'Access-Control-Allow-Origin': 'http://localhost:8080',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
      'Access-Control-Allow-Headers': 'X-Requested-With,content-type:application/json',
      'Access-Control-Allow-Credentials': 'true'
   });

   private httpOptions = {
      headers: this.headersObj
   };

   createNewPost(post: Post): Observable<any> {

      // headers.append("Authorization", "Bearer " + this.localStorage.retrieve('token'));
      // headers.append('Access-Control-Allow-Origin', 'http://localhost:8080');
      // headers.append('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
      // headers.append('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
      // headers.append('Access-Control-Allow-Credentials', 'true');

      console.log("debug: createNewPost in api.service; token: " + this.cookieService.get('token'));
      return this.http.post<Post>(this.POST_POST_URL, post, this.httpOptions);
   }

}
