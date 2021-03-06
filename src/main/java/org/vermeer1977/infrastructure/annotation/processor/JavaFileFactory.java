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

import com.squareup.javapoet.JavaFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Processorで取得した情報からJavaFileを作成するFactoryクラスです.
 *
 * @author Yamashita,Takahiro
 */
public class JavaFileFactory {

    private final ClassFactoryManager classFactoryManager;

    private JavaFileFactory(ClassFactoryManager classFactoryManager) {
        this.classFactoryManager = classFactoryManager;
    }

    /**
     * 指定したクラス生成Factory管理クラスからインスタンスを構築します.
     *
     * @param classFactoryManager AnnotationProcessorでクラスを生成するFactoryのFirstClassCollection管理クラス（必須）
     * @return 構築したインスタンス
     * @throws ClassFactoryException 必須項目が未設定の場合
     */
    public static JavaFileFactory of(ClassFactoryManager classFactoryManager) {
        if (classFactoryManager == null) {
            throw new ClassFactoryException("JavaFileFactory must set classFactoryManager.");
        }
        return new JavaFileFactory(classFactoryManager);
    }

    /**
     * Processorで取得した情報からJavaFileを作成する.
     *
     * @param annotations processで取得したannotations
     * @param roundEnv processで取得したRoundEnvironment
     * @return 生成したJavaFileリスト
     */
    public List<JavaFile> create(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<JavaFile> javaFiles = new ArrayList<>();

        for (TypeElement typeElement : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                List<JavaFile> elementJavaFiles = this.classFactoryManager.getClassFactories().stream()
                        .filter(target -> target.filter(element))
                        .map(target -> target.toJavaFiles(element))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                javaFiles.addAll(elementJavaFiles);
            }
        }
        return javaFiles;
    }
}
