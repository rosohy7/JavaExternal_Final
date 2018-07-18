package external.letiuka.persistence.ormconverter;

import external.letiuka.security.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleOrmConverter implements AttributeConverter<Role,String> {
    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.toString();
    }

    @Override
    public Role convertToEntityAttribute(String s) {
        return Role.valueOf(s);
    }
}
