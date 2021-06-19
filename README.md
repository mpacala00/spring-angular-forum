# Forum app
This is a full-stack forum application made using Spring Boot + Angular using PostgreSQL database. Its main features are JWT implementation, role-based security, conditional component visibilty and some methods restricted to owner of an entity.

> Note: this is not the final version of the project, it's still missing
> important stuff like pagination and sorting.

## Running

    cd forum-api
    mvn spring-boot:run
    
    cd forum-ng
    npm install
    ng serve


## Domain model
 - **One** user can have **many** posts and **many** comments
 - **Many** users can follow **many** categories
 -  **Many** posts belong to **one** category
 -  **Many** comments belong to **one** post

Categories group posts and posts group comments.
Any user is allowed to create posts and comments after registration, but creating new categories is reserved for >= moderators

## Roles in the app
Each role has its own array of strings containing authorities, which are used in preauthorizing secured methods in controllers or in displaying additional elements in Angular client, e.g. button for accessing admin panel.

Roles higher in hierarchy also contain authorities from previous ones.

| Role      | Authorities                             |
|-----------|-----------------------------------------|
| USER      | content:view                            |
| MODERATOR | user:block, content:delete, content:add |
| ADMIN     | user:delete, user:update                |


**content:view** allows for viewing other users' profiles, **content:add** allows for adding additional categories, rest is self-explanatory

## JWT implementation
Example token looks like this:

    {
	  "iss": "mpacala00",
	  "iat": 1623945965,
	  "exp": 1624032365,
	  "username": "admin",
	  "authorities": [
	    "content:view",
	    "user:block",
	    "content:delete",
	    "content:add",
	    "user:delete",
	    "user:update"
	  ]
	}
Token lasts exactly for one day, which can be changed in **application.yml**.
Username passed in the token is helpful in determining if user is the owner of content (post or comment).
Authorities are used in conditional component visibilty.

Token is decoded on the front-end using [@auth0/angular-jwt](https://github.com/auth0/angular2-jwt) package and its stored in cookies using [ngx-cookie-service](https://www.npmjs.com/package/ngx-cookie-service)

## Conditional component visibility
Component visibility is determined in 2 ways: either by using ***hasAuthority** directive or by checking **isOwner** boolean declared in component

	<button  mat-raised-button *hasAuthority="'user:delete'" color="accent">Admin</button>

*hasAuthority directive is checking if the token has required authority.

Implementation:
		
	@Directive({
	selector:  '[hasAuthority]'
	})

	export  class  HasAuthorityDirective  implements  OnInit {
	
		public  authority: string;

		@Input()
		set hasAuthority(authority: string) {
			this.authority = authority;
		}

		constructor(private  viewContainerRef: ViewContainerRef,
		private  templateRef: TemplateRef<any>,
		private  authService: AuthService) { }

		ngOnInit(): void {

			let  userAuthorities = this.authService.getUserAuthorities();

			if (!userAuthorities) {
				this.viewContainerRef.clear();
				return;
			}

			if (userAuthorities.includes(this.authority)) {
				this.viewContainerRef.createEmbeddedView(this.templateRef);
			}
		}

	}

Checking for ownership of entity in Angular client:

**Template:**

	<div class="btn-div" *ngIf="checkIfOwner(c.creator)">
**Component:**

	private checkIfOwner(usernameToCheck: string): boolean {
		return this.authService.checkIfEntityIsOwned(usernameToCheck);
	}
**checkIfOwner()** function sets the property by calling authService which checks if passed in string matches username in the token.
