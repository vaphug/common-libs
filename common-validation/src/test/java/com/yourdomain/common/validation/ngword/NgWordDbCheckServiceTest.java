package com.yourdomain.common.validation.ngword;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NgWordDbCheckServiceTest {

    @Test
    void check_shouldUseRepositoryWords() {
        NgWordRepository repo = () -> List.of("ＡＢＣ");
        NgWordDbCheckService service = new NgWordDbCheckService(
                repo,
                new NgWordCheckService(new LegacyNgWordNormalizer())
        );

        assertTrue(service.check("ab@c").blocked());
    }

    @Test
    void check_shouldRespectWhitelist() {
        NgWordRepository repo = () -> List.of("ＡＢＣ");
        NgWordDbCheckService service = new NgWordDbCheckService(
                repo,
                new NgWordCheckService(new LegacyNgWordNormalizer())
        );

        assertFalse(service.check("ab@c", Set.of("abc")).blocked());
    }
}
