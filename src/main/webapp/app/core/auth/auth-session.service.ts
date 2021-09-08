import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class AuthServerProvider {
    constructor(private http: HttpClient) {}

    logout(): Observable<any> {
        const { protocol, host } = window.location;
        const redirectUri = `${protocol}//${host}/#/`;
        const logoutUrl = `http://dockerwebtest02.si.cnr.it:8110/auth/realms/cnr/protocol/openid-connect/logout?redirect_uri=${encodeURIComponent(
            redirectUri
        )}`;
        // logout from keycloak
        window.location.href = logoutUrl;

        // logout from the server
        return this.http.post(SERVER_API_URL + 'api/logout', {}, { observe: 'response' }).pipe(
            map((response: HttpResponse<any>) => {
                // to get a new csrf token call the api
                this.http.get(SERVER_API_URL + 'api/account').subscribe();
                return response;
            })
        );
    }
}
