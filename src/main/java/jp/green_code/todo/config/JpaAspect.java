package jp.green_code.todo.config;

import java.util.Arrays;
import jp.green_code.todo.entity.BaseEntity;
import jp.green_code.todo.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * JPA のsave メソッドに一律、createdBy とupdatedBy をセットする
 */
@Aspect
@Component
@Slf4j
public class JpaAspect {

  @Before("execution(* *..*.repository.*JpaRepository.save(..))")
  public void setUpdateBy(JoinPoint jp) {
    Arrays.stream(jp.getArgs())
        .filter(a -> a instanceof BaseEntity)
        .map(a -> (BaseEntity) a)
        .forEach(ThreadLocalUtil::beforeSave);
  }
}
