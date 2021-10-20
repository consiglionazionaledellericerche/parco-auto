import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { Problem, IProblem } from './problem.model';
import { HelpDeskService } from './helpdesk.service';
import { HelpDeskComponent } from './helpdesk.component';

@Injectable({ providedIn: 'root' })
export class HelpDeskResolve implements Resolve<IProblem> {
    constructor(private service: HelpDeskService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return of(new Problem());
    }
}

export const helpDeskRoute: Routes = [
    {
        path: 'helpdesk/new',
        component: HelpDeskComponent,
        resolve: {
            problem: HelpDeskResolve
        },
        data: {
            authorities: [],
            pageTitle: 'parcoautoApp.helpdesk.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'helpdesk/:id/edit',
        component: HelpDeskComponent,
        resolve: {
            problem: HelpDeskResolve
        },
        data: {
            authorities: [],
            pageTitle: 'parcoautoApp.helpdesk.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
