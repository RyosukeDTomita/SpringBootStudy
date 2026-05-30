# converter/

## ABOUT

レイヤー間のオブジェクト変換を担当する。

| Converter | 変換内容 |
| --- | --- |
| ShopConverter | ProductEntity → Product（ドメイン）、Product → ProductResponse（DTO）、Order → OrderEntity（永続化） |
| UserConverter | UserEntity → User（ドメイン）、User → UserResponse（DTO） |
| ItemConverter | ItemEntity ↔ Item（ドメイン）、Item → ItemResponse（DTO）、ItemRequest → Item（入力） |
| AuthUserConverter | AuthUserEntity → AuthUser（ドメイン） |

要するに、DAO層(Entity) ↔ ドメイン層 ↔ Controller層(DTO) の変換を MapStruct のアノテーション(@Mapper) で自動生成させている仕組みです。build/ 配下に *Impl.javaが自動生成されており、手書きの変換コードを省略できる。
