package it.cnr.si.config;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomCsvBindByPosition {
    /**
     *
     * @return Column position in csv
     */
    int value();
}
