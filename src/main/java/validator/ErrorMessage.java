package validator;

public interface ErrorMessage {
    String SERVER_ERROR = "Ошибка сервера";
    String MISSING_PARAMETER = "Отсутствует параметр %s";
    String NOT_FOUND = "Объект(ы) (%s) не найден(ы)";
    String INVALID_PARAMETER = "Параметр %s содердит не допустимые символы";
    String INVALID_CODE = "Параметр code должен соответствовать стандарту ISO 4217";
    String ERROR_EXCHANGE = "Невозможно выполнить обмен (отсутствует возможность конвертации)";
    String ERROR_CONNECTION = "Подключение к базе данных не выполнено";




}
