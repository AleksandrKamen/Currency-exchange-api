package validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {
    int code;
    String message;
}
