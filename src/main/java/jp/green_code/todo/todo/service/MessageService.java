package jp.green_code.todo.todo.service;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// MessageSource のラッパー
// Locale をLocaleContextHolder から取得する点が異なる
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object... params) {
        return getMessage(code, params, getLocale());
    }

    public String getMessage(String code, Object[] params, Locale locale) {
        return messageSource.getMessage(code, params, locale);
    }
}
