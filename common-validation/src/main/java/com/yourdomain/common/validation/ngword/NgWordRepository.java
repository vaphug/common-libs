package com.yourdomain.common.validation.ngword;

import java.util.Collection;

/**
 * Repository contract for loading NG words from persistence.
 *
 * <p>Implementations must use parameterized queries / bind variables.
 */
public interface NgWordRepository {

    /**
     * Load active NG words for checking.
     */
    Collection<String> findActiveNgWords();
}
