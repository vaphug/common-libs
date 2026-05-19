package com.yourdomain.common.database.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Mapper generic dùng provider SQL dynamic để thao tác dữ liệu.
 */
@Mapper
public interface CommonEntityMapper {

    @UpdateProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "insert")
    int insert(@Param("table") String table, @Param("fields") Map<String, Object> fields);

    @UpdateProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "update")
    int update(@Param("table") String table,
               @Param("idColumn") String idColumn,
               @Param("id") Object id,
               @Param("fields") Map<String, Object> fields,
               @Param("modifiedAtColumn") String modifiedAtColumn,
               @Param("expectedModifiedAt") Object expectedModifiedAt);

    @SelectProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "findById")
    Map<String, Object> findById(@Param("table") String table,
                                 @Param("idColumn") String idColumn,
                                 @Param("id") Object id,
                                 @Param("deletedColumn") String deletedColumn,
                                 @Param("includeDeleted") boolean includeDeleted,
                                 @Param("lockClause") String lockClause);

    @UpdateProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "softDelete")
    int softDelete(@Param("table") String table,
                   @Param("idColumn") String idColumn,
                   @Param("id") Object id,
                   @Param("deletedColumn") String deletedColumn,
                   @Param("deletedAtColumn") String deletedAtColumn,
                   @Param("modifiedAtColumn") String modifiedAtColumn,
                   @Param("modifiedUserColumn") String modifiedUserColumn,
                   @Param("actor") String actor);

    @UpdateProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "hardDelete")
    int hardDelete(@Param("table") String table,
                   @Param("idColumn") String idColumn,
                   @Param("id") Object id);

    @UpdateProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "restore")
    int restore(@Param("table") String table,
                @Param("idColumn") String idColumn,
                @Param("id") Object id,
                @Param("deletedColumn") String deletedColumn,
                @Param("deletedAtColumn") String deletedAtColumn,
                @Param("modifiedAtColumn") String modifiedAtColumn,
                @Param("modifiedUserColumn") String modifiedUserColumn,
                @Param("actor") String actor);

    @SelectProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "search")
    List<Map<String, Object>> search(@Param("table") String table,
                                     @Param("idColumn") String idColumn,
                                     @Param("deletedColumn") String deletedColumn,
                                     @Param("filters") Map<String, Object> filters,
                                     @Param("includeDeleted") boolean includeDeleted,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    @UpdateProvider(type = com.yourdomain.common.database.sql.CommonSqlProvider.class, method = "cleanupDeletedBefore")
    int cleanupDeletedBefore(@Param("table") String table,
                             @Param("deletedColumn") String deletedColumn,
                             @Param("deletedAtColumn") String deletedAtColumn,
                             @Param("cutoff") Object cutoff);
}
