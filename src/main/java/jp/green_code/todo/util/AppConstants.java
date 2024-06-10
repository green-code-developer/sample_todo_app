package jp.green_code.todo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 定数群
public class AppConstants {

  // アカウントID のデフォルト値
  public static final long SYSTEM_ACCOUNT_ID = -1;

  // 呼び出し元のfilter でMDC に設定されたUUID を取得するためのキー
  public static String MDC_REQUEST_UUID = "request_uuid";

}
