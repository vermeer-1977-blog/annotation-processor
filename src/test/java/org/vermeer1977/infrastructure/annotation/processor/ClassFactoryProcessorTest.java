package org.vermeer1977.infrastructure.annotation.processor;

import com.google.common.io.Resources;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Yamashita,Takahiro
 */
public class ClassFactoryProcessorTest {

    @Test
    public void 新規クラス生成() {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("SampleEnum.java")))
                .processedWith(new ClassFactoryProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(new SourceFileReader(Resources.getResource("Message.java")).toJavaFileObject());
    }

    @Test
    public void フィールドに対象のアノテーションが存在しない() {
        try {
            Truth.assert_()
                    .about(JavaSourceSubjectFactory.javaSource())
                    .that(JavaFileObjects.forResource(Resources.getResource("NoTargetField.java")))
                    .processedWith(new ClassFactoryProcessor())
                    .compilesWithoutError();

        } catch (java.lang.AssertionError e) {
            Assert.assertThat(e.getMessage(), containsString("GenerateResourceEnum.class annotated. TargetResourceName.class annotated field is required."));
        }
    }
}
