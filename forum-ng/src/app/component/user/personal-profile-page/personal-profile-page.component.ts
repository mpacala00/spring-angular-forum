import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
   selector: 'app-personal-profile-page',
   templateUrl: './personal-profile-page.component.html',
   styleUrls: ['./personal-profile-page.component.scss']
})
export class PersonalProfilePageComponent implements OnInit {

   constructor() { }

   form: FormGroup;

   ngOnInit(): void {
      this.form = new FormGroup({
         username: new FormControl(''),
      })
   }

}
