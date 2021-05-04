import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { LoginModel } from '../model/login-model';
import { LoginResponse } from '../model/login-response';
import { RegisterModel } from '../model/register-model';

import jwt_decode from 'jwt-decode';
import { environment } from 'src/environments/environment';

//TODO move all api links to enviroment.ts

@Injectable({
   providedIn: 'root'
})

export class AuthService {

   //@RequestBody UserRegistration = { username, password, passwordConfirm}
   private REGISTER_URL = `${environment.BASE_URL_PUBLIC}/user/register`;
   private LOGIN_URL = `${environment.BASE_URL_PUBLIC}/user/login`;
   public username: string;
   public decodedToken;

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   public login(loginModel: LoginModel): Observable<any> {
      // this.decodeToken();
      return this.http.post<LoginResponse>(this.LOGIN_URL, loginModel);
      //return this.http.post<LoginResponse>(this.loginUrl, loginModel);
   }

   //returns token
   public register(registerModel: RegisterModel) {
      return this.http.post<string>(this.REGISTER_URL, registerModel);
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
