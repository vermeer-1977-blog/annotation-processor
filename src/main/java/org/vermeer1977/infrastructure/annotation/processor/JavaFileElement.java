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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * JavaFileを編集する際のヘルパクラス
 *
 * @author Yamashita,Takahiro
 */
public class JavaFileElement {

    private final Element element;

    private JavaFileElement(Element element) {
        this.element = element;
    }

    /**
     * Factory
     *
     * @param element AnnotaionProcessorで取得した要素
     * @return 生成したクラス
     */
    public static JavaFileElement of(Element element) {
        return new JavaFileElement(element);
    }

    public List<Element> filterKind(ElementKind elementKind) {
        TypeElement type = (TypeElement) element;
        return type.getEnclosedElements().stream()
                .filter(e -> e.getKind().equals(elementKind))
                .collect(Collectors.toList());
    }

    /**
     * Annotationを付与しているクラス名を取得する.
     *
     * @return Annotationを付与しているクラス名
     */
    public String toClassName() {
        return this.element.getSimpleName().toString();
    }

    /**
     * パッケージ名称を取得する
     *
     * @return Elementから編集したパッケージ名称
     */
    public String toPackageName() {
        List<String> packNames = new ArrayList<>();
        Element packageElem = element.getEnclosingElement();
        while (packageElem != null) {
            String packName = packageElem.getSimpleName().toString();
            packNames.add(packName);
            packageElem = packageElem.getEnclosingElement();
        }
        return String.join(".", packNames);
    }

}
