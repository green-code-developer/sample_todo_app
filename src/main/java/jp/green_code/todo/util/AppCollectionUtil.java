package jp.green_code.todo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppCollectionUtil {

  public static <T> List<T> arrayToList(T[] array) {
    if (array == null) {
      return new ArrayList<>();
    }
    return Arrays.stream(array).toList();
  }

  public static <T> boolean isEmpty(List<T> list) {
    return list == null || list.isEmpty();
  }
}
