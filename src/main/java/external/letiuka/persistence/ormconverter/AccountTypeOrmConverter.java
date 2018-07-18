package external.letiuka.persistence.ormconverter;

import external.letiuka.modelviewcontroller.model.AccountType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AccountTypeOrmConverter implements AttributeConverter<AccountType,String> {
    @Override
    public String convertToDatabaseColumn(AccountType accountType) {
        return accountType.toString();
    }

    @Override
    public AccountType convertToEntityAttribute(String s) {
        return AccountType.valueOf(s);
    }
}
