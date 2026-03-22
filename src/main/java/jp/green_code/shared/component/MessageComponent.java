package jp.green_code.shared.component;

import jp.green_code.shared.dto.MessageKey;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

/**
 * MessageSource のラッパー
 * Locale をLocaleContextHolder から取得している
 */
@Service("messageComponent")
public class MessageComponent {

    private final MessageSource messageSource;

    public MessageComponent(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Object... args) {
        return getMessage(key, args, getLocale());
    }

    public String getMessage(String code, Object[] params, Locale locale) {
        return messageSource.getMessage(code, params, locale);
    }

    public String getMessage(MessageKey message) {
        return getMessage(message.key(), message.args());
    }

    public String getMessage(MessageKey message, Locale locale) {
        return getMessage(message.key(), message.args(), locale);
    }
}
