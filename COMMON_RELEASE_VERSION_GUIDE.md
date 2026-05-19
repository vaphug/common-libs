# Common Release Version Guide

Tài liệu này định nghĩa chuẩn quản lý `RELEASE_VERSION.md` cho từng module `common-*`.

## Mục tiêu

- Mỗi module `common-*` có một nơi cố định để lưu version logic và lịch sử thay đổi.
- Mỗi lần sửa module, chỉ cần tăng version trong chính module đó và ghi changelog ngắn gọn.
- Người đọc có thể xem nhanh phạm vi source code, thành phần chính, config, và các thay đổi gần nhất của module.

## Tên file bắt buộc

Mỗi module `common-*` phải có file:

```text
<module>/RELEASE_VERSION.md
```

Ví dụ:

```text
common-notification/RELEASE_VERSION.md
common-sqs/RELEASE_VERSION.md
common-webclient/RELEASE_VERSION.md
```

## Cấu trúc bắt buộc

Mỗi `RELEASE_VERSION.md` phải có tối thiểu các phần:

1. `Module`
2. `Current Release Version`
3. `Versioning Rule`
4. `Module Summary`
5. `Source Map`
6. `Resources and Configuration`
7. `Release History`

## Quy tắc tăng version

Dùng semantic version:

- `MAJOR`: thay đổi breaking change, đổi contract public API, đổi behavior cũ không backward compatible
- `MINOR`: thêm feature mới, thêm API mới, thêm config mới nhưng vẫn backward compatible
- `PATCH`: bug fix, refactor không đổi behavior, thêm comment/Javadoc, tối ưu nhỏ, đổi implementation nội bộ

Ví dụ:

- `0.0.1-SNAPSHOT` -> `0.0.2-SNAPSHOT`: fix bug hoặc refactor nhỏ
- `0.0.2-SNAPSHOT` -> `0.1.0-SNAPSHOT`: thêm channel mới, thêm service mới, thêm capability mới
- `0.1.0-SNAPSHOT` -> `1.0.0`: chốt API ổn định hoặc có breaking change lớn trước đó cần reset major rõ ràng

## Khi nào phải cập nhật file

Phải cập nhật `RELEASE_VERSION.md` khi:

- sửa source code trong module
- thêm, xóa, đổi tên class public
- thêm hoặc đổi config/property/resource quan trọng
- thay đổi dependency ảnh hưởng behavior module
- sửa test nhưng đồng thời có thay đổi behavior hoặc contract cần ghi nhận

Không bắt buộc tăng version nếu:

- chỉ format whitespace thuần túy
- chỉ đổi comment nội bộ không ảnh hưởng source map hoặc contract

Lưu ý:

- Nếu có thay đổi code đáng kể nhưng chỉ là refactor không đổi behavior, vẫn nên tăng `PATCH`.
- Nếu đổi guide, dependency, config hoặc tài liệu theo cách ảnh hưởng cách dùng module, vẫn ghi changelog.

## Cách ghi changelog

Mỗi entry release nên ghi:

- `Version`
- `Date`
- `Type`: `init`, `feature`, `fix`, `refactor`, `breaking`, `docs`
- `Summary`
- `Changes`

Template:

```md
## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Chuẩn hóa Javadoc và dọn boilerplate model/config.
- Changes:
- Thêm Lombok cho DTO/config holder.
- Chuẩn hóa Javadoc cho public API.
- Giữ nguyên behavior runtime và cập nhật test liên quan nếu có.
```

## Quy tắc cập nhật source map

- Nếu thêm class public hoặc package quan trọng, cập nhật phần `Source Map`.
- Nếu thêm resource/config file mới, cập nhật phần `Resources and Configuration`.
- Không cần liệt kê mọi private helper nhỏ; ưu tiên các thành phần chính để người đọc nắm cấu trúc module nhanh.

## Prompt ngắn để dùng về sau

Bạn có thể yêu cầu ngắn như sau:

```text
Update RELEASE_VERSION.md cho common-notification theo thay đổi mới.
```

Hoặc:

```text
Refactor common-sqs và nhớ bump release version.
```
