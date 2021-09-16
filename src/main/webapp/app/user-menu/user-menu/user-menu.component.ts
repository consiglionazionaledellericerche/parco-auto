import { Component, Input, OnInit } from '@angular/core';
import { Principal, LoginService, Account } from 'app/core';
import { Router } from '@angular/router';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCamera } from '@fortawesome/free-solid-svg-icons';
library.add(faCamera);

@Component({
    selector: 'jhi-user-menu',
    templateUrl: './user-menu.component.html',
    styleUrls: ['./user-menu.css']
})
export class UsermenuComponent implements OnInit {
    @Input() account: Account;
    avatarImageUrl = '';
    manageAccountUrl = 'https://utenti.cnr.it';
    editAvatarImageUrl = 'https://intranet.cnr.it/servizi/people/persona/persone/#/profile';
    gravatarFallback = 'robohash';
    gravatarBackgroundColor = '#a1dbb2';

    constructor(private loginService: LoginService, private router: Router, private principal: Principal) {}

    ngOnInit(): void {
        this.principal.identity().then(account => this.account = account);
    }

    logout() {
        this.loginService.logout();
        this.router.navigate(['']);
    }

    getLogin = () => (this.account && this.account.login) || '';
    getEmail = () => (this.account && this.account.email) || '';
    getAvatarImageUrl = () => (this.account && this.account.login && `https://publications.cnr.it/api/v1/author/image/${this.account.login}`) || '';
    getName = () => `${this.account && this.account.firstName} ${this.account && this.account.lastName}`;
}
