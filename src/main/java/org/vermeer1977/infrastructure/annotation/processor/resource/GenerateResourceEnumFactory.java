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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import org.vermeer1977.infrastructure.annotation.processor.AbstractClassFactory;
import org.vermeer1977.infrastructure.annotation.processor.JavaFileElement;

/**
 * {@code GenerateResourceEnum}アノテーションを付与しているクラスからEnumを生成します.
 * <P>
 * @see GenerateResourceEnum
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
     * <P>
     * 生成するEnum値にControlやLocaleを適用したい場合は、本メソッド内の{@link org.vermeer1977.infrastructure.annotation.processor.resource.ResourceEnumToJavaFile}
     * にパラメーターとして追加してください. なお、JavaFileの編集は{@link ResourceEnumToJavaFile}にて行います.
     */
    @Override
    public List<JavaFile> toJavaFiles(Element element) {
        JavaFileElement javaFileElement = JavaFileElement.of(super.getProcessingEnvironment(), element);
        if (this.precondition(javaFileElement) == false) {
            return new ArrayList<>();
        }
        String packageName = javaFileElement.toPackageName();

        return javaFileElement.filter(TargetResource.class).stream()
                .map(VariableElement.class::cast)
                .map(ve -> {
                    return ResourceEnumToJavaFile.of(ve.getConstantValue().toString())
                            .packageName(packageName)
                            .build().toJavaFile();
                })
                .collect(Collectors.toList());
    }

    /**
     * ResourceからEnumを作成する事前条件
     *
     * @param javaFileElement
     * @return 事前条件を充足している場合、true
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
