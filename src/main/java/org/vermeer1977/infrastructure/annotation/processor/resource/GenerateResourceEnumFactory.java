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

import com.squareup.javapoet.JavaFile;
import javax.lang.model.element.Element;
import org.vermeer1977.infrastructure.annotation.processor.AbstractTargetClassFactory;
import org.vermeer1977.infrastructure.annotation.processor.JavaFileElement;
import org.vermeer1977.infrastructure.resourcebundle.classfactory.ResourceEnumFactory;

/**
 * ResourceBundleの情報を元にEnumを自動生成する.<br>
 *
 *
 * @author Yamashita,Takahiro
 */
public class GenerateResourceEnumFactory extends AbstractTargetClassFactory {

    /**
     * {@inheritDoc}
     *
     * @see GenerateResourceEnum
     */
    public GenerateResourceEnumFactory() {
        super(GenerateResourceEnum.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavaFile toJavaFile(Element element) {
        JavaFileElement javaFileElement = JavaFileElement.of(element);

        JavaFile javaFile = ResourceEnumFactory.of("message")
                .packageName(javaFileElement.toPackageName())
                .toJavaFile();

//        TypeSpec.Builder builder = TypeSpec.enumBuilder(javaFileElement.toClassName())
//                .addEnumConstant("TEST");
//
//        TypeElement type = (TypeElement) element;
//        StringBuilder sb = new StringBuilder();
//        sb.append(type.getQualifiedName()).append("\n");
//        List<? extends Element> enclosedElements = type.getEnclosedElements();
//        for (Element elm : enclosedElements) {
//            sb.append("\t").append(elm.getKind()).append(": ").append(elm.getSimpleName()).append("\n");
//            System.out.println(sb);
//        }
        return javaFile;
    }

}
