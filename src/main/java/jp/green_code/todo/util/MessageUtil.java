package jp.green_code.todo.util;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import java.util.Locale;
import jp.green_code.todo.dto.common.AppMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * MessageSource のラッパー
 * Locale をLocaleContextHolder から取得している
 */
@Service("messageUtil")
@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getMessage(String code, Object... params) {
        return getMessage(code, params, getLocale());
    }

    public String getMessage(String code, Object[] params, Locale locale) {
        return messageSource.getMessage(code, params, locale);
    }

    public String getMessage(AppMessage message) {
        if (StringUtils.isBlank(message.getMessage())) {
            return getMessage(message.getCode(), message.getParams());
        } else {
            return message.getMessage();
        }
    }
}
