package com.yourdomain.common.database.repository;

import com.yourdomain.common.database.domain.LockMode;
import com.yourdomain.common.database.domain.SearchCriteria;
import com.yourdomain.common.database.domain.WriteCommand;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * API repository generic cho common-database.
 */
public interface CommonCrudRepository {

    int insert(WriteCommand command);

    int updateById(Object id, WriteCommand command);

    Optional<Map<String, Object>> findById(Object id, LockMode lockMode, boolean includeDeleted);

    int softDeleteById(Object id);

    int softDeleteById(Object id, String actor);

    int hardDeleteById(Object id);

    int restoreById(Object id);

    int restoreById(Object id, String actor);

    List<Map<String, Object>> search(SearchCriteria criteria);

    List<Map<String, Object>> findAll(int offset, int limit, boolean includeDeleted);

    int cleanupRecycleBinBefore(Object cutoffDateTime);
}
