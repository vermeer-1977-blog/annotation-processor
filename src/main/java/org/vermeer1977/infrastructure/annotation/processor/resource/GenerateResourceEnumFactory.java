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
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import org.vermeer1977.infrastructure.annotation.processor.AbstractClassFactory;
import org.vermeer1977.infrastructure.annotation.processor.JavaFileElement;

/**
 * ResourceBundleの情報を元にEnumを自動生成する.<br>
 *
 * @author Yamashita,Takahiro
 */
public class GenerateResourceEnumFactory extends AbstractClassFactory {

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
     * 生成するEnum値にControlやLocaleを指定したい場合は、{@link org.vermeer1977.infrastructure.annotation.processor.resource.ResourceEnumJavaFileFactory}にBuilderに付加してください.
     */
    @Override
    public List<JavaFile> toJavaFiles(Element element) {
        JavaFileElement javaFileElement = JavaFileElement.of(element);
        if (this.precondition(javaFileElement) == false) {
            return null;
        }
        String packageName = javaFileElement.toPackageName();

        return javaFileElement.filter(TargetResource.class).stream()
                .map(VariableElement.class::cast)
                .map(ve -> {
                    return ResourceEnumJavaFileFactory.of(ve.getConstantValue().toString())
                            .packageName(packageName)
                            .toJavaFile();
                })
                .collect(Collectors.toList());
    }

    /**
     * ResourceからEnumを作成する事前条件
     *
     * @param javaFileElement
     * @return 事前条件の充足判定。true：満たされている、false：満たされていない
     */
    boolean precondition(JavaFileElement javaFileElement) {
        boolean hasNotErr = true;
        List<Element> fieldFiltered = javaFileElement.filter(TargetResource.class);
        if (fieldFiltered.isEmpty()) {
            this.printErrMessage("GenerateResourceEnum.class annotated. TargetResourceName.class annotated field is required.", javaFileElement.getElement());
            hasNotErr = false;
        }
        return hasNotErr;
    }
}
