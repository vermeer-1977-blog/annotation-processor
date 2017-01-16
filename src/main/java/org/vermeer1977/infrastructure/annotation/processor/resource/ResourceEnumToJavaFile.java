/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  Copyright © 2017 Yamashita,Takahiro
 */
package org.vermeer1977.infrastructure.annotation.processor.resource;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;
import lombok.NonNull;

/**
 * ResourceBundleからEnumクラスのJavaFileを生成するクラスです.
 * <P>
 * Control と Localeは任意で指定することが出来ます.<br>
 *
 * @author Yamashita,Takahiro
 */
public class ResourceEnumToJavaFile {

    /**
     * 必須項目の設定
     *
     * @param resourceBaseName リソースバンドルのBaseName（必須）
     * @return builderクラス
     */
    public static Builder of(@NonNull String resourceBaseName) {
        return new ResourceEnumToJavaFile.Builder(resourceBaseName);
    }

    /**
     * builderクラス
     */
    public static class Builder {

        private String packageName;
        private final String resourceBaseName;
        private Control control;
        private Locale locale;

        /**
         * インスタンスを構築します.
         *
         * @param resourceBaseName 参照元のリソース名
         */
        public Builder(@NonNull String resourceBaseName) {
            this.resourceBaseName = resourceBaseName;
        }

        /**
         * ResourceBundleの任意引数であるpackageを設定します.<br>
         *
         * @param packageName パッケージパスの文字列
         * @return chainに使用するbuilderクラス
         */
        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        /**
         * ResourceBundleの任意引数であるCustomControlを設定します
         *
         * @see java.util.ResourceBundle.Control
         * @param control {@link java.util.ResourceBundle.Control}または、そのインターフェースを実装したクラス
         * @return chainに使用するbuilderクラス
         */
        public Builder control(Control control) {
            this.control = control;
            return this;
        }

        /**
         * ロケールを設定します.
         *
         * @param locale ロケール
         * @return chainに使用するbuilderクラス
         */
        public Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        /**
         * ResourceからEnumClassを生成します.主にテストや実行結果確認に使用するメソッドです.
         * <P>
         * AnnotationProcessorで出力するのではなく、個別に出力したい場合に使用することを想定しています.
         * コンソールでの簡易確認にはSystem,out出力結果を、ClassLoaderによる出力は実際に作成されたクラスの検証用に使用することを想定しています.<br>
         *
         * @throws java.net.URISyntaxException JavaFile.writeToの出力先のURI編集時に例外が発生した場合
         * @throws java.io.IOException JavaFile.writeToの出力時に例外が発生した場合
         */
        public void writeTo() throws URISyntaxException, IOException {
            JavaFile javaFile = this.toJavaFile();

            /* コンソールでの簡易確認用にSystem,outに出力する */
            javaFile.writeTo(System.out);

            /* ClassLoaderは実際に作成されたクラスの検証用に作成する. */
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource("");
            Path filer;
            filer = Paths.get(url.toURI());
            javaFile.writeTo(filer);
        }

        /**
         * ResourceBundleのkeyとvalueからJavaFileを作成します.
         *
         * @return 生成したJavaFile
         */
        public JavaFile toJavaFile() {
            String createClassName = this.toClassNameInitCap();
            TypeSpec.Builder typeSpecBuilder = TypeSpec.enumBuilder(createClassName);
            Pattern pattern = Pattern.compile("\\{([0-9])\\}");

            // Enum 定数
            ResourceBundle bundle = this.getBundle();
            bundle.keySet().stream()
                    .sorted(Comparator.comparing(String::toString))
                    .forEachOrdered(key -> {
                        String _value = bundle.getString(key);
                        Matcher matcher = pattern.matcher(_value);
                        int count = 0;
                        while (matcher.find()) {
                            count++;
                        }
                        TypeSpec param = TypeSpec
                                .anonymousClassBuilder("$S, $L, $S", key, count, _value)
                                .addJavadoc(_value + "<br>\n")
                                .addJavadoc("parameter count = $L\n", count)
                                .build();
                        typeSpecBuilder.addEnumConstant(this.toEnumField(key), param);
                    });

            //import的な定義
            ClassName _Locale = ClassName.get("java.util", "Locale");
            ClassName _ResourceBundle = ClassName.get("java.util", "ResourceBundle");
            ClassName _Control = ClassName.get("java.util", "ResourceBundle", "Control");
            ClassName _MessageFormat = ClassName.get("java.text", "MessageFormat");

            //フィールド定義
            FieldSpec fieldKey = FieldSpec.builder(String.class, "key", Modifier.PRIVATE, Modifier.FINAL).build();
            FieldSpec fieldParamCount = FieldSpec.builder(Integer.class, "paramCount", Modifier.PRIVATE, Modifier.FINAL).build();
            FieldSpec fieldValue = FieldSpec.builder(String.class, "value", Modifier.PRIVATE, Modifier.FINAL).build();

            FieldSpec fieldLocale = FieldSpec.builder(_Locale, "locale", Modifier.PRIVATE, Modifier.STATIC).build();
            FieldSpec fieldControl = FieldSpec.builder(_Control, "control", Modifier.PRIVATE, Modifier.STATIC).build();

            typeSpecBuilder
                    // クラスコメント
                    .addJavadoc("Generated by ClassFactoryProcessor.\n")
                    .addJavadoc("@see org.vermeer1977.infrastructure.annotation.processor.ClassFactoryProcessor\n")
                    .addJavadoc("@see org.vermeer1977.infrastructure.annotation.processor.resource\n")
                    // フィールド
                    .addField(fieldKey)
                    .addField(fieldParamCount)
                    .addField(fieldValue)
                    // フィールド（ResourceBundle拡張：スレッドアンセーフ）
                    .addField(fieldControl)
                    .addField(fieldLocale)
                    // コンストラクタ
                    .addMethod(MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PRIVATE)
                            .addParameter(String.class, "key")
                            .addParameter(Integer.class, "paramCount")
                            .addParameter(String.class, "value")
                            .addStatement("this.$N = key", fieldKey)
                            .addStatement("this.$N = paramCount", fieldParamCount)
                            .addStatement("this.$N = value", fieldValue)
                            .build()
                    )
                    .build();

            // メソッド（static）
            ParameterSpec paramControl = ParameterSpec.builder(Control.class, "control").build();
            typeSpecBuilder
                    .addMethod(MethodSpec.methodBuilder("setControl")
                            .addJavadoc("任意設定項目：ResourceBundleから値を取得する際に使用するControlを設定します.<br>\n")
                            .addJavadoc("未設定の場合、ResourceBundleのデフォルトで処理します.<br>\n")
                            .addJavadoc("本設定はロケールのFallbackを設定したい場合などに使用します.<br>")
                            .addJavadoc("Caution:this feild is Thread-Unsafe.\n\n")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(paramControl)
                            .addCode(CodeBlock.builder()
                                    .addStatement("$N.$N = $N", createClassName, fieldControl, paramControl)
                                    .build()
                            )
                            .build()
                    )
                    .build();

            ParameterSpec paramLocale = ParameterSpec.builder(Locale.class, "locale").build();
            typeSpecBuilder
                    .addMethod(MethodSpec.methodBuilder("setLocale")
                            .addJavadoc("任意設定項目：ResourceBundleから値を取得する際に使用するLocaleを設定します.<br>\n")
                            .addJavadoc("未設定の場合、デフォルトロケールで処理します.<br>\n")
                            .addJavadoc("Caution:this feild is Thread-Unsafe.\n\n")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(paramLocale)
                            .addCode(CodeBlock.builder()
                                    .addStatement("$N.$N = $N", createClassName, fieldLocale, paramLocale)
                                    .build()
                            )
                            .build()
                    )
                    .build();

            // メソッド
            typeSpecBuilder
                    .addMethod(MethodSpec.methodBuilder("toString")
                            .addAnnotation(Override.class)
                            .addJavadoc("リソースの値を返却します.<br>\n")
                            .addJavadoc("例外捕捉時の対応については、リソースの取得が出来なかった場合に最低限状況判別が出来うるメッセージを表示させるための措置です.<br>\n")
                            .addJavadoc("あわせてメッセージIDを付与してリソースの取得が出来ていなかったことを可視できるようにしています.<br>\n")
                            .addJavadoc("@return 当該定数に該当するリソースの値\n")
                            .addModifiers(Modifier.PUBLIC)
                            .addCode(CodeBlock.builder()
                                    .beginControlFlow("try")
                                    .addStatement("$1L _locale = $2N.$3N == null ? $1L.getDefault() : $2N.$3N",
                                                  _Locale, createClassName, fieldLocale)
                                    .add(CodeBlock.builder()
                                            .beginControlFlow("if ($N.$N == null)", createClassName, fieldControl)
                                            .addStatement("return $L.getBundle($S, _locale).getString(this.$N)",
                                                          _ResourceBundle, this.resourceBaseName, fieldKey
                                            )
                                            .endControlFlow()
                                            .addStatement("return $L.getBundle($S, _locale, $N.$N).getString(this.$N)",
                                                          _ResourceBundle, this.resourceBaseName, createClassName, fieldControl, fieldKey
                                            )
                                            .build()
                                    )
                                    .nextControlFlow("catch($T ex)", Exception.class)
                                    .addStatement("return this.$N", fieldValue)
                                    .endControlFlow()
                                    .build()
                            )
                            .returns(String.class)
                            .build()
                    )
                    .build();

            ParameterSpec paramMessageParams = ParameterSpec.builder(Object[].class, "params").build();
            typeSpecBuilder
                    .addMethod(MethodSpec.methodBuilder("format")
                            .addJavadoc("埋め込み文字の置換をした文字列を返却します.<br>\n")
                            .addJavadoc("@return 埋め込み文字を置換した文字列\n")
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(paramMessageParams)
                            .varargs(true)
                            .addCode(CodeBlock.builder()
                                    .addStatement("return $L.format(toString(), $N)",
                                                  _MessageFormat, paramMessageParams)
                                    .build()
                            )
                            .returns(String.class)
                            .build()
                    )
                    .build();
            return JavaFile.builder(this.getPackageName(), typeSpecBuilder.build()).build();
        }

        /**
         * ResourceBundleを取得します.
         * <P>
         * @return 取得したResourceBundle
         */
        private ResourceBundle getBundle() {
            if (this.locale == null && this.control == null) {
                return ResourceBundle.getBundle(this.resourceBaseName);
            }
            if (this.locale != null && this.control == null) {
                return ResourceBundle.getBundle(this.resourceBaseName, this.locale);
            }
            if (this.locale == null && this.control != null) {
                return ResourceBundle.getBundle(this.resourceBaseName, this.control);
            }
            return ResourceBundle.getBundle(this.resourceBaseName, this.locale, this.control);
        }

        private String getPackageName() {
            return this.packageName == null ? "" : this.packageName;
        }

        /**
         * リソース名から編集したクラス名を編集します.
         *
         * @return リソース名から始めの１文字を大文字にした文字列
         */
        private String toClassNameInitCap() {
            String[] path = this.resourceBaseName.split("\\.");
            String baseName = path[path.length - 1];
            return baseName.substring(0, 1).toUpperCase(Locale.ENGLISH) + baseName.substring(1);
        }

        /**
         * 全ての文字を大文字に変換したものをEnumのフィールドとして編集します.
         *
         * @return 全てを大文字にした文字列
         */
        private String toEnumField(String key) {
            return key.toUpperCase(Locale.ENGLISH);
        }
    }
}
