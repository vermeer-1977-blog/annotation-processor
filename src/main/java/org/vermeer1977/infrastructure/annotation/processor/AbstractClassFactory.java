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
import java.lang.annotation.Annotation;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * AnnotationProcessorによるClass生成の基底となる抽象クラス.
 *
 * @author Yamashita,Takahiro
 */
public abstract class AbstractClassFactory {

    private final Class<? extends Annotation> targetAnnotaion;
    private Messager messager;

    /**
     * 生成マーカーとなるAnnotationInterfaceを指定する
     *
     * @param <T> AnnotaionInterface
     * @param targetAnnotaion 生成マーカーとなるAnnotationInterface
     */
    public <T extends Annotation> AbstractClassFactory(Class<T> targetAnnotaion) {
        this.targetAnnotaion = targetAnnotaion;
    }

    /**
     * AnnotationProcessorの処理時に出力するメッセージ用の{@link javax.annotation.processing.Messager}.
     *
     * @param messager
     */
    public void setMessager(Messager messager) {
        this.messager = messager;
    }

    /**
     * AnnotationProcessorの処理時に出力するメッセージ用の{@link javax.annotation.processing.Messager}を返却する.
     *
     * @return
     */
    Messager getMessager() {
        if (this.messager == null) {
            throw new ClassFactoryException("javax.annotation.processing.Messager is not set");
        }
        return this.messager;
    }

    /**
     * エラーメッセージを出力
     *
     * @param message 出力メッセージ
     * @param element 対象Element
     */
    public void printErrMessage(String message, Element element) {
        this.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * クラス生成対象フィルター
     *
     * @param element 注釈がついている要素
     * @return クラス生成対象の場合:true。対象外の場合:false
     */
    public boolean filter(Element element) {
        return element.getAnnotation(targetAnnotaion) != null;
    }

    /**
     * 注釈がついている要素からJavaFileリストを作成する.<br>
     * １つのAnnotationをもったクラスから複数のJavaFileを作成することを考慮して戻り値はリストとする.
     *
     * @param element 注釈がついている要素
     * @return 生成したJavaFileリスト
     */
    public abstract List<JavaFile> toJavaFiles(Element element);
}
