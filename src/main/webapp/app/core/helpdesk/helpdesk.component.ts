import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { HelpDeskService } from './helpdesk.service';
import { IProblem } from './problem.model';
import { Principal } from '../auth/principal.service';
import { JhiDataUtils } from 'ng-jhipster';

@Component({
    selector: 'jhi-helpdesk',
    templateUrl: './helpdesk.component.html'
})
export class HelpDeskComponent implements OnInit {
    private _problem: IProblem;
    isSaving: boolean;

    constructor(
        private dataUtils: JhiDataUtils,
        private principal: Principal,
        private helpdeskService: HelpDeskService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ problem }) => {
            this.problem = problem;
        });
        this.principal.identity().then(account => {
            this._problem.firstName = account.firstName;
            this._problem.familyName = account.lastName;
            this._problem.login = account.login;
            this._problem.email = account.email;
            this._problem.confirmRequested = 'n';
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.problem.idSegnalazione !== undefined) {
            this.subscribeToSaveResponse(this.helpdeskService.update(this.problem));
        } else {
            this.subscribeToSaveResponse(this.helpdeskService.create(this.problem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProblem>>) {
        result.subscribe((res: HttpResponse<IProblem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get problem() {
        return this._problem;
    }

    set problem(problem: IProblem) {
        this._problem = problem;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }
}
