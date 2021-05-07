import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ApiService } from 'src/app/service/api.service';

@Component({
   selector: 'app-personal-profile-page',
   templateUrl: './personal-profile-page.component.html',
   styleUrls: ['./personal-profile-page.component.scss']
})
export class PersonalProfilePageComponent implements OnInit {

   constructor(private apiService: ApiService) { }

   user: any;

   ngOnInit(): void {
      this.getCurrentUser();
   }

   public getCurrentUser() {
      this.apiService.getCurrentUser().subscribe(
         res => {
            this.user = res;
         },
         err => {
            alert("Could not retrieve current user");
         }
      )
   }

}
