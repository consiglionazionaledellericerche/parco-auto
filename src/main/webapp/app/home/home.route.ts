import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

import { HomeComponent } from './';

export const HOME_ROUTE: Route = {
    path: '',
    component: HomeComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'home.title'
    },
    canActivate: [UserRouteAccessService]
};
