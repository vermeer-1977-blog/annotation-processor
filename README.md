# annotation-processor
Annotation Processor を使用してクラスを自動生成するプロジェクトです。

## ResourceからEnumを作成する

### 概要
ResourceBundleでPropertiesファイルを参照して、その内容を元にEnumを作成します。

### 詳細
想定ケース：メッセージ定数を管理しているEnumを常に最新化する。
<P>
リソースを変更してコンパイルするだけでプログラムで使用するメッセージEnumクラスも更新するので、リソースとEnumクラスの同期漏れのリスクが軽減します。

### version 0.1.0
