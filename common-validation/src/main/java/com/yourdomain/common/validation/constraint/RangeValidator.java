package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.Range;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class RangeValidator implements ConstraintValidator<Range, Object> {
    private BigDecimal min;
    private BigDecimal max;

    @Override
    public void initialize(Range constraintAnnotation) {
        this.min = new BigDecimal(constraintAnnotation.min());
        this.max = new BigDecimal(constraintAnnotation.max());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // Null được coi là "không vi phạm range".
        // Nếu bắt buộc nhập, ghép thêm @NotNull/@NotBlank ở field.
        if (value == null) {
            return true;
        }
        BigDecimal number = toBigDecimal(value);
        // Parse không ra số => dữ liệu sai định dạng => fail validation.
        if (number == null) {
            return false;
        }
        // Pass khi nằm trong [min, max] (bao gồm biên).
        return number.compareTo(min) >= 0 && number.compareTo(max) <= 0;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }
        try {
            return new BigDecimal(value.toString().trim());
        } catch (RuntimeException ex) {
            // Trả null để nhánh isValid() phía trên xử lý fail rõ ràng.
            return null;
        }
    }
}
