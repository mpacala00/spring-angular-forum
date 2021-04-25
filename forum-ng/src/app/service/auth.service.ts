import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { LoginModel } from '../model/login-model';
import { LoginResponse } from '../model/login-response';
import { map } from 'rxjs/operators';
import jwt_decode from 'jwt-decode';

//TODO move all api links to enviroment.ts

@Injectable({
   providedIn: 'root'
})

export class AuthService {

   private loginUrl = "http://localhost:8080/public/users/login";
   public username: string;
   public decodedToken;

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   //todo use RequestBody instead of url params
   login(loginModel: LoginModel): Observable<any> {
      this.loginUrl = this.loginUrl + "?username=" + loginModel.username + "&password=" + loginModel.password;
      // this.decodeToken();
      return this.http.post<LoginResponse>(this.loginUrl, loginModel);
      //return this.http.post<LoginResponse>(this.loginUrl, loginModel);
   }

   public setToken(token: string): void {
      if (this.cookieService.check('token')) {
         this.cookieService.delete('token');
      }
      this.cookieService.set('token', token);
      this.decodeToken(token);
      this.setUsername(this.decodedToken.username);
   }

   public setUsername(username: string): void {
      this.username = username;
   }

   public getUsername(): string {
      return this.username;
   }

   logout(): void {
      this.cookieService.delete('token');
   }

   decodeToken(token: string): void {
      this.decodedToken = jwt_decode(token);
      console.log("decoded: ", this.decodedToken);
   }
}
