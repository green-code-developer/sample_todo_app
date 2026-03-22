package jp.green_code.todo.domain.repository;

import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import jp.green_code.todo.domain.repository.base.BaseRepositoryHelper;

@Component
public class RepositoryHelper extends BaseRepositoryHelper {
    public RepositoryHelper(DataSource dataSource) {
        super(dataSource);
    }
}