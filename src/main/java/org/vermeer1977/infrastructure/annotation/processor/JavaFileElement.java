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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * JavaFileの作成時にElementを元に情報を編集するヘルパクラスです.
 *
 * @author Yamashita,Takahiro
 */
public class JavaFileElement {

    private final ProcessingEnvironment processingEnv;
    private final Element element;

    private JavaFileElement(ProcessingEnvironment processingEnv, Element element) {
        if (processingEnv == null || element == null) {
            throw new ClassFactoryException("JavaFileElement must set processingEnv and element.");
        }
        this.processingEnv = processingEnv;
        this.element = element;
    }

    /**
     * 必須項目を設定してインスタンスを構築します.
     *
     * @param processingEnv JavaFile作成時のメッセージ出力に使用する{@link javax.annotation.processing.ProcessingEnvironment}（必須）
     * @param element AnnotaionProcessorで取得した要素（必須）
     * @return {@code JavaFileElement}
     * @throws ClassFactoryException 必須項目が未設定の場合
     */
    public static JavaFileElement of(ProcessingEnvironment processingEnv, Element element) {
        return new JavaFileElement(processingEnv, element);
    }

    /**
     * 処理対象のElementを返却します.
     *
     * @return 処理対象の{@link javax.lang.model.element.Element}
     */
    public Element getElement() {
        return this.element;
    }

    /**
     * {@link javax.lang.model.element.ElementKind}が一致する要素をすべて返却する
     *
     * @param elementKind 取得対象の{@link javax.lang.model.element.ElementKind}
     * @return 抽出した要素リスト
     */
    public List<Element> filter(ElementKind elementKind) {
        TypeElement type = (TypeElement) this.element;
        return type.getEnclosedElements().stream()
                .filter(e -> e.getKind().equals(elementKind))
                .collect(Collectors.toList());
    }

    /**
     * 指定のAnnotationが付与された要素を返却する
     *
     * @param <A> 処理対象のAnnotationの型
     * @param annotation 取得対象のAnnotation
     * @return 抽出した要素リスト
     */
    public <A extends Annotation> List<Element> filter(Class<A> annotation) {
        TypeElement type = (TypeElement) this.element;
        return type.getEnclosedElements().stream()
                .filter(e -> e.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    /**
     * Annotationを付与しているクラス名を返却する.
     *
     * @return クラス名
     */
    public String toClassName() {
        return this.element.getSimpleName().toString();
    }

    /**
     * Annotationを付与しているクラス名のパッケージ名称を返却する.
     * <P>
     * パッケージ名は{@link org.vermeer1977.infrastructure.annotation.processor.resource.GenerateResourceEnum}が
     * 付与されたクラスのパッケージ名にクラス名を小文字にしたサブパッケージを追記したものです. ただし、生成元の資産が
     * デフォルトパッケージの場合は、テストまたは特殊なケースであると考えられるためサブパッケージを付与せずデフォルトパッケージに並列で出力します。
     *
     * @return パッケージ名称
     */
    public String toPackageName() {
        String packagePath = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
        String className = this.toClassName().toLowerCase(Locale.ENGLISH);
        return packagePath.equals("")
               ? ""
               : packagePath + "." + className;
    }
}
