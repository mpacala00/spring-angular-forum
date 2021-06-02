import { ViewContainerRef } from '@angular/core';
import { TemplateRef } from '@angular/core';
import { Directive, Input, OnDestroy, OnInit } from '@angular/core';
import { SubSink } from 'subsink';
import { AuthService } from '../service/auth.service';

@Directive({
   selector: '[hasAuthority]'
})

//how to use:
//<p *hasAuthority="'user:delete'">i can delete users</p>
export class HasAuthorityDirective implements OnInit, OnDestroy {

   private subs = new SubSink();

   public authority: string;

   @Input()
   set hasAuthority(authority: string) {
      this.authority = authority;
   }


   constructor(private viewContainerRef: ViewContainerRef,
      private templateRef: TemplateRef<any>,
      private authService: AuthService) { }

   ngOnInit(): void {
      //authorities of currently logged in user
      let userAuthorities = this.authService.getUserAuthorities();
      if (!userAuthorities) {
         this.viewContainerRef.clear();
         return;
      }
      if (userAuthorities.includes(this.authority)) {
         this.viewContainerRef.createEmbeddedView(this.templateRef);
      }
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
