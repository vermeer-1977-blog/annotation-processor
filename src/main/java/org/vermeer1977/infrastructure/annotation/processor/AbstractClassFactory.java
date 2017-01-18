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
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * AnnotationProcessorによるクラス生成において、JavaFile生成クラスとの連携を行う基底となる抽象クラス.
 *
 * @author Yamashita,Takahiro
 */
public abstract class AbstractClassFactory {

    private final Class<? extends Annotation> targetAnnotaion;
    private ProcessingEnvironment processingEnv;

    /**
     * 指定した生成対象マーカーとなるAnnotationInterfaceからインスタンスを構築します.
     *
     * @param <A> 生成対象マーカーインターフェースの型
     * @param targetAnnotaion 生成マーカーとなるAnnotationInterface（必須）
     * @throws ClassFactoryException 必須項目が未設定の場合
     */
    public <A extends Annotation> AbstractClassFactory(Class<A> targetAnnotaion) {
        if (targetAnnotaion == null) {
            throw new ClassFactoryException("AbstractClassFactory must set targetAnnotaion.");
        }
        this.targetAnnotaion = targetAnnotaion;
    }

    /**
     * AnnotationProcessor実行時の環境情報である{@link javax.annotation.processing.ProcessingEnvironment}を設定します.
     *
     * @param processingEnv AnnotationProcessor実行時の環境情報である{@link javax.annotation.processing.ProcessingEnvironment}
     */
    public void setProcessingEnvironment(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    /**
     * AnnotationProcessor実行時の環境情報である{@link javax.annotation.processing.ProcessingEnvironment}を返却します.
     *
     * @return AnnotationProcessor実行時の環境情報である{@link javax.annotation.processing.ProcessingEnvironment}
     */
    public ProcessingEnvironment getProcessingEnvironment() {
        return this.processingEnv;
    }

    /**
     * AnnotationProcessorの処理時に出力するメッセージ用の{@link javax.annotation.processing.Messager}を返却します.
     *
     * @return メッセージ出力用の{@link javax.annotation.processing.Messager}
     */
    Messager getMessager() {
        if (this.processingEnv == null) {
            throw new ClassFactoryException("javax.annotation.processing.ProcessingEnvironment is not set");
        }
        return this.processingEnv.getMessager();
    }

    /**
     * AnnotationProcessorによるJavaFile作成時のエラーメッセージを出力します.
     * <P>
     * 事前に{@link #setMessager(javax.annotation.processing.Messager)
     * }にて出力用の{@link javax.annotation.processing.Messager}を設定しておいてください.
     *
     * @param message 出力メッセージ
     * @param element 対象Element
     * @throws ClassFactoryException Messagerを設定していない場合
     */
    public void printErrMessage(String message, Element element) {
        this.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * 処理対象Annotationの有無判定.
     * <P>
     * 処理対象Annotationはコンストラクタにて指定します.
     *
     * @param element 処理対象を抽出元となる{@link javax.lang.model.element.Element}（必須）
     * @return JavaFile生成対象の場合、true
     * @throws ClassFactoryException 必須項目が未設定の場合
     */
    public boolean filter(Element element) {
        if (element == null) {
            throw new ClassFactoryException("AbstractClassFactory must set targetAnnotaion.");
        }
        return element.getAnnotation(targetAnnotaion) != null;
    }

    /**
     * Annotationがついている要素からJavaFileリストを作成します.
     * <P>
     * １つのAnnotationをもったクラスから複数のJavaFileを作成することを考慮して戻り値はリストとします.
     *
     *
     * @param element 処理対象のAnnotationがついている要素
     * @return 生成したJavaFileリスト
     */
    public abstract List<JavaFile> toJavaFiles(Element element);
}
