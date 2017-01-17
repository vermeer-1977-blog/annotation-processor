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
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import org.junit.Test;

/**
 *
 * @author Yamashita,Takahiro
 */
public class ClassFactoryManagerTest {

    public ClassFactoryManagerTest() {
    }

    @Test
    public void リストのappendの疎通() {

        Messager messager = new Messager() {
            @Override
            public void printMessage(Diagnostic.Kind kind, CharSequence msg) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void printMessage(Diagnostic.Kind kind, CharSequence msg, Element e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void printMessage(Diagnostic.Kind kind, CharSequence msg, Element e, AnnotationMirror a) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void printMessage(Diagnostic.Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        List<AbstractClassFactory> classFactories = Arrays.asList(new AbstractClassFactory(Override.class) {
            @Override
            public List<JavaFile> toJavaFiles(Element element) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        ClassFactoryManager instance = ClassFactoryManager.of(messager).append(classFactories).build();

    }

    @Test(expected = ClassFactoryException.class)
    public void 初期値設定Ofの疎通() {
        ClassFactoryManager.Builder result = ClassFactoryManager.of(null);
    }

}
