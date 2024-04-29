package com.example.todo.util;

import static org.apache.commons.lang3.StringUtils.trim;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 日付関連の定数と変換メソッド
public class DateUtil {

    // デフォルトの日付フォーマット
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    // デフォルトの日付フォーマットのフォーマッタ
    // LocalDate --> String の場合はこちらを主に使用する
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER =
        DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    // デフォルトの日時フォーマット（時刻あり）
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // デフォルトの日時フォーマットのフォーマッタ（時刻あり）
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    // デフォルトの日時
    public static final LocalDateTime BLANK_LOCAL_TIME =
        LocalDateTime.parse("1900-01-01 00:00:00", DEFAULT_DATE_TIME_FORMATTER);

    public static final LocalDate BLANK_LOCAL_DATE = BLANK_LOCAL_TIME.toLocalDate();

    // DEFAULT_DATE_FORMAT を使うと桁数を厳密に守る必要が出る
    // LocalDate --> String の場合は柔軟性を上げるためゆるくする
    public static final String PARSE_DATE_FORMAT = "y-M-d";
    public static final DateTimeFormatter PARSE_DATE_FORMATTER =
        DateTimeFormatter.ofPattern(PARSE_DATE_FORMAT);

    // DEFAULT_DATE_TIME_FORMAT を使うと桁数を厳密に守る必要が出る
    // LocalDateTime --> String の場合は柔軟性を上げるためゆるくする
    public static final String PARSE_DATE_TIME_FORMAT = "y-M-d H:m:s";
    public static final DateTimeFormatter PARSE_DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern(PARSE_DATE_TIME_FORMAT);

    // PARSE_DATE_FORMAT の文字列を日付に変換
    // 変換できなければnull を返す
    // 例外は握りつぶす
    public static LocalDate toLocalDate(String s) {
        // 日付に変換できなければエラー
        try {
            return LocalDate.parse(trim(s), PARSE_DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    // PARSE_DATE_TIME_FORMAT の文字列を日付に変換
    // 変換できなければnull を返す
    // 例外は握りつぶす
    public static LocalDateTime toLocalDateTime(String s) {
        // 日付に変換できなければエラー
        try {
            return LocalDateTime.parse(trim(s), PARSE_DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    // LocalDate をString に変換
    public static String dateToString(LocalDate date) {
        return date == null ? "" : DEFAULT_DATE_FORMATTER.format(date);
    }

    // LocalDateTime を日時のString に変換
    public static String dateTimeToString(LocalDateTime dateTime) {
        return dateTime == null ? "" : DEFAULT_DATE_TIME_FORMATTER.format(dateTime);
    }

    // LocalDateTime を日付のString に変換
    public static String dateTimeToDateString(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateToString(dateTime.toLocalDate());
    }
}
