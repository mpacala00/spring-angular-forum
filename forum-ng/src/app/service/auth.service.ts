import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { JwtHelperService } from "@auth0/angular-jwt";

@Injectable({
   providedIn: 'root'
})

export class AuthService {

   private decodedToken;
   private helper = new JwtHelperService();

   constructor(private cookieService: CookieService) { }

   public login(token: string) {
      this.logout();
      this.setToken(token);
   }

   public logout() {
      if (this.cookieService.check('token')) {
         this.cookieService.delete('token');
         this.decodedToken = null;
      }
   }

   public setToken(token: string): void {
      this.cookieService.set('token', token);
      this.decodeToken(token);
   }

   //in app.component, calls this function on app start
   public retrieveTokenFromCookies() {
      if (this.cookieService.check('token')) {
         let token = this.cookieService.get('token');

         if (this.helper.isTokenExpired(token)) {
            this.cookieService.delete('token');
            return;
         }
         this.setToken(token);
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
      this.retrieveTokenFromCookies();
      let username = this.decodedToken.username;
      return username ? username : null;
   }

   decodeToken(token: string): void {
      this.decodedToken = this.helper.decodeToken(token);
   }

   public isTokenSet(): boolean {
      return this.cookieService.check('token');
   }
}
