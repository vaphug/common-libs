# common-validation

## Tom tat
Thư viện annotation/validator nghiệp vụ (regex, range, phone, domain rule, ng-word).

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-validation/src/main/java/com/yourdomain/common/validation/constraint/RangeValidator.java:8:public class RangeValidator implements ConstraintValidator<Range, Object> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/RangeValidator.java:13:    public void initialize(Range constraintAnnotation) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/RangeValidator.java:19:    public boolean isValid(Object value, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/NotBlankValidator.java:11:public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/NotBlankValidator.java:13:    public boolean isValid(String value, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/FullWidthValidator.java:10:public class FullWidthValidator implements ConstraintValidator<FullWidth, String> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/FullWidthValidator.java:12:    public boolean isValid(String value, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/ItemValidateValidator.java:15:public class ItemValidateValidator implements ConstraintValidator<ItemValidate, Object> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/ItemValidateValidator.java:21:    public void initialize(ItemValidate constraintAnnotation) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/ItemValidateValidator.java:26:    public boolean isValid(Object value, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/HalfWidthValidator.java:10:public class HalfWidthValidator implements ConstraintValidator<HalfWidth, String> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/HalfWidthValidator.java:14:    public boolean isValid(String value, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/MaxValidator.java:8:public class MaxValidator implements ConstraintValidator<Max, Object> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/MaxValidator.java:12:    public void initialize(Max constraintAnnotation) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/MaxValidator.java:17:    public boolean isValid(Object value, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/config/DomainValidationRepository.java:20:    public static DomainValidationRepository load() {
common-validation/src/main/java/com/yourdomain/common/validation/config/DomainValidationRepository.java:39:    public DomainValidationDefinition find(String itemKey) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/RegexValidator.java:11:public class RegexValidator implements ConstraintValidator<Regex, String> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/RegexValidator.java:17:    public void initialize(Regex constraintAnnotation) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/RegexValidator.java:28:    public boolean isValid(String input, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/RegexMatcher.java:16:    public static boolean isValid(String value, String regex) {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordCheckService.java:7:public class NgWordCheckService {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordCheckService.java:11:    public NgWordCheckService(NgWordNormalizer normalizer) {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordCheckService.java:15:    public NgWordCheckResult check(String input, Collection<String> ngWords) {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordCheckService.java:22:    public NgWordCheckResult check(String input, Collection<String> ngWords, Set<String> whitelist) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/PhoneNumberValidator.java:21:public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/PhoneNumberValidator.java:25:    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/ValidationKind.java:8:public enum ValidationKind {
common-validation/src/main/java/com/yourdomain/common/validation/validator/ValidationKind.java:33:    public ValidationType validationType() {
common-validation/src/main/java/com/yourdomain/common/validation/validator/ValidationKind.java:38:    public static ValidationKind fromJson(String raw) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/MaxLengthConstraint.java:18:    public static MaxLengthConstraint parse(String raw) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/MaxLengthConstraint.java:29:    public boolean isValid(Object value) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:33:    public static boolean validate(Object value, ValidationType type, Map<String, String> params) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:60:    public static boolean isFullWidth(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:72:    public static boolean isHalfWidth(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:79:    public static boolean isFullAndHalfWidth(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:98:    public static boolean isDigitsOnly(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:102:    public static boolean isNumber(String input, boolean allowMinus) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:109:    public static boolean isGreaterThanZeroNumber(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:116:    public static boolean isAmount(String input, boolean allowMinus) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:123:    public static boolean isMinusOnlyAmount(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:130:    public static boolean isAmountGreaterThanZero(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:137:    public static boolean isPositiveNumber(BigDecimal number) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:141:    public static boolean isNegativeNumber(BigDecimal number) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:145:    public static boolean isDecimalNumber(BigDecimal number) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:149:    public static boolean isAllowedMoneyDenomination(BigDecimal number, Map<String, String> params) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:169:    public static boolean isDateYYYY(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:181:    public static boolean isDateYYYYMM(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:196:    public static boolean isDateYYYYMMDD(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:212:    public static boolean isDateTimeYYYYMMDDHHMM(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:230:    public static boolean isEnumInSet(String input, Map<String, String> params) {
common-validation/src/main/java/com/yourdomain/common/validation/validator/DomainValidator.java:243:    public static boolean isRequired(Object value) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/MinValidator.java:8:public class MinValidator implements ConstraintValidator<Min, Object> {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/MinValidator.java:12:    public void initialize(Min constraintAnnotation) {
common-validation/src/main/java/com/yourdomain/common/validation/constraint/MinValidator.java:17:    public boolean isValid(Object value, ConstraintValidatorContext context) {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordCheckResult.java:3:public record NgWordCheckResult(
common-validation/src/main/java/com/yourdomain/common/validation/ngword/LegacyNgWordNormalizer.java:20:public class LegacyNgWordNormalizer implements NgWordNormalizer {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/LegacyNgWordNormalizer.java:44:    public String normalize(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/config/DomainValidationDefinition.java:17:public class DomainValidationDefinition {
common-validation/src/main/java/com/yourdomain/common/validation/config/DomainValidationDefinition.java:26:    public DomainValidationDefinition(
common-validation/src/main/java/com/yourdomain/common/validation/validator/ValidationType.java:3:public enum ValidationType {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordNormalizer.java:3:public interface NgWordNormalizer {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordRepository.java:10:public interface NgWordRepository {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordDbCheckService.java:8:public class NgWordDbCheckService {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordDbCheckService.java:13:    public NgWordDbCheckService(NgWordRepository repository, NgWordCheckService checkService) {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordDbCheckService.java:18:    public NgWordCheckResult check(String input) {
common-validation/src/main/java/com/yourdomain/common/validation/ngword/NgWordDbCheckService.java:22:    public NgWordCheckResult check(String input, Set<String> whitelist) {
```

## 3rd-party API / thu vien lien quan
- Jakarta Bean Validation: https://jakarta.ee/specifications/bean-validation/
- Hibernate Validator (test/runtime integration): https://hibernate.org/validator/

## Module lien quan
- [common-messages](../common-messages)
