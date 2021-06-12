import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { LoginModel } from '../model/login-model';
import { TokenResponse } from '../model/token-response';
import { RegisterModel } from '../model/register-model';
import { JwtHelperService } from "@auth0/angular-jwt";

import { environment } from 'src/environments/environment';

//TODO move all api links to enviroment.ts

@Injectable({
   providedIn: 'root'
})

export class AuthService {

   //@RequestBody UserRegistration = { username, password, passwordConfirm}
   private REGISTER_URL = `${environment.BASE_URL_PUBLIC}/user/register`;
   private LOGIN_URL = `${environment.BASE_URL_PUBLIC}/user/login`;

   private decodedToken;

   private helper = new JwtHelperService();

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   public login(loginModel: LoginModel): Observable<any> {
      return this.http.post<TokenResponse>(this.LOGIN_URL, loginModel);
   }

   //returns token
   public register(registerModel: RegisterModel) {
      return this.http.post<TokenResponse>(this.REGISTER_URL, registerModel);
   }

   public setToken(token: string): void {
      if (this.cookieService.check('token')) {
         this.cookieService.delete('token');
      }
      this.cookieService.set('token', token);
      this.decodeToken(token);
   }

   public retrieveTokenFromCookies() {
      if (this.cookieService.check('token')) {
         let token = this.cookieService.get('token');

         if (this.helper.isTokenExpired(token)) {
            this.cookieService.delete('token');
            return;
         }
         this.decodeToken(token);
      }
   }

   public getUserAuthorities(): string[] {
      this.retrieveTokenFromCookies();
      if (this.decodedToken != null) {
         return this.decodedToken.authorities;
      }
   }

   //for checking if logged-in user is the creator of post or comment
   public checkIfEntityIsOwned(checkedUsername: string): boolean {
      let loggedInUser = this.getUsername();
      if (!loggedInUser) {
         return false;
      }
      if (loggedInUser === checkedUsername) {
         return true;
      }

      return false;
   }

   public getUsername(): string {
      if (!this.cookieService.check('token')) {
         return null;
      }
      this.decodeToken(this.cookieService.get('token'));
      return this.decodedToken.username;
   }

   logout(): void {
      if (this.cookieService.check('token')) {
         this.cookieService.delete('token');
      }
   }

   decodeToken(token: string): void {
      this.decodedToken = this.helper.decodeToken(token);
   }

   public isTokenSet(): boolean {
      return this.cookieService.check('token');
   }
}
