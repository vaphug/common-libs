package com.yourdomain.common.database.repository;

import com.yourdomain.common.database.context.TableMetadata;
import com.yourdomain.common.database.domain.LockMode;
import com.yourdomain.common.database.domain.SearchCriteria;
import com.yourdomain.common.database.domain.WriteCommand;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import com.yourdomain.common.database.mapper.CommonEntityMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement repository generic dựa trên MyBatis.
 */
public class MyBatisCommonCrudRepository implements CommonCrudRepository {

    private static final Logger log = LoggerFactory.getLogger(MyBatisCommonCrudRepository.class);
    private static final String ACTOR_SYSTEM = "system";
    private static final String LOCK_FOR_SHARE = "FOR SHARE";
    private static final String LOCK_FOR_UPDATE = "FOR UPDATE";

    private final CommonEntityMapper mapper;
    private final TableMetadata metadata;
    private final Clock clock;

    public MyBatisCommonCrudRepository(CommonEntityMapper mapper, TableMetadata metadata) {
        this(mapper, metadata, Clock.systemUTC());
    }

    public MyBatisCommonCrudRepository(CommonEntityMapper mapper, TableMetadata metadata, Clock clock) {
        this.mapper = mapper;
        this.metadata = metadata;
        this.clock = clock;
    }

    @Override
    public int insert(WriteCommand command) {
        validateWriteCommand(command);
        LocalDateTime now = LocalDateTime.now(clock);
        Map<String, Object> fields = new HashMap<>(command.fields());
        fields.putIfAbsent(metadata.createdAtColumn(), now);
        fields.putIfAbsent(metadata.createdUserColumn(), command.actor());
        fields.putIfAbsent(metadata.modifiedAtColumn(), now);
        fields.putIfAbsent(metadata.modifiedUserColumn(), command.actor());
        fields.putIfAbsent(metadata.deletedColumn(), false);

        int affected = mapper.insert(metadata.tableName(), fields);
        log.debug("Insert table={} affected={}", metadata.tableName(), affected);
        return affected;
    }

    @Override
    public int updateById(Object id, WriteCommand command) {
        Objects.requireNonNull(id, "id không được null");
        validateWriteCommand(command);
        Map<String, Object> fields = new HashMap<>(command.fields());
        fields.put(metadata.modifiedUserColumn(), command.actor());

        int affected = mapper.update(metadata.tableName(), metadata.idColumn(), id, fields,
                metadata.modifiedAtColumn(), command.expectedModifiedAt());
        log.debug("Update table={} id={} affected={}", metadata.tableName(), id, affected);
        return affected;
    }

    @Override
    public Optional<Map<String, Object>> findById(Object id, LockMode lockMode, boolean includeDeleted) {
        Objects.requireNonNull(id, "id không được null");
        String lockClause = switch (lockMode) {
            case PESSIMISTIC_READ -> LOCK_FOR_SHARE;
            case PESSIMISTIC_WRITE -> LOCK_FOR_UPDATE;
            default -> "";
        };
        return Optional.ofNullable(mapper.findById(
                metadata.tableName(),
                metadata.idColumn(),
                id,
                metadata.deletedColumn(),
                includeDeleted,
                lockClause
        ));
    }

    @Override
    public int softDeleteById(Object id) {
        return softDeleteById(id, ACTOR_SYSTEM);
    }

    @Override
    public int softDeleteById(Object id, String actor) {
        Objects.requireNonNull(id, "id không được null");
        int affected = mapper.softDelete(metadata.tableName(), metadata.idColumn(), id,
                metadata.deletedColumn(), metadata.deletedAtColumn(),
                metadata.modifiedAtColumn(), metadata.modifiedUserColumn(), normalizeActor(actor));
        log.info("Soft delete table={} id={} affected={}", metadata.tableName(), id, affected);
        return affected;
    }

    @Override
    public int hardDeleteById(Object id) {
        Objects.requireNonNull(id, "id không được null");
        int affected = mapper.hardDelete(metadata.tableName(), metadata.idColumn(), id);
        log.warn("Hard delete table={} id={} affected={}", metadata.tableName(), id, affected);
        return affected;
    }

    @Override
    public int restoreById(Object id) {
        return restoreById(id, ACTOR_SYSTEM);
    }

    @Override
    public int restoreById(Object id, String actor) {
        Objects.requireNonNull(id, "id không được null");
        int affected = mapper.restore(metadata.tableName(), metadata.idColumn(), id,
                metadata.deletedColumn(), metadata.deletedAtColumn(),
                metadata.modifiedAtColumn(), metadata.modifiedUserColumn(), normalizeActor(actor));
        log.info("Restore table={} id={} affected={}", metadata.tableName(), id, affected);
        return affected;
    }

    @Override
    public List<Map<String, Object>> search(SearchCriteria criteria) {
        Objects.requireNonNull(criteria, "criteria không được null");
        return mapper.search(
                metadata.tableName(),
                metadata.idColumn(),
                metadata.deletedColumn(),
                criteria.equalsFilters(),
                criteria.includeDeleted(),
                criteria.offset(),
                criteria.limit()
        );
    }

    @Override
    public List<Map<String, Object>> findAll(int offset, int limit, boolean includeDeleted) {
        return mapper.search(
                metadata.tableName(),
                metadata.idColumn(),
                metadata.deletedColumn(),
                Collections.emptyMap(),
                includeDeleted,
                offset,
                limit
        );
    }

    @Override
    public int cleanupRecycleBinBefore(Object cutoffDateTime) {
        Objects.requireNonNull(cutoffDateTime, "cutoffDateTime không được null");
        int affected = mapper.cleanupDeletedBefore(
                metadata.tableName(),
                metadata.deletedColumn(),
                metadata.deletedAtColumn(),
                cutoffDateTime
        );
        log.info("Cleanup recycle-bin table={} affected={}", metadata.tableName(), affected);
        return affected;
    }

    private void validateWriteCommand(WriteCommand command) {
        Objects.requireNonNull(command, "command không được null");
        if (command.fields().isEmpty()) {
            throw new IllegalArgumentException("fields không được rỗng");
        }
    }

    private String normalizeActor(String actor) {
        return actor == null || actor.isBlank() ? ACTOR_SYSTEM : actor.trim();
    }
}
