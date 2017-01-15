package org.vermeer1977.infrastructure.annotation.processor;

import com.google.common.io.Resources;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Yamashita,Takahiro
 */
public class ClassFactoryProcessorTest {

    /**
     *
     * @throws IOException
     */
    @Test
    public void 新規クラス生成() throws Exception {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("SampleEnum.java")))
                .processedWith(new ClassFactoryProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(new SourceFileReader(Resources.getResource("Message.java")).toJavaFileObject());
    }
}
