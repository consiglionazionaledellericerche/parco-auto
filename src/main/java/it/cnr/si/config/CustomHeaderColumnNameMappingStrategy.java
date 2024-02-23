package it.cnr.si.config;

import com.opencsv.bean.FieldMap;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class CustomHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy {

    @Override
    public FieldMap getFieldMap() {
        return super.getFieldMap();
    }
}
