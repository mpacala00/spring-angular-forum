import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../service/api.service';
import { Post } from '../../model/post';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthService } from 'src/app/service/auth.service';

@Component({
   selector: 'app-new-post',
   templateUrl: './new-post.component.html',
   styleUrls: ['./new-post.component.scss']
})
export class NewPostComponent implements OnInit {

   constructor(private service: ApiService, private router: Router, private authService: AuthService) { }

   public postForm: FormGroup;

   ngOnInit(): void {
      this.postForm = new FormGroup({
         creator: new FormControl(''),
         title: new FormControl(''),
         body: new FormControl('')
      })
   }

   // post: Post = {
   //    creator: this.localStorage.retrieve('username'),
   //    title: '',
   //    body: '',
   //    date: ''
   // };

   createNewPost() {
      this.postForm.controls.creator.setValue(this.authService.getUsername());

      //this.post.date = Date.now().toString();
      //console.log(this.post.date);

      this.service.createNewPost(this.postForm.value).subscribe(
         res => { console.log("success"); window.location.href = "/"; },
         err => { console.log("error: ", err); }
      );

      //this.router.navigate(["/"]);
   }

   onUpdateLog() {
      this.postForm.controls.creator.setValue(this.authService.getUsername());
      console.log("postForm: ", this.postForm.value);
   }

}
