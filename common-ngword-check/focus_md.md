# Focus Function

Tài liệu này chỉ tập trung trả lời một câu hỏi:

Từ yêu cầu nghiệp vụ, module `common-ngword-check` cần những function nào để đáp ứng đúng flow mong muốn.

## 1. Yêu cầu gốc cần đáp ứng

Từ nội dung trao đổi, flow mong muốn là:

1. Màn hình nhập text
2. Màn hình validate cơ bản
3. Gọi gRPC check NG word
4. gRPC chuẩn hóa input
5. Sau khi chuẩn hóa, áp dụng whitelist / 表示ゆれ / hira-kana
6. Check với NG word trong DB
7. Trả kết quả `NG` hoặc `OK`

Rule chuẩn hóa đã confirm từ legacy function `FUNC_CNV_CHKWORD`:

1. Chuyển chữ thường -> chữ hoa
2. Chuyển half-width -> full-width
3. Loại bỏ ký hiệu ASCII

Các điểm mở rộng được nhắc trong chat:

1. `whitelist`
2. `表示ゆれ`
3. `hira-kana`
4. `pattern/scope` để validate input

## 2. Function cần có

### 2.1. Hàm validate input

`InputValidationService.validate(String rawInput, String scope)`

- Dùng để: kiểm tra input có hợp lệ trước khi đi vào flow check NG
- Input:
  - `rawInput`: text người dùng nhập, ví dụ `"abc@1"`
  - `scope`: mã màn hình/nghiệp vụ, ví dụ `"default"` hoặc `"member_register"`
- Output:
  - `InputValidationResult`
- Ý nghĩa:
  - Đây là hàm phục vụ phần: `Màn hình sẽ nhập text -> validate ở màn hình`

`PatternRuleProvider.getRules(String scope)`

- Dùng để: trả bộ rule validate tương ứng với từng scope
- Input:
  - `scope`: ví dụ `"default"`
- Output:
  - `List<PatternRule>`
- Ý nghĩa:
  - Nếu không có hàm này thì `validate(...)` không biết phải validate theo rule nào

### 2.2. Hàm common chuẩn hóa input

`NgWordNormalizerService.normalize(String text)`

- Dùng để: chuẩn hóa text trước khi so khớp NG word
- Input:
  - `text`: có thể là input user, NG token trong DB, hoặc whitelist token
  - Ví dụ: `"abc@1"`
- Output:
  - chuỗi đã normalize
  - Ví dụ: `"ＡＢＣ１"`
- Rule tối thiểu phải cover:
  1. lower -> upper
  2. half-width -> full-width
  3. remove ASCII symbols
- Ý nghĩa:
  - Đây là hàm common convert

### 2.3. Hàm cung cấp rule normalize theo scope

`NormalizationProfileProvider.getProfile(String domainOrScreen)`

- Dùng để: quyết định scope hiện tại bật/tắt những bước normalize nào
- Input:
  - `domainOrScreen`: ví dụ `"default"`
- Output:
  - `NormalizationProfile`
- Ý nghĩa:
  - Phục vụ các rule có thể thay đổi theo màn hình/nghiệp vụ

`NormalizationProfileProvider.getNotationVariantMap(String domainOrScreen)`

- Dùng để: trả mapping `表示ゆれ`
- Input:
  - `domainOrScreen`: ví dụ `"default"`
- Output:
  - `Map<String, String>`
- Ví dụ output:
  - `{ "髙":"高", "ヵ":"カ" }`
- Ý nghĩa:
  - **Phục vụ phần `表示ゆれ-các cách viết có cùng nghĩa`**

## 3. Function phục vụ whitelist

`WhitelistService.normalizeWhitelist(Set<String> rawWhitelist)`

- Dùng để: normalize whitelist token về cùng chuẩn với input/NG token
- Input:
  - `{"abc", "a@1"}`
- Output:
  - `{"ＡＢＣ", "Ａ１"}`

`WhitelistService.normalizeWhitelistRules(Collection<WhitelistRule> rules)`

- Dùng để: normalize whitelist rule kiểu `EXACT`
- Input:
  - `{ (EXACT, "abc") }`
- Output:
  - `{ (EXACT, "ＡＢＣ") }`

`WhitelistService.isWhitelisted(String normalizedNgWord, Set<String> normalizedWhitelist, Set<WhitelistRule> normalizedRules)`

- Dùng để: quyết định một NG token đã normalize có được bỏ qua không
- Input:
  - `normalizedNgWord = "ＡＢＣ"`
- Output:
  - `true` hoặc `false`

## 4. Function check NG word

`NgWordCheckService.check(String rawInput, Collection<String> ngWords, Set<String> whitelist, Set<WhitelistRule> whitelistRules)`

- Dùng để:
  1. normalize input
  2. normalize từng NG word
  3. áp whitelist
  4. kiểm tra có match hay không
- Input:
  - `rawInput = "abc@1"`
  - `ngWords = ["ABC"]`
  - `whitelist = {}`
  - `whitelistRules = {}`
- Output:
  - `NgWordCheckResult`
- Ví dụ output:
  - `ng = true`
  - `normalizedInput = "ＡＢＣ１"`
  - `matchedRawNgWord = "ABC"`
  - `matchedNormalizedNgWord = "ＡＢＣ"`
- Ý nghĩa:
  - Đây là phần tương đương ý của `PRC_CHK_STOPWORD`: normalize xong rồi check trong table NG

## 5. Function đọc dữ liệu NG word từ DB

`NgWordRepository.findActiveNgWords()`

- Dùng để: lấy danh sách NG word active từ DB/table
- Output:
  - `List<String>`

`NgWordRepository.findWhitelistTokens(String scope)`

- Dùng để: lấy whitelist token theo scope nếu hệ thống lưu whitelist trong DB

`NgWordRepository.findWhitelistRules(String scope)`

- Dùng để: lấy whitelist rule theo scope nếu hệ thống lưu rule trong DB

`NgWordDbCheckService.checkAgainstDbByScope(String rawInput, String scope)`

- Dùng để: wrapper tiện dụng cho case check NG trực tiếp bằng dữ liệu DB
- Ý nghĩa:
  - gRPC chỉ cần gọi service này nếu muốn đi theo hướng "scope -> load DB data -> check"

## 6. Function điều phối toàn flow

`NgWordCheckOrchestratorService.check(String rawInput, String scope, Set<String> inlineWhitelist, Set<WhitelistRule> inlineRules)`

- Dùng để điều phối toàn bộ flow:
  1. validate input
  2. load whitelist theo scope
  3. merge whitelist request + whitelist DB
  4. check NG
  5. trả trạng thái cuối
- Output:
  - `NgWordCheckOutcome`
- Ý nghĩa:
  - Đây là service phù hợp nhất để gRPC gọi

## 7. Function entrypoint cho gRPC

`NgWordGrpcFacade.checkNgWord(CheckNgWordRequest request)`

- Dùng để: làm entrypoint cho gRPC
- Input:
  - `request.input`
  - `request.scope`
  - `request.inlineWhitelist`
  - `request.inlineWhitelistRules`
- Output:
  - `CheckNgWordResponse`
- Ý nghĩa:
  - Đây là điểm nối đúng với yêu cầu chat: `khi đó gọi gRPC check NG`

## 8. Kết luận ngắn gọn

Nếu bám sát đúng yêu cầu, thì các function quan trọng nhất là:

1. `InputValidationService.validate(...)`
2. `PatternRuleProvider.getRules(...)`
3. `NgWordNormalizerService.normalize(...)`
4. `NormalizationProfileProvider.getProfile(...)`
5. `NormalizationProfileProvider.getNotationVariantMap(...)`
6. `WhitelistService.normalizeWhitelist(...)`
7. `WhitelistService.isWhitelisted(...)`
8. `NgWordCheckService.check(...)`
9. `NgWordRepository.findActiveNgWords()`
10. `NgWordCheckOrchestratorService.check(...)`
11. `NgWordGrpcFacade.checkNgWord(...)`

## 9. SQL Injection có nằm trong common normalize không

Không.

Lý do:

1. Hàm normalize chỉ phục vụ so khớp nghiệp vụ NG word
2. SQL injection phải xử lý ở tầng truy vấn bằng prepared statement / bind parameter
3. Không nên trộn concern security query vào hàm convert/normalize

## 10. Flow ngắn gọn nhất

```text
UI nhập text
-> UI validate cơ bản
-> gRPC gọi NgWordGrpcFacade.checkNgWord(...)
-> Orchestrator gọi InputValidationService.validate(...)
-> Orchestrator gọi NgWordCheckService.check(...)
-> NgWordCheckService dùng NgWordNormalizerService.normalize(...)
-> áp whitelist / 表示ゆれ / hira-kana
-> so khớp với NG word từ DB
-> trả OK hoặc NG
```
