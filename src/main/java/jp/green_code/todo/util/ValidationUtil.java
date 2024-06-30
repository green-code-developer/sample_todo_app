package jp.green_code.todo.util;

import static jp.green_code.todo.dto.common.AppMessage.rawMessage;
import static jp.green_code.todo.util.AppCollectionUtil.arrayToList;
import static org.apache.commons.lang3.RegExUtils.removeAll;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.util.Map;
import java.util.stream.Collectors;
import jp.green_code.todo.dto.common.AppErrorResult;
import jp.green_code.todo.dto.common.AppValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

/** やることのバリデーション */
@Component
@RequiredArgsConstructor
public class ValidationUtil {

  private final SmartValidator smartValidator;
  private final MessageUtil messageUtil;

  public <T> AppValidationResult<T> validate(T o) {
    var bindingResult = rawValidate(o);
    return toMultiResult(bindingResult);
  }

  public <T> void throwIfInvalid(T o) {
    var multiResult = validate(o);
    if (!multiResult.isSuccess()) {
      var message = errorMapToString(multiResult.getErrorMap());
      throw new RuntimeException(message);
    }
  }

  String errorMapToString(Map<String, AppErrorResult> map) {
    return map.keySet().stream()
        .map(
            k -> {
              var message =
                  map.get(k).getAppMessageList().stream().map(messageUtil::getMessage).collect(Collectors.joining(","));
              return k + ":" + message;
            })
        .collect(Collectors.joining(", "));
  }

  public <T> BindingResult rawValidate(T o) {
    var result = new DataBinder(o).getBindingResult();
    smartValidator.validate(o, result);
    return result;
  }

  public static <T> AppValidationResult<T> toMultiResult(BindingResult bindingResult) {
    var result = new AppValidationResult<>((T) bindingResult.getTarget());
    bindingResult
        .getAllErrors()
        .forEach(
            oe -> {
              var message = rawMessage(oe.getDefaultMessage());
              var field = toField(oe);
              // assertionOf を削除する
              field = uncapitalize(removeAll(field, "^assertionOf"));
              if (!result.getMap().containsKey(field)) {
                result.getMap().put(field, new AppErrorResult());
              }
              result.getMap().get(field).getAppMessageList().add(message);
            });
    return result;
  }

  static String toField(ObjectError objectError) {
    var arguments = objectError.getArguments();
    var argumentList = arrayToList(arguments);
    for (Object o : argumentList) {
      if (o instanceof DefaultMessageSourceResolvable d) {
        return d.getDefaultMessage();
      }
    }
    return "";
  }
}
