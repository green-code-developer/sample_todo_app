package jp.green_code.todo.repository;

import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import jp.green_code.todo.repository.base.BaseRepositoryHelper;

@Component
public class RepositoryHelper extends BaseRepositoryHelper {
    public RepositoryHelper(DataSource dataSource) {
        super(dataSource);
    }
}