import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthApiService } from 'src/app/service/auth-api.service';
import { AuthService } from 'src/app/service/auth.service';
import { SubSink } from 'subsink';

@Component({
   selector: 'app-register-page',
   templateUrl: './register-page.component.html',
   styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   public registerForm: FormGroup;
   public registerFailedMessage: string;

   constructor(private authService: AuthService, private authApiService: AuthApiService, private router: Router) { }

   ngOnInit(): void {
      this.registerForm = new FormGroup({
         username: new FormControl('', [Validators.required]),
         password: new FormControl('', Validators.required),
         passwordConfirmation: new FormControl('', Validators.required),
         email: new FormControl('', Validators.compose(
            [Validators.required, Validators.email]
         ))
      })
   }

   public register() {
      this.registerFailedMessage = null;

      if (this.registerForm.valid) {
         this.subs.sink = this.authApiService.postRegister(this.registerForm.value).subscribe(
            res => {
               this.authService.login(res.token);
               window.location.href = '/';
            },
            err => {
               console.log(err);
               this.registerFailedMessage = err.error.message;
               console.log("registerFailedMessage: ", this.registerFailedMessage);
            }
         );
      }

   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
