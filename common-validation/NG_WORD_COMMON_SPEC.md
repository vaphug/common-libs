# NG Word Common Spec (Kiban)

## 1) Scope
Common function cho luồng NG word check:
- Normalize input text
- Check text với NG words từ DB
- Hỗ trợ whitelist
- Hỗ trợ 表示ゆれ (notation variants)
- Hỗ trợ chuyển đổi hira-kana

## 2) Normalize rule (legacy + hội thoại)
Theo `FUNC_CNV_CHKWORD` + confirm từ Kaiin:
1. lower -> UPPER
2. half-width -> full-width
3. remove ASCII symbols

Mở rộng theo yêu cầu hội thoại:
4. hira -> kata
5. normalize ký tự tương thích theo chuẩn Unicode/Shift_JIS compatibility (NFKC)
6. apply notation-variant map (表示ゆれ)

## 3) DB check flow (theo PRC_CHK_STOPWORD)
- Input từ màn hình đã validate sơ bộ
- gRPC gọi common normalize
- Lấy danh sách NG words từ DB (repository)
- Normalize từng NG word và check contains
- Match => NG, không match => OK

## 4) SQL Injection
Không xử lý SQL Injection trong hàm normalize.
Bắt buộc xử lý ở tầng data access:
- PreparedStatement / bind variables
- Không nối chuỗi SQL trực tiếp từ input

## 5) API dùng trong common-validation
- `NgWordNormalizer`
- `LegacyNgWordNormalizer`
- `NgWordCheckService`
- `NgWordRepository`
- `NgWordDbCheckService`
- `NgWordCheckResult`

## 6) Team action còn lại
- Kiban + Kaiin chốt thêm mapping pattern cụ thể nếu khách yêu cầu sâu hơn
- Bổ sung test vector tương ứng theo từng pattern đã chốt
