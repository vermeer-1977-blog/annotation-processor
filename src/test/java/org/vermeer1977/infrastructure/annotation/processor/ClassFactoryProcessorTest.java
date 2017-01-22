package org.vermeer1977.infrastructure.annotation.processor;

import com.google.common.io.Resources;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Yamashita,Takahiro
 */
public class ClassFactoryProcessorTest {

    @Ignore
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

    @Test
    public void 親パッケージ指定なし_サブパッケージ指定なし() {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("packagetest/SampleEnumPackage.java")))
                .processedWith(new ClassFactoryProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(new SourceFileReader(Resources.getResource("packagetest/sampleenumpackage/Message6.java")).toJavaFileObject());
    }

    @Test
    public void 親パッケージ指定あり_サブパッケージ指定なし() {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("packagetest/EnumBasePackageName.java")))
                .processedWith(new ClassFactoryProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(new SourceFileReader(Resources.getResource("basepackage/Message7.java")).toJavaFileObject());
    }

    @Test
    public void 親パッケージ指定なし_サブパッケージ指定あり() {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("packagetest/EnumSubPackageName.java")))
                .processedWith(new ClassFactoryProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(new SourceFileReader(Resources.getResource("packagetest/subpackage/Message8.java")).toJavaFileObject());
    }

    @Test
    public void 親パッケージ指定あり_サブパッケージ指定あり() {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("packagetest/EnumBaseSubPackageName.java")))
                .processedWith(new ClassFactoryProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(new SourceFileReader(Resources.getResource("basepackage/subpackage2/Message9.java")).toJavaFileObject());
    }
}
