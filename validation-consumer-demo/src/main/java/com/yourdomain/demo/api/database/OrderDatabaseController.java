package com.yourdomain.demo.api.database;

import com.yourdomain.common.database.domain.LockMode;
import com.yourdomain.common.database.domain.SearchCriteria;
import com.yourdomain.common.database.domain.WriteCommand;
import com.yourdomain.common.database.service.CommonDatabaseService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller demo để test trực tiếp common-database bằng Postman.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderDatabaseController {

    private final CommonDatabaseService databaseService;

    public OrderDatabaseController(CommonDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody OrderCommandRequest request) {
        int affected = databaseService.insert(new WriteCommand(
                request.fields(),
                null,
                request.actor()
        ));
        return ResponseEntity.ok(Map.of("affected", affected));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestBody OrderCommandRequest request
    ) {
        LocalDateTime expectedModifiedAt = request.expectedModifiedAt() == null || request.expectedModifiedAt().isBlank()
                ? null
                : LocalDateTime.parse(request.expectedModifiedAt());

        int affected = databaseService.updateById(id, new WriteCommand(
                request.fields(),
                expectedModifiedAt,
                request.actor()
        ));
        return ResponseEntity.ok(Map.of("affected", affected));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Map<String, Object>>> findById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "NONE") LockMode lockMode,
            @RequestParam(defaultValue = "false") boolean includeDeleted
    ) {
        return ResponseEntity.ok(databaseService.findById(id, lockMode, includeDeleted));
    }

    /**
     * Endpoint giữ read lock trong transaction để test concurrent lock.
     */
    @GetMapping("/{id}/lock/share")
    @Transactional
    public ResponseEntity<Map<String, Object>> holdShareLock(
            @PathVariable Long id,
            @RequestParam(defaultValue = "15") int holdSeconds,
            @RequestParam(defaultValue = "false") boolean includeDeleted
    ) throws InterruptedException {
        databaseService.findByIdForShare(id, includeDeleted);
        Thread.sleep(Math.max(0, holdSeconds) * 1000L);
        return ResponseEntity.ok(Map.of("lock", "PESSIMISTIC_READ", "id", id, "holdSeconds", holdSeconds));
    }

    /**
     * Endpoint giữ write lock trong transaction để test concurrent lock.
     */
    @GetMapping("/{id}/lock/update")
    @Transactional
    public ResponseEntity<Map<String, Object>> holdUpdateLock(
            @PathVariable Long id,
            @RequestParam(defaultValue = "15") int holdSeconds,
            @RequestParam(defaultValue = "false") boolean includeDeleted
    ) throws InterruptedException {
        databaseService.findByIdForUpdate(id, includeDeleted);
        Thread.sleep(Math.max(0, holdSeconds) * 1000L);
        return ResponseEntity.ok(Map.of("lock", "PESSIMISTIC_WRITE", "id", id, "holdSeconds", holdSeconds));
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Map<String, Object>> softDelete(@PathVariable Long id, @RequestBody(required = false) OrderActionRequest request) {
        int affected = databaseService.softDelete(id, request == null ? "system" : request.actor());
        return ResponseEntity.ok(Map.of("affected", affected));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Map<String, Object>> hardDelete(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("affected", databaseService.hardDelete(id)));
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restore(@PathVariable Long id, @RequestBody(required = false) OrderActionRequest request) {
        int affected = databaseService.restore(id, request == null ? "system" : request.actor());
        return ResponseEntity.ok(Map.of("affected", affected));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(@RequestBody OrderSearchRequest request) {
        SearchCriteria criteria = new SearchCriteria(
                request.equalsFilters(),
                request.offset(),
                request.limit(),
                request.includeDeleted()
        );
        return ResponseEntity.ok(databaseService.search(criteria));
    }

    @PostMapping("/recycle-bin/cleanup")
    public ResponseEntity<Map<String, Object>> cleanup(@RequestBody RecycleBinCleanupRequest request) {
        LocalDateTime cutoff = LocalDateTime.parse(request.cutoff());
        int affected = databaseService.cleanupBefore(cutoff);
        return ResponseEntity.ok(Map.of("affected", affected));
    }
}
