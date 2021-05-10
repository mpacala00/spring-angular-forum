import { Component, OnDestroy, OnInit } from '@angular/core';
import { ApiService } from '../../service/api.service';
import { PostModel } from '../../model/post-model';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthService } from 'src/app/service/auth.service';
import { SubSink } from 'subsink';

@Component({
   selector: 'app-new-post',
   templateUrl: './new-post.component.html',
   styleUrls: ['./new-post.component.scss']
})
export class NewPostComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   public postForm: FormGroup;

   constructor(private service: ApiService, private router: Router, private authService: AuthService) { }

   ngOnInit(): void {
      this.postForm = new FormGroup({
         creator: new FormControl(''),
         title: new FormControl(''),
         body: new FormControl('')
      })
   }

   createNewPost() {
      this.postForm.controls.creator.setValue(this.authService.getUsername());

      //this.post.date = Date.now().toString();
      //console.log(this.post.date);

      this.subs.sink = this.service.createNewPost(this.postForm.value).subscribe(
         res => { console.log("success"); window.location.href = "/"; },
         err => { console.log("error: ", err); }
      );

      //this.router.navigate(["/"]);
   }

   onUpdateLog() {
      this.postForm.controls.creator.setValue(this.authService.getUsername());
      console.log("postForm: ", this.postForm.value);
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }


}
