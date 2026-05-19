package com.yourdomain.common.database.sql;

import static com.yourdomain.common.database.util.SqlIdentifierValidator.safeIdentifier;

import java.util.Map;
import java.util.StringJoiner;

/**
 * SQL provider dynamic cho các thao tác CRUD/recycle-bin.
 */
public class CommonSqlProvider {

    public String insert(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) params.get("fields");

        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");
        for (String key : fields.keySet()) {
            String c = safeIdentifier(key);
            columns.add(c);
            values.add("#{fields." + c + "}");
        }
        return "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
    }

    public String update(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        String idColumn = safeIdentifier((String) params.get("idColumn"));
        String modifiedAtColumn = safeIdentifier((String) params.get("modifiedAtColumn"));
        Object expectedModifiedAt = params.get("expectedModifiedAt");

        @SuppressWarnings("unchecked")
        Map<String, Object> fields = (Map<String, Object>) params.get("fields");
        StringJoiner setClause = new StringJoiner(", ");
        for (String key : fields.keySet()) {
            String c = safeIdentifier(key);
            setClause.add(c + " = #{fields." + c + "}");
        }
        setClause.add(modifiedAtColumn + " = CURRENT_TIMESTAMP");

        StringBuilder sql = new StringBuilder("UPDATE ")
                .append(table)
                .append(" SET ")
                .append(setClause)
                .append(" WHERE ")
                .append(idColumn)
                .append(" = #{id}");

        if (expectedModifiedAt != null) {
            sql.append(" AND ").append(modifiedAtColumn).append(" = #{expectedModifiedAt}");
        }
        return sql.toString();
    }

    public String findById(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        String idColumn = safeIdentifier((String) params.get("idColumn"));
        String deletedColumn = safeIdentifier((String) params.get("deletedColumn"));
        boolean includeDeleted = (boolean) params.get("includeDeleted");
        String lockClause = (String) params.get("lockClause");

        StringBuilder sql = new StringBuilder("SELECT * FROM ")
                .append(table)
                .append(" WHERE ")
                .append(idColumn)
                .append(" = #{id}");

        if (!includeDeleted) {
            sql.append(" AND ").append(deletedColumn).append(" = FALSE");
        }
        if (lockClause != null && !lockClause.isBlank()) {
            sql.append(" ").append(lockClause);
        }
        return sql.toString();
    }

    public String softDelete(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        String idColumn = safeIdentifier((String) params.get("idColumn"));
        String deletedColumn = safeIdentifier((String) params.get("deletedColumn"));
        String deletedAtColumn = safeIdentifier((String) params.get("deletedAtColumn"));
        String modifiedAtColumn = safeIdentifier((String) params.get("modifiedAtColumn"));
        String modifiedUserColumn = safeIdentifier((String) params.get("modifiedUserColumn"));
        return "UPDATE " + table + " SET " + deletedColumn + " = TRUE, " + deletedAtColumn
                + " = CURRENT_TIMESTAMP, " + modifiedAtColumn + " = CURRENT_TIMESTAMP, " + modifiedUserColumn
                + " = #{actor} WHERE " + idColumn + " = #{id}";
    }

    public String hardDelete(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        String idColumn = safeIdentifier((String) params.get("idColumn"));
        return "DELETE FROM " + table + " WHERE " + idColumn + " = #{id}";
    }

    public String restore(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        String idColumn = safeIdentifier((String) params.get("idColumn"));
        String deletedColumn = safeIdentifier((String) params.get("deletedColumn"));
        String deletedAtColumn = safeIdentifier((String) params.get("deletedAtColumn"));
        String modifiedAtColumn = safeIdentifier((String) params.get("modifiedAtColumn"));
        String modifiedUserColumn = safeIdentifier((String) params.get("modifiedUserColumn"));
        return "UPDATE " + table + " SET " + deletedColumn + " = FALSE, " + deletedAtColumn
                + " = NULL, " + modifiedAtColumn + " = CURRENT_TIMESTAMP, " + modifiedUserColumn
                + " = #{actor} WHERE " + idColumn + " = #{id}";
    }

    public String search(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        String idColumn = safeIdentifier((String) params.get("idColumn"));
        String deletedColumn = safeIdentifier((String) params.get("deletedColumn"));
        boolean includeDeleted = (boolean) params.get("includeDeleted");

        @SuppressWarnings("unchecked")
        Map<String, Object> filters = (Map<String, Object>) params.get("filters");

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(table).append(" WHERE 1=1");
        if (!includeDeleted) {
            sql.append(" AND ").append(deletedColumn).append(" = FALSE");
        }
        for (String key : filters.keySet()) {
            String c = safeIdentifier(key);
            sql.append(" AND ").append(c).append(" = #{filters.").append(c).append("}");
        }
        sql.append(" ORDER BY ").append(idColumn).append(" DESC OFFSET #{offset} LIMIT #{limit}");
        return sql.toString();
    }

    public String cleanupDeletedBefore(Map<String, Object> params) {
        String table = safeIdentifier((String) params.get("table"));
        String deletedColumn = safeIdentifier((String) params.get("deletedColumn"));
        String deletedAtColumn = safeIdentifier((String) params.get("deletedAtColumn"));
        return "DELETE FROM " + table + " WHERE " + deletedColumn + " = TRUE AND " + deletedAtColumn
                + " < #{cutoff}";
    }
}
