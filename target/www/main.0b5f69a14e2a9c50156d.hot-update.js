webpackHotUpdate("main",{

/***/ "./src/main/webapp/app/layouts/navbar/navbar.component.html":
/*!******************************************************************!*\
  !*** ./src/main/webapp/app/layouts/navbar/navbar.component.html ***!
  \******************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

eval("module.exports = \"<nav class=\\\"navbar navbar-dark navbar-expand-md jh-navbar\\\"> <div class=\\\"jh-logo-container float-left\\\"> <a class=\\\"jh-navbar-toggler d-lg-none float-right\\\" href=\\\"javascript:void(0);\\\" data-toggle=\\\"collapse\\\" data-target=\\\"#navbarResponsive\\\" aria-controls=\\\"navbarResponsive\\\" aria-expanded=\\\"false\\\" aria-label=\\\"Toggle navigation\\\" (click)=\\\"toggleNavbar()\\\"> <fa-icon [icon]=\\\"'bars'\\\"></fa-icon> </a> <a class=\\\"navbar-brand logo float-left\\\" routerLink=\\\"/\\\" (click)=\\\"collapseNavbar()\\\"> <span class=\\\"logo-img\\\"></span> <span jhiTranslate=\\\"global.title\\\" class=\\\"navbar-title\\\">Parco Auto CNR</span> AAA </a> </div> <div class=\\\"navbar-collapse collapse\\\" id=\\\"navbarResponsive\\\" [ngbCollapse]=\\\"isNavbarCollapsed\\\" [ngSwitch]=\\\"isAuthenticated()\\\"> <ul class=\\\"navbar-nav ml-auto\\\"> <li class=\\\"nav-item\\\" routerLinkActive=\\\"active\\\" [routerLinkActiveOptions]=\\\"{exact: true}\\\"> <a class=\\\"nav-link\\\" routerLink=\\\"/\\\" (click)=\\\"collapseNavbar()\\\"> <span> <fa-icon [icon]=\\\"'home'\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.home\\\">Home</span> </span> </a> </li> <li *ngSwitchCase=\\\"true\\\" ngbDropdown class=\\\"nav-item dropdown pointer\\\" routerLinkActive=\\\"active\\\" [routerLinkActiveOptions]=\\\"{exact: true}\\\"> <a class=\\\"nav-link dropdown-toggle\\\" ngbDropdownToggle href=\\\"javascript:void(0);\\\" id=\\\"entity-menu\\\"> <span> <fa-icon [icon]=\\\"'th-list'\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.entities.main\\\"> Entities </span> </span> </a> <ul class=\\\"dropdown-menu\\\" ngbDropdownMenu> <li *jhiHasAnyAuthority=\\\"'ROLE_ADMIN'\\\"> <a class=\\\"dropdown-item\\\" routerLink=\\\"istituti\\\" routerLinkActive=\\\"active\\\" [routerLinkActiveOptions]=\\\"{ exact: true }\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'asterisk'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.entities.istituti\\\">Istituti</span> </a> </li> <li *jhiHasAnyAuthority=\\\"['ROLE_ADMIN', 'ROLE_USER', 'ROLE_VIEW']\\\"> <a class=\\\"dropdown-item\\\" routerLink=\\\"auto\\\" routerLinkActive=\\\"active\\\" [routerLinkActiveOptions]=\\\"{ exact: true }\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'asterisk'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.entities.auto\\\">Auto</span> </a> </li> <li *jhiHasAnyAuthority=\\\"'ROLE_ADMIN'\\\"> <a class=\\\"dropdown-item\\\" routerLink=\\\"user-istituti\\\" routerLinkActive=\\\"active\\\" [routerLinkActiveOptions]=\\\"{ exact: true }\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'asterisk'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.entities.userIstituti\\\">User Istituti</span> </a> </li> </ul> </li> <li *jhiHasAnyAuthority=\\\"'ROLE_ADMIN'\\\" ngbDropdown class=\\\"nav-item dropdown pointer\\\" routerLinkActive=\\\"active\\\" [routerLinkActiveOptions]=\\\"{exact: true}\\\"> <a class=\\\"nav-link dropdown-toggle\\\" ngbDropdownToggle href=\\\"javascript:void(0);\\\" id=\\\"admin-menu\\\"> <span> <fa-icon [icon]=\\\"'user-plus'\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.main\\\">Administration</span> </span> </a> <ul class=\\\"dropdown-menu\\\" ngbDropdownMenu> <li> <a class=\\\"dropdown-item\\\" routerLink=\\\"admin/user-management\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'user'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.userManagement\\\">User management</span> </a> </li> <li> <a class=\\\"dropdown-item\\\" routerLink=\\\"admin/jhi-metrics\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'tachometer-alt'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.metrics\\\">Metrics</span> </a> </li> <li> <a class=\\\"dropdown-item\\\" routerLink=\\\"admin/jhi-health\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'heart'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.health\\\">Health</span> </a> </li> <li> <a class=\\\"dropdown-item\\\" routerLink=\\\"admin/jhi-configuration\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'list'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.configuration\\\">Configuration</span> </a> </li> <li> <a class=\\\"dropdown-item\\\" routerLink=\\\"admin/audits\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'bell'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.audits\\\">Audits</span> </a> </li> <li> <a class=\\\"dropdown-item\\\" routerLink=\\\"admin/logs\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'tasks'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.logs\\\">Logs</span> </a> </li> <li *ngIf=\\\"swaggerEnabled\\\"> <a class=\\\"dropdown-item\\\" routerLink=\\\"admin/docs\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'book'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.apidocs\\\">API</span> </a> </li> <li *ngIf=\\\"!inProduction\\\"> <a class=\\\"dropdown-item\\\" href=\\\"./h2-console\\\" target=\\\"_tab\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'hdd'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.admin.database\\\">Database</span> </a> </li> </ul> </li> <li ngbDropdown class=\\\"nav-item dropdown pointer\\\" *ngIf=\\\"languages && languages.length > 1\\\"> <a class=\\\"nav-link dropdown-toggle\\\" ngbDropdownToggle href=\\\"javascript:void(0);\\\" id=\\\"languagesnavBarDropdown\\\"> <span> <fa-icon [icon]=\\\"'flag'\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.language\\\">Language</span> </span> </a> <ul class=\\\"dropdown-menu\\\" ngbDropdownMenu> <li *ngFor=\\\"let language of languages\\\"> <a class=\\\"dropdown-item\\\" [jhiActiveMenu]=\\\"language\\\" href=\\\"javascript:void(0);\\\" (click)=\\\"changeLanguage(language);collapseNavbar();\\\">{{language | findLanguageFromKey}}</a> </li> </ul> </li> <li ngbDropdown class=\\\"nav-item dropdown pointer\\\" placement=\\\"bottom-right\\\" routerLinkActive=\\\"active\\\" [routerLinkActiveOptions]=\\\"{exact: true}\\\"> <a class=\\\"nav-link dropdown-toggle\\\" ngbDropdownToggle href=\\\"javascript:void(0);\\\" id=\\\"account-menu\\\"> <span *ngIf=\\\"!getImageUrl()\\\"> <fa-icon [icon]=\\\"'user'\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.account.main\\\"> Account </span> </span> <span *ngIf=\\\"getImageUrl()\\\"> <img [src]=\\\"getImageUrl()\\\" class=\\\"profile-image img-circle\\\" alt=\\\"Avatar\\\"> </span> </a> <ul class=\\\"dropdown-menu\\\" ngbDropdownMenu> <li *ngSwitchCase=\\\"true\\\"> <a class=\\\"dropdown-item\\\" routerLink=\\\"settings\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'wrench'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.account.settings\\\">Settings</span> </a> </li> <li *ngSwitchCase=\\\"true\\\"> <a class=\\\"dropdown-item\\\" routerLink=\\\"password\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'clock'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.account.password\\\">Password</span> </a> </li> <li *ngSwitchCase=\\\"true\\\"> <a class=\\\"dropdown-item\\\" (click)=\\\"logout()\\\" id=\\\"logout\\\"> <fa-icon [icon]=\\\"'sign-out-alt'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.account.logout\\\">Sign out</span> </a> </li> <li *ngSwitchCase=\\\"false\\\"> <a class=\\\"dropdown-item\\\" (click)=\\\"login()\\\" id=\\\"login\\\"> <fa-icon [icon]=\\\"'sign-in-alt'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.account.login\\\">Sign in</span> </a> </li> <li *ngSwitchCase=\\\"false\\\"> <a class=\\\"dropdown-item\\\" routerLink=\\\"register\\\" routerLinkActive=\\\"active\\\" (click)=\\\"collapseNavbar()\\\"> <fa-icon [icon]=\\\"'user-plus'\\\" [fixedWidth]=\\\"true\\\"></fa-icon> <span jhiTranslate=\\\"global.menu.account.register\\\">Register</span> </a> </li> </ul> </li> </ul> </div> </nav> \";//# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vLi9zcmMvbWFpbi93ZWJhcHAvYXBwL2xheW91dHMvbmF2YmFyL25hdmJhci5jb21wb25lbnQuaHRtbD83MzI0Il0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBLDRNQUE0TSxvckJBQW9yQixZQUFZLGlVQUFpVSxZQUFZLHNGQUFzRiwwVkFBMFYsY0FBYywrVkFBK1YsY0FBYyxvVUFBb1UsY0FBYyx3VkFBd1YsWUFBWSxzRkFBc0YsKzdFQUErN0Usa1VBQWtVLHNDQUFzQyxpQkFBaUIsS0FBSyxnQ0FBZ0MsK0pBQStKLFlBQVksc0ZBQXNGIiwiZmlsZSI6Ii4vc3JjL21haW4vd2ViYXBwL2FwcC9sYXlvdXRzL25hdmJhci9uYXZiYXIuY29tcG9uZW50Lmh0bWwuanMiLCJzb3VyY2VzQ29udGVudCI6WyJtb2R1bGUuZXhwb3J0cyA9IFwiPG5hdiBjbGFzcz1cXFwibmF2YmFyIG5hdmJhci1kYXJrIG5hdmJhci1leHBhbmQtbWQgamgtbmF2YmFyXFxcIj4gPGRpdiBjbGFzcz1cXFwiamgtbG9nby1jb250YWluZXIgZmxvYXQtbGVmdFxcXCI+IDxhIGNsYXNzPVxcXCJqaC1uYXZiYXItdG9nZ2xlciBkLWxnLW5vbmUgZmxvYXQtcmlnaHRcXFwiIGhyZWY9XFxcImphdmFzY3JpcHQ6dm9pZCgwKTtcXFwiIGRhdGEtdG9nZ2xlPVxcXCJjb2xsYXBzZVxcXCIgZGF0YS10YXJnZXQ9XFxcIiNuYXZiYXJSZXNwb25zaXZlXFxcIiBhcmlhLWNvbnRyb2xzPVxcXCJuYXZiYXJSZXNwb25zaXZlXFxcIiBhcmlhLWV4cGFuZGVkPVxcXCJmYWxzZVxcXCIgYXJpYS1sYWJlbD1cXFwiVG9nZ2xlIG5hdmlnYXRpb25cXFwiIChjbGljayk9XFxcInRvZ2dsZU5hdmJhcigpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCInYmFycydcXFwiPjwvZmEtaWNvbj4gPC9hPiA8YSBjbGFzcz1cXFwibmF2YmFyLWJyYW5kIGxvZ28gZmxvYXQtbGVmdFxcXCIgcm91dGVyTGluaz1cXFwiL1xcXCIgKGNsaWNrKT1cXFwiY29sbGFwc2VOYXZiYXIoKVxcXCI+IDxzcGFuIGNsYXNzPVxcXCJsb2dvLWltZ1xcXCI+PC9zcGFuPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC50aXRsZVxcXCIgY2xhc3M9XFxcIm5hdmJhci10aXRsZVxcXCI+UGFyY28gQXV0byBDTlI8L3NwYW4+IEFBQSA8L2E+IDwvZGl2PiA8ZGl2IGNsYXNzPVxcXCJuYXZiYXItY29sbGFwc2UgY29sbGFwc2VcXFwiIGlkPVxcXCJuYXZiYXJSZXNwb25zaXZlXFxcIiBbbmdiQ29sbGFwc2VdPVxcXCJpc05hdmJhckNvbGxhcHNlZFxcXCIgW25nU3dpdGNoXT1cXFwiaXNBdXRoZW50aWNhdGVkKClcXFwiPiA8dWwgY2xhc3M9XFxcIm5hdmJhci1uYXYgbWwtYXV0b1xcXCI+IDxsaSBjbGFzcz1cXFwibmF2LWl0ZW1cXFwiIHJvdXRlckxpbmtBY3RpdmU9XFxcImFjdGl2ZVxcXCIgW3JvdXRlckxpbmtBY3RpdmVPcHRpb25zXT1cXFwie2V4YWN0OiB0cnVlfVxcXCI+IDxhIGNsYXNzPVxcXCJuYXYtbGlua1xcXCIgcm91dGVyTGluaz1cXFwiL1xcXCIgKGNsaWNrKT1cXFwiY29sbGFwc2VOYXZiYXIoKVxcXCI+IDxzcGFuPiA8ZmEtaWNvbiBbaWNvbl09XFxcIidob21lJ1xcXCI+PC9mYS1pY29uPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC5tZW51LmhvbWVcXFwiPkhvbWU8L3NwYW4+IDwvc3Bhbj4gPC9hPiA8L2xpPiA8bGkgKm5nU3dpdGNoQ2FzZT1cXFwidHJ1ZVxcXCIgbmdiRHJvcGRvd24gY2xhc3M9XFxcIm5hdi1pdGVtIGRyb3Bkb3duIHBvaW50ZXJcXFwiIHJvdXRlckxpbmtBY3RpdmU9XFxcImFjdGl2ZVxcXCIgW3JvdXRlckxpbmtBY3RpdmVPcHRpb25zXT1cXFwie2V4YWN0OiB0cnVlfVxcXCI+IDxhIGNsYXNzPVxcXCJuYXYtbGluayBkcm9wZG93bi10b2dnbGVcXFwiIG5nYkRyb3Bkb3duVG9nZ2xlIGhyZWY9XFxcImphdmFzY3JpcHQ6dm9pZCgwKTtcXFwiIGlkPVxcXCJlbnRpdHktbWVudVxcXCI+IDxzcGFuPiA8ZmEtaWNvbiBbaWNvbl09XFxcIid0aC1saXN0J1xcXCI+PC9mYS1pY29uPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC5tZW51LmVudGl0aWVzLm1haW5cXFwiPiBFbnRpdGllcyA8L3NwYW4+IDwvc3Bhbj4gPC9hPiA8dWwgY2xhc3M9XFxcImRyb3Bkb3duLW1lbnVcXFwiIG5nYkRyb3Bkb3duTWVudT4gPGxpICpqaGlIYXNBbnlBdXRob3JpdHk9XFxcIidST0xFX0FETUlOJ1xcXCI+IDxhIGNsYXNzPVxcXCJkcm9wZG93bi1pdGVtXFxcIiByb3V0ZXJMaW5rPVxcXCJpc3RpdHV0aVxcXCIgcm91dGVyTGlua0FjdGl2ZT1cXFwiYWN0aXZlXFxcIiBbcm91dGVyTGlua0FjdGl2ZU9wdGlvbnNdPVxcXCJ7IGV4YWN0OiB0cnVlIH1cXFwiIChjbGljayk9XFxcImNvbGxhcHNlTmF2YmFyKClcXFwiPiA8ZmEtaWNvbiBbaWNvbl09XFxcIidhc3RlcmlzaydcXFwiIFtmaXhlZFdpZHRoXT1cXFwidHJ1ZVxcXCI+PC9mYS1pY29uPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC5tZW51LmVudGl0aWVzLmlzdGl0dXRpXFxcIj5Jc3RpdHV0aTwvc3Bhbj4gPC9hPiA8L2xpPiA8bGkgKmpoaUhhc0FueUF1dGhvcml0eT1cXFwiWydST0xFX0FETUlOJywgJ1JPTEVfVVNFUicsICdST0xFX1ZJRVcnXVxcXCI+IDxhIGNsYXNzPVxcXCJkcm9wZG93bi1pdGVtXFxcIiByb3V0ZXJMaW5rPVxcXCJhdXRvXFxcIiByb3V0ZXJMaW5rQWN0aXZlPVxcXCJhY3RpdmVcXFwiIFtyb3V0ZXJMaW5rQWN0aXZlT3B0aW9uc109XFxcInsgZXhhY3Q6IHRydWUgfVxcXCIgKGNsaWNrKT1cXFwiY29sbGFwc2VOYXZiYXIoKVxcXCI+IDxmYS1pY29uIFtpY29uXT1cXFwiJ2FzdGVyaXNrJ1xcXCIgW2ZpeGVkV2lkdGhdPVxcXCJ0cnVlXFxcIj48L2ZhLWljb24+IDxzcGFuIGpoaVRyYW5zbGF0ZT1cXFwiZ2xvYmFsLm1lbnUuZW50aXRpZXMuYXV0b1xcXCI+QXV0bzwvc3Bhbj4gPC9hPiA8L2xpPiA8bGkgKmpoaUhhc0FueUF1dGhvcml0eT1cXFwiJ1JPTEVfQURNSU4nXFxcIj4gPGEgY2xhc3M9XFxcImRyb3Bkb3duLWl0ZW1cXFwiIHJvdXRlckxpbms9XFxcInVzZXItaXN0aXR1dGlcXFwiIHJvdXRlckxpbmtBY3RpdmU9XFxcImFjdGl2ZVxcXCIgW3JvdXRlckxpbmtBY3RpdmVPcHRpb25zXT1cXFwieyBleGFjdDogdHJ1ZSB9XFxcIiAoY2xpY2spPVxcXCJjb2xsYXBzZU5hdmJhcigpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCInYXN0ZXJpc2snXFxcIiBbZml4ZWRXaWR0aF09XFxcInRydWVcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5lbnRpdGllcy51c2VySXN0aXR1dGlcXFwiPlVzZXIgSXN0aXR1dGk8L3NwYW4+IDwvYT4gPC9saT4gPC91bD4gPC9saT4gPGxpICpqaGlIYXNBbnlBdXRob3JpdHk9XFxcIidST0xFX0FETUlOJ1xcXCIgbmdiRHJvcGRvd24gY2xhc3M9XFxcIm5hdi1pdGVtIGRyb3Bkb3duIHBvaW50ZXJcXFwiIHJvdXRlckxpbmtBY3RpdmU9XFxcImFjdGl2ZVxcXCIgW3JvdXRlckxpbmtBY3RpdmVPcHRpb25zXT1cXFwie2V4YWN0OiB0cnVlfVxcXCI+IDxhIGNsYXNzPVxcXCJuYXYtbGluayBkcm9wZG93bi10b2dnbGVcXFwiIG5nYkRyb3Bkb3duVG9nZ2xlIGhyZWY9XFxcImphdmFzY3JpcHQ6dm9pZCgwKTtcXFwiIGlkPVxcXCJhZG1pbi1tZW51XFxcIj4gPHNwYW4+IDxmYS1pY29uIFtpY29uXT1cXFwiJ3VzZXItcGx1cydcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5hZG1pbi5tYWluXFxcIj5BZG1pbmlzdHJhdGlvbjwvc3Bhbj4gPC9zcGFuPiA8L2E+IDx1bCBjbGFzcz1cXFwiZHJvcGRvd24tbWVudVxcXCIgbmdiRHJvcGRvd25NZW51PiA8bGk+IDxhIGNsYXNzPVxcXCJkcm9wZG93bi1pdGVtXFxcIiByb3V0ZXJMaW5rPVxcXCJhZG1pbi91c2VyLW1hbmFnZW1lbnRcXFwiIHJvdXRlckxpbmtBY3RpdmU9XFxcImFjdGl2ZVxcXCIgKGNsaWNrKT1cXFwiY29sbGFwc2VOYXZiYXIoKVxcXCI+IDxmYS1pY29uIFtpY29uXT1cXFwiJ3VzZXInXFxcIiBbZml4ZWRXaWR0aF09XFxcInRydWVcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5hZG1pbi51c2VyTWFuYWdlbWVudFxcXCI+VXNlciBtYW5hZ2VtZW50PC9zcGFuPiA8L2E+IDwvbGk+IDxsaT4gPGEgY2xhc3M9XFxcImRyb3Bkb3duLWl0ZW1cXFwiIHJvdXRlckxpbms9XFxcImFkbWluL2poaS1tZXRyaWNzXFxcIiByb3V0ZXJMaW5rQWN0aXZlPVxcXCJhY3RpdmVcXFwiIChjbGljayk9XFxcImNvbGxhcHNlTmF2YmFyKClcXFwiPiA8ZmEtaWNvbiBbaWNvbl09XFxcIid0YWNob21ldGVyLWFsdCdcXFwiIFtmaXhlZFdpZHRoXT1cXFwidHJ1ZVxcXCI+PC9mYS1pY29uPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC5tZW51LmFkbWluLm1ldHJpY3NcXFwiPk1ldHJpY3M8L3NwYW4+IDwvYT4gPC9saT4gPGxpPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgcm91dGVyTGluaz1cXFwiYWRtaW4vamhpLWhlYWx0aFxcXCIgcm91dGVyTGlua0FjdGl2ZT1cXFwiYWN0aXZlXFxcIiAoY2xpY2spPVxcXCJjb2xsYXBzZU5hdmJhcigpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCInaGVhcnQnXFxcIiBbZml4ZWRXaWR0aF09XFxcInRydWVcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5hZG1pbi5oZWFsdGhcXFwiPkhlYWx0aDwvc3Bhbj4gPC9hPiA8L2xpPiA8bGk+IDxhIGNsYXNzPVxcXCJkcm9wZG93bi1pdGVtXFxcIiByb3V0ZXJMaW5rPVxcXCJhZG1pbi9qaGktY29uZmlndXJhdGlvblxcXCIgcm91dGVyTGlua0FjdGl2ZT1cXFwiYWN0aXZlXFxcIiAoY2xpY2spPVxcXCJjb2xsYXBzZU5hdmJhcigpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCInbGlzdCdcXFwiIFtmaXhlZFdpZHRoXT1cXFwidHJ1ZVxcXCI+PC9mYS1pY29uPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC5tZW51LmFkbWluLmNvbmZpZ3VyYXRpb25cXFwiPkNvbmZpZ3VyYXRpb248L3NwYW4+IDwvYT4gPC9saT4gPGxpPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgcm91dGVyTGluaz1cXFwiYWRtaW4vYXVkaXRzXFxcIiByb3V0ZXJMaW5rQWN0aXZlPVxcXCJhY3RpdmVcXFwiIChjbGljayk9XFxcImNvbGxhcHNlTmF2YmFyKClcXFwiPiA8ZmEtaWNvbiBbaWNvbl09XFxcIidiZWxsJ1xcXCIgW2ZpeGVkV2lkdGhdPVxcXCJ0cnVlXFxcIj48L2ZhLWljb24+IDxzcGFuIGpoaVRyYW5zbGF0ZT1cXFwiZ2xvYmFsLm1lbnUuYWRtaW4uYXVkaXRzXFxcIj5BdWRpdHM8L3NwYW4+IDwvYT4gPC9saT4gPGxpPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgcm91dGVyTGluaz1cXFwiYWRtaW4vbG9nc1xcXCIgcm91dGVyTGlua0FjdGl2ZT1cXFwiYWN0aXZlXFxcIiAoY2xpY2spPVxcXCJjb2xsYXBzZU5hdmJhcigpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCIndGFza3MnXFxcIiBbZml4ZWRXaWR0aF09XFxcInRydWVcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5hZG1pbi5sb2dzXFxcIj5Mb2dzPC9zcGFuPiA8L2E+IDwvbGk+IDxsaSAqbmdJZj1cXFwic3dhZ2dlckVuYWJsZWRcXFwiPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgcm91dGVyTGluaz1cXFwiYWRtaW4vZG9jc1xcXCIgcm91dGVyTGlua0FjdGl2ZT1cXFwiYWN0aXZlXFxcIiAoY2xpY2spPVxcXCJjb2xsYXBzZU5hdmJhcigpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCInYm9vaydcXFwiIFtmaXhlZFdpZHRoXT1cXFwidHJ1ZVxcXCI+PC9mYS1pY29uPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC5tZW51LmFkbWluLmFwaWRvY3NcXFwiPkFQSTwvc3Bhbj4gPC9hPiA8L2xpPiA8bGkgKm5nSWY9XFxcIiFpblByb2R1Y3Rpb25cXFwiPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgaHJlZj1cXFwiLi9oMi1jb25zb2xlXFxcIiB0YXJnZXQ9XFxcIl90YWJcXFwiIChjbGljayk9XFxcImNvbGxhcHNlTmF2YmFyKClcXFwiPiA8ZmEtaWNvbiBbaWNvbl09XFxcIidoZGQnXFxcIiBbZml4ZWRXaWR0aF09XFxcInRydWVcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5hZG1pbi5kYXRhYmFzZVxcXCI+RGF0YWJhc2U8L3NwYW4+IDwvYT4gPC9saT4gPC91bD4gPC9saT4gPGxpIG5nYkRyb3Bkb3duIGNsYXNzPVxcXCJuYXYtaXRlbSBkcm9wZG93biBwb2ludGVyXFxcIiAqbmdJZj1cXFwibGFuZ3VhZ2VzICYmIGxhbmd1YWdlcy5sZW5ndGggPiAxXFxcIj4gPGEgY2xhc3M9XFxcIm5hdi1saW5rIGRyb3Bkb3duLXRvZ2dsZVxcXCIgbmdiRHJvcGRvd25Ub2dnbGUgaHJlZj1cXFwiamF2YXNjcmlwdDp2b2lkKDApO1xcXCIgaWQ9XFxcImxhbmd1YWdlc25hdkJhckRyb3Bkb3duXFxcIj4gPHNwYW4+IDxmYS1pY29uIFtpY29uXT1cXFwiJ2ZsYWcnXFxcIj48L2ZhLWljb24+IDxzcGFuIGpoaVRyYW5zbGF0ZT1cXFwiZ2xvYmFsLm1lbnUubGFuZ3VhZ2VcXFwiPkxhbmd1YWdlPC9zcGFuPiA8L3NwYW4+IDwvYT4gPHVsIGNsYXNzPVxcXCJkcm9wZG93bi1tZW51XFxcIiBuZ2JEcm9wZG93bk1lbnU+IDxsaSAqbmdGb3I9XFxcImxldCBsYW5ndWFnZSBvZiBsYW5ndWFnZXNcXFwiPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgW2poaUFjdGl2ZU1lbnVdPVxcXCJsYW5ndWFnZVxcXCIgaHJlZj1cXFwiamF2YXNjcmlwdDp2b2lkKDApO1xcXCIgKGNsaWNrKT1cXFwiY2hhbmdlTGFuZ3VhZ2UobGFuZ3VhZ2UpO2NvbGxhcHNlTmF2YmFyKCk7XFxcIj57e2xhbmd1YWdlIHwgZmluZExhbmd1YWdlRnJvbUtleX19PC9hPiA8L2xpPiA8L3VsPiA8L2xpPiA8bGkgbmdiRHJvcGRvd24gY2xhc3M9XFxcIm5hdi1pdGVtIGRyb3Bkb3duIHBvaW50ZXJcXFwiIHBsYWNlbWVudD1cXFwiYm90dG9tLXJpZ2h0XFxcIiByb3V0ZXJMaW5rQWN0aXZlPVxcXCJhY3RpdmVcXFwiIFtyb3V0ZXJMaW5rQWN0aXZlT3B0aW9uc109XFxcIntleGFjdDogdHJ1ZX1cXFwiPiA8YSBjbGFzcz1cXFwibmF2LWxpbmsgZHJvcGRvd24tdG9nZ2xlXFxcIiBuZ2JEcm9wZG93blRvZ2dsZSBocmVmPVxcXCJqYXZhc2NyaXB0OnZvaWQoMCk7XFxcIiBpZD1cXFwiYWNjb3VudC1tZW51XFxcIj4gPHNwYW4gKm5nSWY9XFxcIiFnZXRJbWFnZVVybCgpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCIndXNlcidcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5hY2NvdW50Lm1haW5cXFwiPiBBY2NvdW50IDwvc3Bhbj4gPC9zcGFuPiA8c3BhbiAqbmdJZj1cXFwiZ2V0SW1hZ2VVcmwoKVxcXCI+IDxpbWcgW3NyY109XFxcImdldEltYWdlVXJsKClcXFwiIGNsYXNzPVxcXCJwcm9maWxlLWltYWdlIGltZy1jaXJjbGVcXFwiIGFsdD1cXFwiQXZhdGFyXFxcIj4gPC9zcGFuPiA8L2E+IDx1bCBjbGFzcz1cXFwiZHJvcGRvd24tbWVudVxcXCIgbmdiRHJvcGRvd25NZW51PiA8bGkgKm5nU3dpdGNoQ2FzZT1cXFwidHJ1ZVxcXCI+IDxhIGNsYXNzPVxcXCJkcm9wZG93bi1pdGVtXFxcIiByb3V0ZXJMaW5rPVxcXCJzZXR0aW5nc1xcXCIgcm91dGVyTGlua0FjdGl2ZT1cXFwiYWN0aXZlXFxcIiAoY2xpY2spPVxcXCJjb2xsYXBzZU5hdmJhcigpXFxcIj4gPGZhLWljb24gW2ljb25dPVxcXCInd3JlbmNoJ1xcXCIgW2ZpeGVkV2lkdGhdPVxcXCJ0cnVlXFxcIj48L2ZhLWljb24+IDxzcGFuIGpoaVRyYW5zbGF0ZT1cXFwiZ2xvYmFsLm1lbnUuYWNjb3VudC5zZXR0aW5nc1xcXCI+U2V0dGluZ3M8L3NwYW4+IDwvYT4gPC9saT4gPGxpICpuZ1N3aXRjaENhc2U9XFxcInRydWVcXFwiPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgcm91dGVyTGluaz1cXFwicGFzc3dvcmRcXFwiIHJvdXRlckxpbmtBY3RpdmU9XFxcImFjdGl2ZVxcXCIgKGNsaWNrKT1cXFwiY29sbGFwc2VOYXZiYXIoKVxcXCI+IDxmYS1pY29uIFtpY29uXT1cXFwiJ2Nsb2NrJ1xcXCIgW2ZpeGVkV2lkdGhdPVxcXCJ0cnVlXFxcIj48L2ZhLWljb24+IDxzcGFuIGpoaVRyYW5zbGF0ZT1cXFwiZ2xvYmFsLm1lbnUuYWNjb3VudC5wYXNzd29yZFxcXCI+UGFzc3dvcmQ8L3NwYW4+IDwvYT4gPC9saT4gPGxpICpuZ1N3aXRjaENhc2U9XFxcInRydWVcXFwiPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgKGNsaWNrKT1cXFwibG9nb3V0KClcXFwiIGlkPVxcXCJsb2dvdXRcXFwiPiA8ZmEtaWNvbiBbaWNvbl09XFxcIidzaWduLW91dC1hbHQnXFxcIiBbZml4ZWRXaWR0aF09XFxcInRydWVcXFwiPjwvZmEtaWNvbj4gPHNwYW4gamhpVHJhbnNsYXRlPVxcXCJnbG9iYWwubWVudS5hY2NvdW50LmxvZ291dFxcXCI+U2lnbiBvdXQ8L3NwYW4+IDwvYT4gPC9saT4gPGxpICpuZ1N3aXRjaENhc2U9XFxcImZhbHNlXFxcIj4gPGEgY2xhc3M9XFxcImRyb3Bkb3duLWl0ZW1cXFwiIChjbGljayk9XFxcImxvZ2luKClcXFwiIGlkPVxcXCJsb2dpblxcXCI+IDxmYS1pY29uIFtpY29uXT1cXFwiJ3NpZ24taW4tYWx0J1xcXCIgW2ZpeGVkV2lkdGhdPVxcXCJ0cnVlXFxcIj48L2ZhLWljb24+IDxzcGFuIGpoaVRyYW5zbGF0ZT1cXFwiZ2xvYmFsLm1lbnUuYWNjb3VudC5sb2dpblxcXCI+U2lnbiBpbjwvc3Bhbj4gPC9hPiA8L2xpPiA8bGkgKm5nU3dpdGNoQ2FzZT1cXFwiZmFsc2VcXFwiPiA8YSBjbGFzcz1cXFwiZHJvcGRvd24taXRlbVxcXCIgcm91dGVyTGluaz1cXFwicmVnaXN0ZXJcXFwiIHJvdXRlckxpbmtBY3RpdmU9XFxcImFjdGl2ZVxcXCIgKGNsaWNrKT1cXFwiY29sbGFwc2VOYXZiYXIoKVxcXCI+IDxmYS1pY29uIFtpY29uXT1cXFwiJ3VzZXItcGx1cydcXFwiIFtmaXhlZFdpZHRoXT1cXFwidHJ1ZVxcXCI+PC9mYS1pY29uPiA8c3BhbiBqaGlUcmFuc2xhdGU9XFxcImdsb2JhbC5tZW51LmFjY291bnQucmVnaXN0ZXJcXFwiPlJlZ2lzdGVyPC9zcGFuPiA8L2E+IDwvbGk+IDwvdWw+IDwvbGk+IDwvdWw+IDwvZGl2PiA8L25hdj4gXCI7Il0sInNvdXJjZVJvb3QiOiIifQ==\n//# sourceURL=webpack-internal:///./src/main/webapp/app/layouts/navbar/navbar.component.html\n");

/***/ })

})