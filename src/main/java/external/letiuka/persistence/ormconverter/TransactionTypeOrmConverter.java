package external.letiuka.persistence.ormconverter;

import external.letiuka.modelviewcontroller.model.TransactionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeOrmConverter implements AttributeConverter<TransactionType,String> {
    @Override
    public String convertToDatabaseColumn(TransactionType transactionType) {
        return transactionType.toString();
    }

    @Override
    public TransactionType convertToEntityAttribute(String s) {
        return TransactionType.valueOf(s);
    }
}
