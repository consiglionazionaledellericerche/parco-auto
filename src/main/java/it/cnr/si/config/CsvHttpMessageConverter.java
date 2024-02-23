package it.cnr.si.config;


import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanField;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CsvHttpMessageConverter<T extends Serializable, L extends List<T>>
        extends AbstractHttpMessageConverter<L> {

    public CsvHttpMessageConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    protected L readInternal(Class<? extends L> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new RuntimeException("Not yet implemented");
    }

    public void write(L l, Writer writer) throws IOException{
        CustomHeaderColumnNameMappingStrategy<T> strategy = new CustomHeaderColumnNameMappingStrategy<T>();
        final Optional<?> any = l.stream().findAny();
        if (!any.isPresent())
            return;
        final Class<?> aClass = any.get().getClass();
        strategy.setType(aClass);
        strategy.setColumnOrderOnWrite(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return getCustomCsvBindByPosition(strategy, aClass, o1).compareTo(getCustomCsvBindByPosition(strategy, aClass, o2));
            }
        });

        StatefulBeanToCsv<T> beanToCsv =
                new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                        .withSeparator(';')
                        .withMappingStrategy(strategy)
                        .build();
        try {
            beanToCsv.write(l);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void writeInternal(L l, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        write(l, new OutputStreamWriter(outputMessage.getBody()));
    }

    private BigInteger getCustomCsvBindByPosition(CustomHeaderColumnNameMappingStrategy<T> strategy, Class<?> masterClass, String o) {
        final BigInteger multiply = Optional.ofNullable(strategy.getFieldMap().get(o))
                .flatMap(beanField -> Optional.ofNullable(beanField.getType()))
                .map(aClass -> {
                    if (masterClass.equals(aClass))
                        return BigInteger.ONE;
                    return BigInteger.valueOf(aClass.getName().hashCode()).abs();
                })
                .orElse(BigInteger.ONE);

        return Optional.ofNullable(strategy.getFieldMap().get(o))
                .map(BeanField::getField)
                .map(Field::getDeclaredAnnotations)
                .flatMap(annotations -> {
                    return Arrays.asList(annotations)
                            .stream()
                            .filter(annotation -> annotation.annotationType().isAssignableFrom(CustomCsvBindByPosition.class))
                            .findAny()
                            .filter(CustomCsvBindByPosition.class::isInstance)
                            .map(CustomCsvBindByPosition.class::cast)
                            .map(customCsvBindByPosition -> customCsvBindByPosition.value());
                })
                .map(integer -> multiply.add(BigInteger.valueOf(integer)))
                .orElse(BigInteger.ONE);
    }
}
