import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginModel } from '../../model/login-model';
import { AuthService } from '../../service/auth.service';
import { CookieService } from 'ngx-cookie-service';
import { SubSink } from 'subsink';


@Component({
   selector: 'app-login-page',
   templateUrl: './login-page.component.html',
   styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   model: LoginModel;
   loginForm: FormGroup;

   constructor(private authService: AuthService, private cookieService: CookieService) {

   }
   

   ngOnInit(): void {
      this.loginForm = new FormGroup({
         username: new FormControl('', Validators.required),
         password: new FormControl('', Validators.required)
      });

   }

   // setSession(authResult) {
   //    localStorage.setItem('token', authResult);
   // }

   login() {

      this.authService.login(this.loginForm.value).subscribe(
         // res => { this.localStorage.store('token', res) },
         // err => { console.log("error on login component while login"); }
         res => {
            console.log(res);
            //setting the token also sets the username in auth service
            this.authService.setToken(res.token);

            //this redirect works but causes the whole page to reload
            //needed to refresh nav component to show logout button etc.
            //not the most elegant solution
            window.location.href = "/";
         },
         err => {
            console.log(err);
         }
      )

      //debug
      //console.log(localStorage.getItem('token'));
   }

   getErrorMessage() {
      if (this.loginForm.get('username').hasError('required') || this.loginForm.get('password').hasError('required')) {
         return 'You must enter a value';
      }
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }


}

