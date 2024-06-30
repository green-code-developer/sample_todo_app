package jp.green_code.todo.util;

import java.util.Optional;

/**
 * 文字列から数値の変換を行い、Optional で返却する
 */
public class AppNumberUtil {
  public static Optional<Long> toLong(String s) {
    try {
      return Optional.of(Long.valueOf(s));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public static Optional<Integer> toInteger(String s) {
    try {
      return Optional.of(Integer.valueOf(s));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
