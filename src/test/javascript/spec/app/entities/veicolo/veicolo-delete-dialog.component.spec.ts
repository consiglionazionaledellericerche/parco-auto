/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ParcoautoTestModule } from '../../../test.module';
import { VeicoloDeleteDialogComponent } from 'app/entities/veicolo/veicolo-delete-dialog.component';
import { VeicoloService } from 'app/entities/veicolo/veicolo.service';

describe('Component Tests', () => {
    describe('Veicolo Management Delete Component', () => {
        let comp: VeicoloDeleteDialogComponent;
        let fixture: ComponentFixture<VeicoloDeleteDialogComponent>;
        let service: VeicoloService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ParcoautoTestModule],
                declarations: [VeicoloDeleteDialogComponent]
            })
                .overrideTemplate(VeicoloDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VeicoloDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VeicoloService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
