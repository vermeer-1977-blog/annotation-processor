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
package org.vermeer1977.infrastructure.annotation.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

/**
 * リソースからJavaFileObjectを作成するクラス.
 * {@literal com.google.testing.compile.CompileTester.GeneratedPredicateClause#generatesSources(javax.tools.JavaFileObject, javax.tools.JavaFileObject...) }では
 * ソースコードの定数にUTF-8のマルチバイトが含まれる場合、文字化けする.本クラスではUTF-8を考慮したJavaFileObjectを生成します.<br>
 *
 * @author Yamashita,Takahiro
 */
public class SourceFileReader {

    private String sourceCode;

    /**
     * JavaファイルのURLからソースファイルを取得してUTF-8の文字列として処理できるようにします.
     *
     * @param url 参照元のJavaファイルのURL
     */
    public SourceFileReader(URL url) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            in.lines()
                    .forEachOrdered(line -> {
                        sb.append(line);
                    });
            this.sourceCode = sb.toString();
        } catch (IOException ex) {
            throw new ClassFactoryException("java code could not read." + Arrays.toString(ex.getStackTrace()));
        }
    }

    /**
     * 文字列ソースコードからJavaFileObjectを作成します.
     *
     * @return 文字列から生成したJavaFIleObject
     */
    public JavaFileObject toJavaFileObject() {
        return new SourceFileReader.StringJavaFileObject("", this.sourceCode);
    }

    /**
     * 文字列からJavaFileObjectを作成します.
     */
    private static class StringJavaFileObject extends SimpleJavaFileObject {

        private final String content;

        /**
         * @param className 生成クラスファイル名
         * @param source 生成JavaFileObjectのコード文字列
         */
        public StringJavaFileObject(String className, String source) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = source;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.content;
        }
    }
}
