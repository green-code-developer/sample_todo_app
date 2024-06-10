package jp.green_code.todo.util;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.lang3.StringUtils.trim;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Component;

// 日付関連の定数と変換メソッド
// Thymeleaf から使用するため名称を設定
@Component("dateUtil")
public class DateUtil {

    public static final String FORMAT_YMD = "yyyy/MM/dd";
    public static final String FORMAT_YMD_hyphen = "yyyy-MM-dd";
    public static final String FORMAT_YMD_hyphen_loose = "y-M-d";
    public static final String FORMAT_YMD_HM = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_YMD_HMS_LOOSE = "y-M-d H:m:s";
    public static final String FORMAT_YMD_HMSS = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final OffsetDateTime BLANK_OFFSET_TIME = parseISO("1900-01-01T00:00:00+00:00").get();

    public static String toYMD(OffsetDateTime dateTime) {
        return format(dateTime, FORMAT_YMD);
    }

    public static String toYMD_hyphen(OffsetDateTime dateTime) {
        return format(dateTime, FORMAT_YMD_hyphen);
    }

    public static String toYMD_HM(OffsetDateTime dateTime) {
        return format(dateTime, FORMAT_YMD_HM);
    }

    static String format(OffsetDateTime dateTime, String format) {
        DateTimeFormatter fmt = ofPattern(format);
        return dateTime == null ? "" : fmt.format(dateTime.atZoneSameInstant(ZoneId.systemDefault()));
    }

    // Date 型をミリ秒まで表示
    public static String dateToYMD_HMSS(Date date) {
        return new SimpleDateFormat(FORMAT_YMD_HMSS).format(date);
    }

    // "1900-01-01T00:00:00+00:00" 形式の文字列を日付に変換
    public static Optional<OffsetDateTime> parseISO(String s) {
        try {
            return Optional.of(OffsetDateTime.parse(trim(s), ISO_OFFSET_DATE_TIME));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // PARSE_DATE_TIME_FORMAT の文字列を日付に変換
    // 柔軟性を上げるためゆるく解析する
    public static Optional<LocalDateTime> parseYMD_HMS(String s) {
        try {
            var formatter = ofPattern(FORMAT_YMD_HMS_LOOSE);
            return Optional.of(LocalDateTime.parse(trim(s), formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDate> parseYMD_hyphen_loose(String s) {
        try {
            var formatter = ofPattern(FORMAT_YMD_hyphen_loose);
            return Optional.of(LocalDate.parse(trim(s), formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime ldt) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ldt.atZone(zoneId).toOffsetDateTime();
    }

    public static OffsetDateTime localDateToOffsetDateTime(LocalDate ld) {
        return toOffsetDateTime(ld.atStartOfDay());
    }
}
