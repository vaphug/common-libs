# Validation Matrix (Common Validation)

## 1. Mục tiêu
Tài liệu này là chuẩn dùng annotation validation cho toàn bộ service sử dụng `common-validation`.
Mục tiêu là tránh dùng sai annotation theo kiểu dữ liệu (ví dụ `@Regex` trên `Integer`) gây `UnexpectedTypeException`.

## 2. Quy ước chung
- `message`: mã lỗi nghiệp vụ (ví dụ `M01`) hoặc message key i18n (`{validation.xxx}`).
- `value`: cấu hình chính của annotation (pattern, format, ngưỡng, ...).
- `param`: tham số phụ dạng chuỗi, ưu tiên format `csv` hoặc `key=value;key2=value2`.
- Null handling mặc định: hầu hết validator trả `true` khi giá trị `null`; dùng thêm `@NotBlank`/`@NotNull` nếu field bắt buộc.

## 3. Matrix hiện tại
| Annotation | Supported Type | Thuộc tính chính | Ví dụ | Ghi chú |
|---|---|---|---|---|
| `@NotBlank` | `String` | `message` | `@NotBlank(message="M_REQUIRED")` | Không dùng cho số |
| `@HalfWidth` | `String` | `message` | `@HalfWidth(message="M_HALF")` | Chỉ half-width ASCII |
| `@FullWidth` | `String` | `message` | `@FullWidth(message="M_FULL")` | Chỉ full-width |
| `@Regex` | `String` | `pattern` hoặc `value`, `param`, `message` | `@Regex(pattern="^[0-9]+$")` | Không dùng cho `Integer`/`Long` trực tiếp |
| `@Regex` (range date) | `String` | `value="yyyyMMdd"`, `param="start,end"` | `@Regex(message="M01", value="yyyyMMdd", param="20261010,20201030")` | Có normalize nếu start > end |
| `@Min` | `Number`, `String` parse được số | `value`, `message` | `@Min(value="1", message="M_MIN")` | Dùng cho field số |
| `@Max` | `Number`, `String` parse được số | `value`, `message` | `@Max(value="999", message="M_MAX")` | Dùng cho field số |
| `@Range` | `Number`, `String` parse được số | `min`, `max`, `message` | `@Range(min="1", max="100", message="M_RANGE")` | Dùng cho khoảng số |
| `@ItemValidate` | `Object` | `item`, `message` | `@ItemValidate(item="orderNo")` | Validate theo cấu hình domain JSON |
| `@ValidPhoneNumber` | `String` | `message` | `@ValidPhoneNumber(message="M_PHONE")` | Rule theo validator hiện tại |

## 4. Rule bắt buộc cho tầng nghiệp vụ/service
1. Field kiểu số (`Integer`, `Long`, `BigDecimal`) không dùng `@Regex`, `@NotBlank`, `@HalfWidth`, `@FullWidth`.
2. Field kiểu chuỗi mới dùng `@Regex`/`@HalfWidth`/`@FullWidth`.
3. Nếu field bắt buộc, luôn ghép với validator required tương ứng (`@NotBlank` cho String, `@NotNull` cho Number/Object nếu có).
4. Không trộn quá nhiều annotation trùng mục đích trên 1 field (ví dụ vừa `@Range` vừa `@Min/@Max` nếu không có lý do rõ ràng).

## 5. Mẫu dùng khuyến nghị
### 5.1 String date range
```java
@Regex(message = "M01", value = "yyyyMMdd", param = "20261010,20201030")
private String raceDate;
```

### 5.2 Integer range
```java
@Min(value = "1", message = "M02")
@Max(value = "999999", message = "M03")
private Integer orderNo;
```

### 5.3 Numeric range gọn
```java
@Range(min = "1", max = "100", message = "M_RANGE")
private Integer rank;
```

## 6. Đề xuất mở rộng tiếp theo
- Thêm `@DateRange(format="yyyyMMdd", param="start,end")` tách riêng khỏi `@Regex` để semantic rõ hơn.
- Thêm `@ArrayIn` / `@MatchingArray` cho rule tập giá trị hợp lệ.
- Thêm unit test "misuse" để đảm bảo dùng sai type trả lỗi rõ ràng.
