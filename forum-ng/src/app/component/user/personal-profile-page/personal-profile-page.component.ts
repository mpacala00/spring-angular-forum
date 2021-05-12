import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ApiService } from 'src/app/service/api.service';
import { UserModel } from 'src/app/model/user-model';
import { SubSink } from 'subsink';
import { PostModel } from 'src/app/model/post-model';

@Component({
   selector: 'app-personal-profile-page',
   templateUrl: './personal-profile-page.component.html',
   styleUrls: ['./personal-profile-page.component.scss']
})
export class PersonalProfilePageComponent implements OnInit, OnDestroy {

   private subs = new SubSink();

   user: UserModel;
   userPosts: PostModel[];

   constructor(private apiService: ApiService) { }
   
   ngOnInit(): void {
      this.getCurrentUser();
   }

   public getCurrentUser() {
      this.subs.sink = this.apiService.getCurrentUser().subscribe(
         res => {
            this.user = res;
         },
         err => {
            alert("Could not retrieve current user");
         }
      )
   }

   public getUserPosts() {
      console.log("helo?");
      this.apiService.getPostsByUsername(this.user.username).subscribe(
         res => {
            this.userPosts = res;
         },
         err => {
            alert("An error occured while fetching user posts");
         }
      )
   }

   ngOnDestroy(): void {
      this.subs.unsubscribe();
   }

}
