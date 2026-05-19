package com.yourdomain.common.database.service;

import com.yourdomain.common.database.domain.LockMode;
import com.yourdomain.common.database.domain.SearchCriteria;
import com.yourdomain.common.database.domain.WriteCommand;
import com.yourdomain.common.database.repository.CommonCrudRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service facade mức common để các module khác tái sử dụng.
 */
public class CommonDatabaseService {

    private final CommonCrudRepository repository;

    public CommonDatabaseService(CommonCrudRepository repository) {
        this.repository = repository;
    }

    /**
     * Ghi mới bản ghi.
     */
    public int insert(WriteCommand command) {
        return repository.insert(command);
    }

    /**
     * Cập nhật bản ghi theo id, hỗ trợ optimistic lock nếu truyền expectedModifiedAt.
     */
    public int updateById(Object id, WriteCommand command) {
        return repository.updateById(id, command);
    }

    /**
     * Tìm theo id với lock mode tuỳ chọn (none/pessimistic).
     */
    public Optional<Map<String, Object>> findById(Object id, LockMode lockMode, boolean includeDeleted) {
        return repository.findById(id, lockMode, includeDeleted);
    }

    /**
     * Tìm theo id với pessimistic read lock (FOR SHARE).
     */
    public Optional<Map<String, Object>> findByIdForShare(Object id, boolean includeDeleted) {
        return repository.findById(id, LockMode.PESSIMISTIC_READ, includeDeleted);
    }

    /**
     * Tìm theo id với pessimistic write lock (FOR UPDATE).
     */
    public Optional<Map<String, Object>> findByIdForUpdate(Object id, boolean includeDeleted) {
        return repository.findById(id, LockMode.PESSIMISTIC_WRITE, includeDeleted);
    }

    /**
     * Xoá mềm (đưa vào recycle-bin).
     */
    public int softDelete(Object id) {
        return repository.softDeleteById(id);
    }

    public int softDelete(Object id, String actor) {
        return repository.softDeleteById(id, actor);
    }

    /**
     * Xoá cứng khỏi database.
     */
    public int hardDelete(Object id) {
        return repository.hardDeleteById(id);
    }

    /**
     * Khôi phục dữ liệu từ recycle-bin.
     */
    public int restore(Object id) {
        return repository.restoreById(id);
    }

    public int restore(Object id, String actor) {
        return repository.restoreById(id, actor);
    }

    /**
     * Search dynamic (bao gồm cả recycle-bin nếu includeDeleted=true).
     */
    public List<Map<String, Object>> search(SearchCriteria criteria) {
        return repository.search(criteria);
    }

    /**
     * Lấy toàn bộ bản ghi theo phân trang, có thể bao gồm dữ liệu đã xoá mềm.
     */
    public List<Map<String, Object>> findAll(int offset, int limit, boolean includeDeleted) {
        return repository.findAll(offset, limit, includeDeleted);
    }

    /**
     * Dọn dữ liệu recycle-bin trước mốc thời gian chỉ định.
     */
    public int cleanupBefore(LocalDateTime cutoff) {
        return repository.cleanupRecycleBinBefore(cutoff);
    }
}
