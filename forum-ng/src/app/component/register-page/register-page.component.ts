import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
   public errorMessage: string;

   constructor(private authService: AuthService, private router: Router) { }

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
      if(this.registerForm.controls['username'].value.indexOf(' ') >= 0) {
         this.errorMessage = "Username cannot contain spaces";
         return;
      }
      this.subs.sink = this.authService.register(this.registerForm.value).subscribe(
         res => {
            let token = res.token;
            //basically log in
            this.authService.setToken(token);
            this.router.navigateByUrl('/');
         },
         err => {
            console.log(err);
         }
      );

   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
