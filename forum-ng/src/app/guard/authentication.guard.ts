import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../service/auth.service';

@Injectable({
   providedIn: 'root'
})
export class AuthenticationGuard  {

   constructor(private authService: AuthService) {

   }

   canActivate(
      route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      return this.isUserLoggedIn();
   }

   //check for 'content:view' authority in the token
   private isUserLoggedIn(): boolean {
      let authorities = this.authService.getUserAuthorities();
      if (!authorities) {
         return false;
      }

      if (authorities.includes(environment.viewAuthority)) {
         return true;
      }

      alert('You need to be logged in to perform this action');
      return false;
   }

}
