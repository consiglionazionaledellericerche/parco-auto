/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ParcoautoTestModule } from '../../../test.module';
import { MultaDeleteDialogComponent } from 'app/entities/multa/multa-delete-dialog.component';
import { MultaService } from 'app/entities/multa/multa.service';

describe('Component Tests', () => {
    describe('Multa Management Delete Component', () => {
        let comp: MultaDeleteDialogComponent;
        let fixture: ComponentFixture<MultaDeleteDialogComponent>;
        let service: MultaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ParcoautoTestModule],
                declarations: [MultaDeleteDialogComponent]
            })
                .overrideTemplate(MultaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MultaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MultaService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
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
                )
            );
        });
    });
});
