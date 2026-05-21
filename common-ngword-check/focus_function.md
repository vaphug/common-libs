# Focus Function

Mục tiêu: chỉ liệt kê function cốt lõi để đáp ứng đúng yêu cầu chat, không lan man.

Lưu ý: phần validate input đã dùng `common-validation`, nên tài liệu này chỉ tập trung vào `common-ngword-check`.

## 1) Function bắt buộc trong `common-ngword-check`

`NgWordNormalizerService.normalize(String text)`
- Dùng để chuẩn hóa chuỗi theo rule đã confirm từ legacy:
1. lower -> upper
2. half-width -> full-width
3. remove ASCII symbols
- Cover thêm `表示ゆれ` và `hira/kana` theo profile.

`NormalizationProfileProvider.getProfile(String domainOrScreen)`
- Trả profile bật/tắt rule normalize theo scope.

`NormalizationProfileProvider.getNotationVariantMap(String domainOrScreen)`
- Trả mapping `表示ゆれ` theo scope.

`WhitelistService.normalizeWhitelist(Set<String> rawWhitelist)`
- Chuẩn hóa whitelist token về cùng chuẩn normalize.

`WhitelistService.normalizeWhitelistRules(Collection<WhitelistRule> rules)`
- Chuẩn hóa whitelist rules (đặc biệt mode `EXACT`).

`WhitelistService.isWhitelisted(String normalizedNgWord, Set<String> normalizedWhitelist, Set<WhitelistRule> normalizedRules)`
- Quyết định NG token đã normalize có được whitelist bỏ qua hay không.

`NgWordCheckService.check(String rawInput, Collection<String> ngWords, Set<String> whitelist, Set<WhitelistRule> whitelistRules)`
- So khớp NG word sau normalize + whitelist.
- Trả `NgWordCheckResult` (NG/OK + token match).

`NgWordRepository.findActiveNgWords()`
- Lấy danh sách NG word active từ DB/table.

`NgWordRepository.findWhitelistTokens(String scope)`
- Lấy whitelist token theo scope (nếu có).

`NgWordRepository.findWhitelistRules(String scope)`
- Lấy whitelist rule theo scope (nếu có).

`NgWordDbCheckService.checkAgainstDbByScope(String rawInput, String scope)`
- Wrapper check NG trực tiếp theo scope bằng dữ liệu DB.

`NgWordCheckOrchestratorService.check(String rawInput, String scope, Set<String> inlineWhitelist, Set<WhitelistRule> inlineRules)`
- Điều phối flow trong module: merge whitelist + check NG + trả outcome.

`NgWordGrpcFacade.checkNgWord(CheckNgWordRequest request)`
- Entrypoint cho gRPC layer.

## 2) Cái gì không nằm trong file này

`InputValidationService.validate(...)` và pattern rules:
- Đã giao cho `common-validation` theo đúng định hướng mới.
- Không coi là focus của `common-ngword-check`.

Tóm tắt gợi ý dùng `common-validation`:
- `@NotBlank`: bắt buộc có dữ liệu đầu vào.
- `@Regex`: ràng buộc pattern theo nghiệp vụ.
- `@Min`, `@Max`, `@Range`: ràng buộc độ dài/giá trị nếu cần.
- `@ItemValidate`: dùng khi validate theo rule domain cấu hình.

## 3) Flow ngắn gọn

```text
UI nhập text
-> UI validate bằng common-validation
-> gRPC gọi NgWordGrpcFacade.checkNgWord(...)
-> normalize + whitelist + check NG word DB
-> trả OK hoặc NG
```

## 4) SQL Injection

Không xử lý SQL injection trong normalize function.
SQL injection phải xử lý ở tầng query bằng prepared statement / bind parameter.
