package jp.green_code.shared.component;

import jp.green_code.shared.dto.EnumLabel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

// 画面で使用するため、表示用文字列をmessage.properties から取得する
@Component("enumLabelResolver")
public class EnumLabelResolver {

    private final MessageComponent messageComponent;

    public EnumLabelResolver(MessageComponent messageComponent) {
        this.messageComponent = messageComponent;
    }

    public <T extends Enum<T>> List<EnumLabel<T>> enumValues(Class<T> clazz) {
        return Arrays.stream(clazz.getEnumConstants()).map(e -> new EnumLabel<>(e, getLabel(e))).toList();
    }

    public <T extends Enum<T>>String getLabel(T e) {
        return messageComponent.getMessage("enum." + e.getDeclaringClass().getSimpleName() + "." + e.name());
    }
}
