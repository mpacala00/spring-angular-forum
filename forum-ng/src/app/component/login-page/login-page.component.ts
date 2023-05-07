import { Component, OnDestroy, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { LoginModel } from '../../model/login-model';
import { AuthService } from '../../service/auth.service';
import { AuthApiService } from '../../service/auth-api.service';
import { SubSink } from 'subsink';


@Component({
   selector: 'app-login-page',
   templateUrl: './login-page.component.html',
   styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   model: LoginModel;
   loginForm: UntypedFormGroup;
   loginFailedMessage: string;

   constructor(private authService: AuthService,
      private authApiService: AuthApiService) { }


   ngOnInit(): void {
      this.loginForm = new UntypedFormGroup({
         username: new UntypedFormControl('', Validators.required),
         password: new UntypedFormControl('', Validators.required)
      });

   }


   login() {
      this.loginFailedMessage = null;
      if (this.loginForm.valid) {

         this.authApiService.postLogin(this.loginForm.value).subscribe(
            // res => { this.localStorage.store('token', res) },
            // err => { console.log("error on login component while login"); }
            res => {
               this.authService.login(res.token);

               //this redirect works but causes the whole page to reload
               //needed to refresh nav component to show logout button etc.
               //not the most elegant solution
               window.location.href = "/";
            },
            err => {
               this.loginFailedMessage = err.error.message;
            }
         );
      }

   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }


}

