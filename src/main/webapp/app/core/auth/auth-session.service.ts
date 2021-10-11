import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL, KEYCLOAKLOGOUTURL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class AuthServerProvider {
    constructor(private http: HttpClient) {}

    logout(): Observable<any> {
        // logout from the server
        return this.http.post(SERVER_API_URL + 'api/logout', {}, { observe: 'response' }).pipe(response => {
            // logout from keycloak
            const { protocol, host } = window.location;
            const redirectUri = `${protocol}//${host}/#/`;
            const logoutUrl = `${KEYCLOAKLOGOUTURL}?redirect_uri=${encodeURIComponent(redirectUri)}`;
            setTimeout(() => {
                window.location.href = logoutUrl;
            }, 0);
            return response;
        });
    }
}
