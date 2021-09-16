import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsermenuComponent } from './user-menu/user-menu.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { GravatarModule } from 'ngx-gravatar';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
    imports: [CommonModule, GravatarModule, NgbModule, FontAwesomeModule],
    declarations: [UsermenuComponent],
    exports: [UsermenuComponent, GravatarModule, NgbModule, FontAwesomeModule]
})
export class UserMenuModule {}
