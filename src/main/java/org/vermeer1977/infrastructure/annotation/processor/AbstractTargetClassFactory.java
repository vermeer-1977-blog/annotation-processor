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
import javax.lang.model.element.Element;

/**
 * AnnotationProcessorによるClass生成の基底となる抽象クラス.
 *
 * @author Yamashita,Takahiro
 */
public abstract class AbstractTargetClassFactory {

    private final Class<? extends Annotation> targetAnnotaion;

    /**
     * 生成マーカーとなるAnnotationInterfaceを指定する
     *
     * @param <T> AnnotaionInterface
     * @param targetAnnotaion 生成マーカーとなるAnnotationInterface
     */
    public <T extends Annotation> AbstractTargetClassFactory(Class<T> targetAnnotaion) {
        this.targetAnnotaion = targetAnnotaion;
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
     * 注釈がついている要素からクラスを生成する
     *
     * @param element 注釈がついている要素
     * @return 生成したコード文字列。生成対象外の要素の場合はnullを返却する
     */
    public abstract JavaFile toJavaFile(Element element);

}
