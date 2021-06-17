import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from '../service/auth.service';

@Injectable({
   providedIn: 'root'
})
export class AdminGuard implements CanActivate {

   constructor(private authService: AuthService) { }

   canActivate(
      route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      return this.isAdmin();
   }

   private isAdmin(): boolean {
      let authorities = this.authService.getUserAuthorities();
      if (!authorities) {
         return false;
      }

      if (authorities.includes(environment.userDeleteAuthority)) {
         return true;
      }

      return false;
   }

}
