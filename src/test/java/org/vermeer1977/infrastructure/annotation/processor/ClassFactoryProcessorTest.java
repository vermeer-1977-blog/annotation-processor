package org.vermeer1977.infrastructure.annotation.processor;

import com.google.common.io.Resources;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import org.junit.Test;

public class ClassFactoryProcessorTest {

    @Test
    public void クラス生成() {
        Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("SampleEnum.java")))
                .processedWith(new ClassFactoryProcessor(true))
                .compilesWithoutError();
    }
}
