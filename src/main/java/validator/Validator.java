package validator;

import java.sql.SQLException;

public interface Validator<T> {
    ValidationResult isValid(T object) throws SQLException;
}
