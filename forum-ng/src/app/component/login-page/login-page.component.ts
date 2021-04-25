import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginModel } from '../../model/login-model';
import { AuthService } from '../../service/auth.service';
import { CookieService } from 'ngx-cookie-service';


@Component({
   selector: 'app-login-page',
   templateUrl: './login-page.component.html',
   styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {

   constructor(private authService: AuthService, private cookieService: CookieService) {
      this.model = {
         username: '',
         password: ''
      };
   }

   model: LoginModel;
   loginForm: FormGroup;

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
      this.model.username = this.loginForm.get('username').value;
      this.model.password = this.loginForm.get('password').value;
      //post using provided credentials to login into backend
      console.log(this.model);
      this.authService.login(this.model).subscribe(
         // res => { this.localStorage.store('token', res) },
         // err => { console.log("error on login component while login"); }
         res => {
            console.log(res);
            this.authService.setToken(res.token);
            this.authService.setUsername(this.model.username);

            //this redirect works but causes the whole page to reload
            // window.location.href = "/";
         }
      )

      //debug
      //console.log(localStorage.getItem('token'));
   }

   logout() {
      this.authService.logout();
      window.location.href = "/";
   }

   getErrorMessage() {
      if (this.loginForm.get('username').hasError('required') || this.loginForm.get('password').hasError('required')) {
         return 'You must enter a value';
      }
   }


}

