import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IProblem } from './problem.model';

type EntityResponseType = HttpResponse<IProblem>;

@Injectable({ providedIn: 'root' })
export class HelpDeskService {
    private resourceUrl = SERVER_API_URL + 'api/oil/problem';

    constructor(private http: HttpClient) {}

    create(problem: IProblem, allegatoContentType: string, allegatoFileName: string): Observable<EntityResponseType> {
        return this.http.post<IProblem>(this.resourceUrl, problem, {
            observe: 'response',
            params: {
                allegatoContentType: allegatoContentType,
                allegatoFileName: allegatoFileName
            }
        });
    }

    update(problem: IProblem, allegatoContentType: string, allegatoFileName: string): Observable<EntityResponseType> {
        return this.http.put<IProblem>(this.resourceUrl, problem, {
            observe: 'response',
            params: {
                allegatoContentType: allegatoContentType,
                allegatoFileName: allegatoFileName
            }
        });
    }
}
