export interface IProblem {
    idSegnalazione?: number;
    firstName?: string;
    familyName?: string;
    email?: string;
    login?: string;
    titolo?: string;
    descrizione?: string;
    categoria?: number;
    categoriaDescrizione?: string;
    confirmRequested?: string;
    nota?: string;
    stato?: string;
    allegato?: any;
    allegatoContentType?: string;
}

export class Problem implements IProblem {
    constructor(
        public idSegnalazione?: number,
        public firstName?: string,
        public familyName?: string,
        public email?: string,
        public login?: string,
        public titolo?: string,
        public descrizione?: string,
        public categoria?: number,
        public categoriaDescrizione?: string,
        public confirmRequested?: string,
        public nota?: string,
        public stato?: string,
        public allegato?: any,
        public allegatoContentType?: any
    ) {}
}
