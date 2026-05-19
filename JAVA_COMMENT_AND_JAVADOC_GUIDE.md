# Java Comment and Javadoc Guide

Tài liệu này là tiêu chuẩn comment/Javadoc chính thức cho code Java trong repo.
Mọi code Java mới hoặc code được refactor phải follow tài liệu này nếu có thêm hoặc sửa public API.

## Cách kích hoạt guide trong prompt

Mặc định, chỉ cần bạn nhắc tên guide là đủ. Không cần ghi cả câu mô tả dài.

Ví dụ ngắn:

```text
Theo JAVA_COMMENT_AND_JAVADOC_GUIDE, tạo giúp mình module mới.
```

```text
Tạo code mới cho feature này, apply JAVA_COMMENT_AND_JAVADOC_GUIDE.
```

```text
Refactor class này theo JAVA_COMMENT_AND_JAVADOC_GUIDE.
```

Nếu không nhắc lại tên guide, mình không nên tự giả định bắt buộc áp dụng chuẩn này cho mọi request.

Khi muốn ghi rõ hơn, có thể dùng câu lệnh ngắn sau:

```text
Follow JAVA_COMMENT_AND_JAVADOC_GUIDE.md. Write Javadoc and inline comments in Vietnamese, keep technical terms in English where clearer, and follow the B1)/B2) workflow comment style for non-trivial logic.
```

Nếu muốn khóa chuẩn chặt hơn trong prompt:

```text
Follow JAVA_COMMENT_AND_JAVADOC_GUIDE.md as a mandatory standard. Every public/protected class and method must have Javadoc. Every public/protected method must document all @param, @return when non-void, and @throws when exceptions are part of the contract.
```

## Mục tiêu

- Code tự đọc được bằng tên class, method, biến và cấu trúc rõ ràng.
- Javadoc mô tả contract của API/method: làm gì, input/output là gì, lỗi nào có thể xảy ra.
- Comment trong thân hàm giải thích luồng xử lý, quyết định nghiệp vụ, side effect hoặc lý do kỹ thuật khó thấy.
- Boilerplate accessor/constructor được giảm bằng Lombok khi phù hợp.
- Không comment lại điều code đã nói rõ.

## Lombok

Ưu tiên dùng Lombok để giảm boilerplate getter/setter/constructor ở các class dữ liệu hoặc class chỉ mang tính cấu hình.

Áp dụng tốt cho:

- DTO, request, response, model trao đổi dữ liệu
- properties/config holder
- object khởi tạo nhiều field cần `@Builder`

Quy tắc:

- Không viết tay getter/setter đơn giản nếu Lombok xử lý được.
- Ưu tiên `@Getter` và `@Setter` hơn việc generate thủ công accessor lặp lại.
- Dùng `@Builder` cho object có nhiều field optional hoặc cần khởi tạo fluent.
- Dùng `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor` khi constructor chỉ là boilerplate.
- Với object immutable, ưu tiên `final` field kết hợp `@Getter` và `@RequiredArgsConstructor`.
- Chỉ viết tay getter/setter nếu accessor có thêm validation, transform dữ liệu, lazy init hoặc side effect.
- Không lạm dụng `@Data` cho entity/domain object nhạy cảm với `equals`, `hashCode`, `toString`.

Ví dụ:

```java
@Getter
@Setter
public class NotificationSendRequest {
    private String recipient;
    private String subject;
}
```

```java
@Getter
@Builder
@RequiredArgsConstructor
public class NotificationSendResult {
    private final boolean success;
    private final String providerMessageId;
}
```

## Phạm vi áp dụng

Áp dụng cho:

- `public` class, interface, enum, record
- `protected` class hoặc nested type
- mọi method `public` và `protected`
- method `private` nếu có contract riêng, workflow nhiều bước, side effect khó đoán, hoặc logic dễ đọc sai
- field trong DTO/config/model nếu chỉ nhìn tên field không đủ rõ nghĩa

Không bắt buộc cho:

- getter/setter đơn giản, ưu tiên dùng Lombok thay vì viết tay
- constructor đơn giản chỉ gán field, ưu tiên dùng Lombok nếu phù hợp
- test helper ngắn, mapping hiển nhiên, hoặc one-liner private method dễ đọc

## Javadoc cho method

Mọi method `public` và `protected` phải có Javadoc theo tinh thần Google Java Style:

1. Câu đầu tiên tóm tắt method làm gì.
2. Nếu method có nhiều bước nghiệp vụ quan trọng, thêm đoạn mô tả luồng dạng danh sách đánh số.
3. Có dòng trống giữa mô tả và block tag.
4. Khai báo đầy đủ `@param` cho mọi tham số.
5. Khai báo `@return` cho method không phải `void`.
6. Khai báo `@throws` cho exception có thể được throw ra cho caller, bao gồm checked exception và unchecked exception có ý nghĩa contract.
7. Không dùng HTML phức tạp nếu không cần; ưu tiên câu ngắn, rõ.
8. Nếu method có side effect quan trọng như ghi DB, gửi HTTP, xóa message, upload S3, phải nói rõ trong phần mô tả.
9. Nếu method có fallback hoặc default behavior, phải nói rõ ngay trong `@param` hoặc phần mô tả.

Template:

```java
/**
 * Tóm tắt method làm gì.
 *
 * <p>Luồng xử lý:
 * 1) bước nghiệp vụ đầu tiên
 * 2) bước nghiệp vụ tiếp theo
 * 3) kết quả hoặc side effect chính
 *
 * @param request dữ liệu đầu vào dùng để thực hiện nghiệp vụ
 * @param options tùy chọn xử lý; có thể null nếu method hỗ trợ default
 * @return kết quả xử lý trả về cho caller
 * @throws IllegalArgumentException khi request không hợp lệ
 * @throws IllegalStateException khi trạng thái hệ thống không cho phép xử lý
 */
```

Với method đơn giản, không cần phần "Luồng xử lý":

```java
/**
 * Lấy cấu hình hiệu lực, ưu tiên giá trị caller truyền vào trước default.
 *
 * @param options tùy chọn caller truyền vào; có thể null
 * @return cấu hình đã được merge đầy đủ default
 */
```

## Javadoc cho auto-configuration và bean factory method

Với class Spring auto-config và method `@Bean`, bắt buộc mô tả:

- class tạo ra module gì
- bean đó dùng để làm gì
- điều kiện tạo bean mặc định nếu có

Template:

```java
/**
 * Auto-configuration cho module common-s3file.
 *
 * <p>Class này khởi tạo các bean AWS S3 mặc định khi ứng dụng chưa tự cung cấp
 * S3Client, S3Presigner, hoặc CommonS3FileService.
 */
public class CommonS3FileAutoConfiguration {

    /**
     * Tạo S3 client mặc định cho các thao tác đồng bộ với S3.
     *
     * @param properties cấu hình common.s3file đã bind từ application properties
     * @return S3Client sẵn sàng để upload, download, head object, và delete object
     */
    @Bean
    public S3Client s3Client(CommonS3FileProperties properties) {
        ...
    }
}
```

## Javadoc cho class/interface

Class hoặc interface public phải có Javadoc ngắn gọn:

- Vai trò chính của class/interface.
- Dependency hoặc side effect quan trọng nếu có.
- Quy tắc sử dụng quan trọng, ví dụ thread-safe, retry, idempotency, transaction.

Ví dụ:

```java
/**
 * Service dùng chung để gửi, nhận và xử lý message từ SQS.
 *
 * <p>Service hỗ trợ idempotency, heartbeat visibility timeout và retry thông qua cơ chế
 * native của SQS. Handler nghiệp vụ chỉ được gọi sau khi message chiếm được idempotency lock.
 */
```

## Javadoc cho DTO, config, model, enum

Với DTO/config/model không cần Javadoc cho từng getter/setter.
Thay vào đó:

- class có Javadoc 1-3 câu
- field có comment ngắn nếu nghĩa nghiệp vụ không hoàn toàn hiển nhiên
- enum value có comment khi tên enum chưa đủ để hiểu usage

Ví dụ:

```java
/**
 * Request chuẩn hóa cho mọi channel gửi notification.
 *
 * <p>Các channel cụ thể có thể dùng toàn bộ field chung này hoặc chỉ dùng một phần.
 */
public class NotificationSendRequest {

    /** Đích nhận notification, ví dụ email, phone number, device token, hoặc LINE user ID. */
    private String recipient;

    /** Payload nghiệp vụ bổ sung cần merge vào request body của provider. */
    private Map<String, Object> payload = new LinkedHashMap<>();
}
```

## Comment trong thân hàm

Chỉ thêm comment trong thân hàm khi nó giúp hiểu nhanh luồng xử lý hoặc lý do kỹ thuật. Ưu tiên style:

```java
// B1) Chuẩn hóa options để mọi nhánh bên dưới đều dùng một bộ tham số hợp lệ.
// B2) Poll message từ queue với long polling và visibility timeout đã cấu hình.
// B3) Tính idempotency key trước khi gọi handler để chống xử lý trùng.
```

Quy tắc:

- Dùng prefix `B1)`, `B2)`, `B3)` cho method có workflow nhiều bước.
- Dùng `B4a)`, `B4b)` cho các nhánh cùng một bước logic.
- Comment phải giải thích mục đích hoặc hệ quả, không chỉ diễn lại tên method.
- Comment đặt ngay trước block code liên quan.
- Không comment cho getter/setter, mapping đơn giản, return đơn giản hoặc logic hiển nhiên.
- Nếu comment cần quá dài, cân nhắc tách method nhỏ với tên rõ hơn.
- Chỉ dùng comment nhiều bước ở method thực sự có workflow; không ép mọi method phải có `B1)`, `B2)`.
- Với nhánh quan trọng, có thể dùng `B4a)`, `B4b)` để phân biệt decision branch cùng một bước.

Nên viết:

```java
// B4b) Message đang được worker khác xử lý thì dời visibility ngắn và nhường lượt hiện tại.
if (start == MessageIdempotencyStore.StartResult.ALREADY_IN_PROGRESS) {
    changeMessageVisibility(queueName, message.receiptHandle(),
            Math.max(1, effective.getHeartbeatIntervalSeconds()));
    continue;
}
```

Không nên viết:

```java
// Gọi changeMessageVisibility.
changeMessageVisibility(queueName, message.receiptHandle(), timeout);
```

## Khi nào phải có @throws

Phải có `@throws` nếu:

- method chủ động validate input và ném `IllegalArgumentException`
- method phụ thuộc trạng thái runtime và ném `IllegalStateException` như một phần contract
- method khai báo checked exception
- caller cần biết exception đó để dùng API đúng

Không bắt buộc ghi `@throws` nếu:

- exception chỉ là implementation detail nội bộ và không phải contract public
- method private rất nhỏ, dễ hiểu, và exception không cần được xem là API contract

## Quy tắc viết câu

- Câu đầu của Javadoc bắt đầu bằng động từ hoặc mô tả trực tiếp chức năng.
- Mỗi `@param` phải nói rõ ý nghĩa tham số, không chỉ lặp lại tên biến.
- `@return` mô tả caller nhận được gì, không viết kiểu "return result".
- `@throws` mô tả điều kiện phát sinh lỗi, không chỉ ghi "when error occurs".
- Ưu tiên câu ngắn 1 dòng đến 2 dòng; xuống dòng khi block tag dài.

## Ngôn ngữ

- Với repo này, Javadoc và comment giải thích code viết bằng tiếng Việt.
- Giữ nguyên thuật ngữ kỹ thuật phổ biến bằng tiếng Anh nếu dịch ra làm khó hiểu hơn: `handler`, `retry`, `timeout`, `visibility`, `idempotency`, `lock`, `default`, `worker`.
- Câu ngắn, trực tiếp, tránh diễn giải lan man.

## Mức độ chi tiết

Áp dụng theo độ phức tạp:

- Method public/protected đơn giản: Javadoc đầy đủ `@param`/`@return`/`@throws`, không cần comment trong body.
- Method có workflow nhiều bước: Javadoc mô tả luồng tổng quan, body có comment `B1)`, `B2)` tại các điểm quan trọng.
- Method private đơn giản: không bắt buộc Javadoc; chỉ comment nếu có logic nghiệp vụ hoặc kỹ thuật dễ hiểu sai.
- Method private phức tạp hoặc có contract riêng: thêm Javadoc như method public.
- DTO/config/model: comment ở class và field, không lặp comment ở getter/setter trừ khi getter/setter có behavior riêng.
- Enum: comment ở class, và comment từng enum value nếu tên enum chưa đủ giải thích intent.

## Ví dụ chuẩn

```java
/**
 * Luồng xử lý message an toàn:
 * 1) receive
 * 2) check idempotency
 * 3) start heartbeat extend visibility
 * 4) gọi handler business
 * 5) success -> delete + mark success
 * 6) fail -> mark failed, không delete để SQS retry hoặc DLQ
 *
 * @param queueName tên queue cần poll
 * @param options tùy chọn xử lý; có thể null để dùng default
 * @param handler handler business cho từng message
 * @return số message xử lý thành công trong lượt gọi này
 * @throws IllegalArgumentException khi queueName hoặc handler không hợp lệ
 */
public int processMessages(String queueName, ProcessMessageOptions options, SqsMessageHandler handler) {
    // B1) Chuẩn hóa options để mọi nhánh bên dưới đều dùng một bộ tham số đầy đủ và hợp lệ.
    ProcessMessageOptions effective = mergeOptions(options);

    // B2) Poll message từ queue với long polling và visibility timeout đã được hợp nhất.
    List<Message> messages = receiveMessages(
            queueName,
            effective.getMaxMessages(),
            effective.getWaitTimeSeconds(),
            effective.getVisibilityTimeoutSeconds());

    if (messages.isEmpty()) {
        return 0;
    }

    int processed = 0;
    for (Message message : new ArrayList<>(messages)) {
        // B3) Tính idempotency key trước khi gọi handler để chống xử lý trùng giữa các lần poll.
        String key = extractIdempotencyKey(effective, message);

        // Business logic tiếp theo...
    }

    return processed;
}
```

## Mẫu chuẩn nên follow theo loại source

### 1. Service public

- class-level Javadoc
- Javadoc đầy đủ cho mọi method public
- comment `B1)`, `B2)` cho workflow phức tạp

### 2. Spring AutoConfiguration

- class-level Javadoc
- Javadoc cho từng `@Bean`
- nói rõ bean tạo ra để làm gì và `@param` là dependency gì

### 3. Repository interface

- class-level Javadoc
- mỗi method public có Javadoc ngắn, tập trung vào dữ liệu được ghi/đọc/cập nhật

### 4. DTO / Config / Model

- class-level Javadoc
- comment cho field quan trọng
- không cần Javadoc cho getter/setter đơn giản

### 5. Private helper

- chỉ thêm Javadoc nếu helper có contract riêng hoặc logic không hiển nhiên
- nếu helper chỉ validate hoặc transform ngắn, có thể không cần Javadoc

## Anti-pattern cần tránh

- Comment chỉ lặp lại tên method: `// Call sendMessage.`
- Javadoc rỗng nghĩa: `@param queueName queue name`
- Comment quá sát từng dòng khiến source bị nhiễu
- Viết comment sai lệch với logic hiện tại sau khi refactor
- Dùng tiếng Việt dài dòng, mô tả lan man thay vì chốt contract
- Comment getter/setter đơn giản ở mọi nơi làm source nặng và khó scan

## Checklist trước khi commit

- Method public/protected đã có Javadoc.
- `@param` khớp đúng toàn bộ tham số và tên tham số hiện tại.
- Method có return value đã có `@return`.
- Exception là một phần contract đã có `@throws`.
- Comment trong body giải thích lý do hoặc luồng xử lý, không lặp lại code.
- Comment workflow dùng thứ tự `B1)`, `B2)`, `B3)` nhất quán.
- Không có comment sai lệch sau khi đổi logic.
- DTO/config/model đã có class-level Javadoc hoặc field comment đủ rõ để người đọc hiểu intent.
- Auto-config và `@Bean` factory method đã có Javadoc mô tả bean contract.
