import { Component, OnInit } from '@angular/core';
import { UserModel } from 'src/app/model/user-model';
import { UserApiService } from 'src/app/service/user-api.service';
import { SubSink } from 'subsink';

@Component({
   selector: 'app-admin-panel-page',
   templateUrl: './admin-panel-page.component.html',
   styleUrls: ['./admin-panel-page.component.scss']
})
export class AdminPanelPageComponent implements OnInit {

   private subs = new SubSink();

   public users: UserModel[];
   public errorMessage: string;

   displayedColumns: string[] = ['id', 'username', 'email', 'role', 'block', 'delete'];
   roles: string[] = ['ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER'];

   constructor(private userApiService: UserApiService) { }

   ngOnInit(): void {
      this.getUsers();
   }

   public getUsers() {
      this.subs.sink = this.userApiService.getAllUsers().subscribe(
         res => {
            this.users = res
         },
         err => {
            this.errorMessage = err.error.message;
         }
      )
   }

   public onRoleChange($event, user: any) {
      let role = $event.value;
      this.subs.sink = this.userApiService.patchRole(user.id, role).subscribe(
         res => {
            //udpate view
            user.role = role;
         },
         err => {
            alert("An error occured while updating role");
         }
      );

   }

}
