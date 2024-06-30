package jp.green_code.todo.util;

import static jp.green_code.todo.web.interceptor.MdcInterceptor.MDC_KEY_ACCOUNT_ID;

import java.util.Optional;
import jp.green_code.todo.entity.BaseEntity;
import org.slf4j.MDC;

public class ThreadLocalUtil {
  public static Optional<Long> getAccountId() {
    var mdcAccountId = MDC.get(MDC_KEY_ACCOUNT_ID);
    return AppNumberUtil.toLong(mdcAccountId);
  }

  public static void setAccountId(long accountId) {
    MDC.put(MDC_KEY_ACCOUNT_ID, accountId + "");
  }

  public static void beforeSave(BaseEntity e) {
    getAccountId().ifPresent(l -> {
      e.setUpdatedBy(l);
      e.setCreatedBy(l);
    });
  }
}
