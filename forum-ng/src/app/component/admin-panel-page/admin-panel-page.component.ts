import { Component, OnInit } from '@angular/core';
import { MatLegacyDialog as MatDialog } from '@angular/material/legacy-dialog';
import { UserModel } from 'src/app/model/user-model';
import { UserApiService } from 'src/app/service/user-api.service';
import { SubSink } from 'subsink';
import { ConfirmationDialogModel, ConfirmationDialogComponent } from '../shared/confirmation-dialog/confirmation-dialog.component';

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
   roles: string[];

   constructor(private userApiService: UserApiService,
      public dialog: MatDialog,) { }

   ngOnInit(): void {
      this.getUserRoles();
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

   public getUserRoles() {
      this.subs.sink = this.userApiService.getUserRoles().subscribe(
         res => {
            this.roles = res;
         },
         err => {
            alert("An error occured while fetching roles");
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

   public onBlockUser(user: any) {
      this.subs.sink = this.userApiService.blockUser(user.id).subscribe(
         res => {
            user.notLocked = !user.notLocked;
         },
         err => {
            alert("An error occured while blocking role");
         }
      )
   }

   public onDeleteUser(userId: number) {
      this.subs.sink = this.userApiService.deleteUser(userId).subscribe(
         res => {
            this.getUsers();
         },
         err => {
            alert("An error occured while deleting user");
         }
      )
   }

   public openBlockDialog(user: any) {

      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
         width: '400px',
         data: new ConfirmationDialogModel(`Block/Unblock user ${user.username}`, 'Are you sure you want to continue?')
      });


      dialogRef.afterClosed().subscribe(dialogResult => {

         if (dialogResult == true) {
            this.onBlockUser(user);
         }
      });
   }

   public openDeleteDialog(user: any) {

      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
         width: '400px',
         data: new ConfirmationDialogModel(`Delete user ${user.username}`, 'Are you sure you want to continue?')
      });


      dialogRef.afterClosed().subscribe(dialogResult => {

         if (dialogResult == true) {
            this.onDeleteUser(user.id);
         }
      });
   }

}
