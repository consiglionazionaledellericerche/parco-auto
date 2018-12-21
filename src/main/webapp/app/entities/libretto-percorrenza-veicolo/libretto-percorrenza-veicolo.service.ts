import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILibrettoPercorrenzaVeicolo } from 'app/shared/model/libretto-percorrenza-veicolo.model';

type EntityResponseType = HttpResponse<ILibrettoPercorrenzaVeicolo>;
type EntityArrayResponseType = HttpResponse<ILibrettoPercorrenzaVeicolo[]>;

@Injectable({ providedIn: 'root' })
export class LibrettoPercorrenzaVeicoloService {
    private resourceUrl = SERVER_API_URL + 'api/libretto-percorrenza-veicolos';

    constructor(private http: HttpClient) {}

    create(librettoPercorrenzaVeicolo: ILibrettoPercorrenzaVeicolo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(librettoPercorrenzaVeicolo);
        return this.http
            .post<ILibrettoPercorrenzaVeicolo>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(librettoPercorrenzaVeicolo: ILibrettoPercorrenzaVeicolo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(librettoPercorrenzaVeicolo);
        return this.http
            .put<ILibrettoPercorrenzaVeicolo>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ILibrettoPercorrenzaVeicolo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILibrettoPercorrenzaVeicolo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(librettoPercorrenzaVeicolo: ILibrettoPercorrenzaVeicolo): ILibrettoPercorrenzaVeicolo {
        const copy: ILibrettoPercorrenzaVeicolo = Object.assign({}, librettoPercorrenzaVeicolo, {
            data:
                librettoPercorrenzaVeicolo.data != null && librettoPercorrenzaVeicolo.data.isValid()
                    ? librettoPercorrenzaVeicolo.data.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.data = res.body.data != null ? moment(res.body.data) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((librettoPercorrenzaVeicolo: ILibrettoPercorrenzaVeicolo) => {
            librettoPercorrenzaVeicolo.data = librettoPercorrenzaVeicolo.data != null ? moment(librettoPercorrenzaVeicolo.data) : null;
        });
        return res;
    }
}
