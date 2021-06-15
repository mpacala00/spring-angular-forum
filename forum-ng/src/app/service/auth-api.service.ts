import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginModel } from '../model/login-model';
import { TokenResponse } from '../model/token-response';
import { RegisterModel } from '../model/register-model';

import { environment } from '../../environments/environment';

@Injectable({
   providedIn: 'root'
})
export class AuthApiService {

   private REGISTER_URL = `${environment.BASE_URL_PUBLIC}/user/register`;
   private LOGIN_URL = `${environment.BASE_URL_PUBLIC}/user/login`;

   constructor(private http: HttpClient) { }

   public postLogin(loginModel: LoginModel): Observable<any> {
      return this.http.post<TokenResponse>(this.LOGIN_URL, loginModel);
   }

   public postRegister(registerModel: RegisterModel) {
      return this.http.post<TokenResponse>(this.REGISTER_URL, registerModel);
   }
}
