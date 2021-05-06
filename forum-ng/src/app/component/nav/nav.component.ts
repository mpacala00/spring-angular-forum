import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthService } from '../../service/auth.service';
import { CookieService } from 'ngx-cookie-service';

@Component({
   selector: 'app-nav',
   templateUrl: './nav.component.html',
   styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {

   username: string;

   isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
      .pipe(
         map(result => result.matches),
         shareReplay()
      );

   constructor(private breakpointObserver: BreakpointObserver,
      private authService: AuthService) {
   }
   ngOnInit(): void {
      if (this.authService.isTokenSet()) {
         this.username = this.authService.getUsername();
      }

   }

   logout() {
      this.authService.logout();
      this.username = null;
      window.location.href = "/";
   }
}
