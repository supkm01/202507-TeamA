# 202507_TeamA
<p align="center">
  <img src="https://img.shields.io/badge/-Java-007396.svg?logo=java&style=for-the-badge">
  <img src="https://img.shields.io/badge/-SpringBoot-6DB33F.svg?logo=springboot&style=for-the-badge&logoColor=white">
  <img src="https://img.shields.io/badge/-Thymeleaf-005F0F.svg?style=for-the-badge">
  <img src="https://img.shields.io/badge/-PostgreSQL-4479A1.svg?logo=mysql&style=for-the-badge&logoColor=white">
  <img src="https://img.shields.io/badge/-GitHub-181717.svg?logo=github&style=for-the-badge">
</p>

---

## 📚 目次

1. [プロジェクト概要](#プロジェクト概要)
2. [使用技術](#使用技術)
3. [画面イメージ](#画面イメージ)
4. [ユースケース図](#ユースケース図)
5. [テーブル設計](#テーブル設計)
6. [URL設計](#url設計)
7. [ディレクトリ構成](#ディレクトリ構成)
8. [工夫した点](#工夫した点)
9. [今後の課題](#今後の課題)
10. [作成者情報](#作成者情報)

---

## 🧩 プロジェクト概要

Spring Bootを使用して開発した商品の販売管理システムです。
管理者が商品の登録・編集・削除・検索もできます。
ユーザーは商品の購入・ができます。

---

## ⚙️ 使用技術

- バックエンド：Java 17 / Spring Boot / JPA
- フロントエンド：HTML / CSS / JavaScript / Thymeleaf
- データベース：PostgreSQL
- ビルド・管理：Maven / GitHub
- IDE：sping-tools-for-eclipse

---

## 🖼 画面イメージ

> スクリーンショットを `images/` フォルダに追加し、以下に貼り付けてください

- ログイン画面
<img width="715" height="461" alt="e8ff7d48a7a5060a2fcbfba7e04e880e" src="https://github.com/user-attachments/assets/d68f7d31-f8ba-46a7-b3fd-b95e01feb232" />
- Lesson一覧画面
<img width="731" height="483" alt="2b7f5942a127de57d5ee537388b2a3bf" src="https://github.com/user-attachments/assets/31d8928b-bf39-499e-ab0c-0d98734a3f4b" />
- Lesson追加フォーム
<img width="680" height="587" alt="59ff95049043b952cb1a467cd2307254" src="https://github.com/user-attachments/assets/c252781a-c2ab-4190-9f45-dbbb94a9126a" />
- Lesson編集フォーム
<img width="610" height="834" alt="8347661eaccc59760d9cd0c59c02abd0" src="https://github.com/user-attachments/assets/45a76dec-6c46-40fd-9d74-8ace63afaadd" />
- Lesson削除フォーム
<img width="657" height="319" alt="97eb7fbe89e3ad1e3eddeab7efbda93d" src="https://github.com/user-attachments/assets/529ad51a-ed01-4a86-8621-06443457b13a" />

---

## 🧭 ユースケース図

- 管理者登録・ログイン・ログアウト
- Lesson作成・編集・削除
<img width="783" height="480" alt="73fd535111dd0e488a04a0a72dac2476" src="https://github.com/user-attachments/assets/aec14e5f-3775-4c51-b04a-054e58012fbe" />




---

## 🗃 テーブル設計

```sql
--adminテーブル
CREATE TABLE IF NOT EXISTS public.admin
(
admin_id BIGINT GENERATED ALWAYS AS IDENTITY,
admin_name character varying COLLATE pg_catalog."default",
admin_email character varying COLLATE pg_catalog."default",
admin_password character varying COLLATE pg_catalog."default",
delete_flg integer,
register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT admin_pkey PRIMARY KEY (admin_id)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.admin
OWNER TO postgres;
```


```sql
--Lessonテーブル
CREATE TABLE IF NOT EXISTS public.lesson
(
lesson_id BIGINT GENERATED ALWAYS AS IDENTITY,
start_date date,
start_time time without time zone,
finish_time time without time zone,
lesson_name character varying COLLATE pg_catalog."default",
lesson_detail character varying COLLATE pg_catalog."default",
lesson_fee integer,
image_name character varying COLLATE pg_catalog."default",
register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
admin_id bigint,
CONSTRAINT lesson_pkey PRIMARY KEY (lesson_id),
CONSTRAINT admin FOREIGN KEY (admin_id)
REFERENCES public.admin (admin_id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION
NOT VALID
);

```
## 🌐 URL設計

- `/admin/register`：管理者登録画面（GET, POST）
- `/admin/login`：管理者ログイン画面（GET, POST）
- `//admin/logout`：管理者ログアウト画面（GET, POST）
- `/account/logout`：ログアウト処理（GET）
- `/admin/lesson/register`：講座情報登録画面（GET, POST）
- `/admin/lesson/image/edit/{lessonId}`：講座画像情報編集画面（GET, POST）
- `/admin/lesson/delete`：講座削除画面（GET, POST）
- `/user/register`：ユーザー登録画面
- `/user/confirm`：ユーザー登録確認機能
- `/user/register/process`：ユーザー登録機能
- `/user/login`：ユーザーログイン画面
- `/user/login/process`：ユーザーログイン機能
- `/user/logout`：ユーザーログアウト
- `/lesson/menu`：商品一覧画面
- `/lesson/detail/{lessonId}`：商品詳細画面
- `/lesson/show/cart`：カート一覧機能
- `/lesson/cart/all`：カート登録機能
- `/lesson/cart/delete/{lessonId}`：カート削除機能
- `/lesson/request`：申し込み手続き機能(お支払い方法選択)
- `/lesson/confirm`：申し込み手続き機能(申し込み内容確認)
- `/lesson/complete`：申し込み手続き機能(申し込み完了)
- `/lesson/mypage`：購入履歴機能
- `/lesson/menu/logout`：ログアウト機能
- `/user/password/reset`：パスワードリセット画面
- `/user/change/password/complete`：パスワードリ変更機能

---

## 📂 ディレクトリ構成

```
src/
├── controller       // 各種コントローラ
├── entity           // JPAエンティティ
├── repository       // データベース操作
├── service          // 業務ロジック
├── templates        // Thymeleafテンプレート
├── static           // CSSやJSなど静的ファイル
└── security         // Spring Security設定
```

---

## 💡 工夫した点

- バックエンドとフロントエンドのデータ送り

---

## 👤 作成者情報

- 氏名：王 志豪、邵旻豪、冉旻雯

<p align="right">(<a href="#top">トップへ</a>)</p>
