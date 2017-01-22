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

import com.google.common.io.Resources;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.vermeer1977.infrastructure.annotation.processor.SourceFileReader;

/**
 *
 * @author Yamashita,Takahiro
 */
public class ResourceEnumToJavaFileTest {

    @Ignore
    @Test
    public void 疎通() {
        String resourceBaseName = "resource.message";
        System.out.println(ResourceEnumToJavaFile.of(resourceBaseName).build().toSourceCode());
    }

    @Test(expected = java.util.MissingResourceException.class)
    public void リソースが存在しない場合は例外スロー() {
        String resourceBaseName = "resource.messagenotexist";
        ResourceEnumToJavaFile.of(resourceBaseName).build().toJavaFile();
    }

    @Test
    public void ロケールを指定_英語ロケールを参照_ロケール一致() throws IOException {
        String resourceBaseName = "resource.message2";
        String javaFile = ResourceEnumToJavaFile.of(resourceBaseName)
                .locale(Locale.ENGLISH)
                .build().toSourceCode();
        String after = new SourceFileReader(Resources.getResource("Message2.java")).toSourceCode();
        Assert.assertThat(javaFile, is(after));
    }

    @Test
    public void ロケールを指定_英語ロケールを参照_ロケール不一致() throws IOException {
        String resourceBaseName = "resource.message2";
        String javaFile = ResourceEnumToJavaFile.of(resourceBaseName)
                .build().toSourceCode();
        String after = new SourceFileReader(Resources.getResource("Message2.java")).toSourceCode();
        Assert.assertThat(javaFile, is(not(after)));
    }

    @Test
    public void 存在しないロケール_デフォルトロケール_日本_が優先される() throws IOException {
        String resourceBaseName = "resource.message3";
        String javaFile = ResourceEnumToJavaFile.of(resourceBaseName)
                .locale(Locale.ITALIAN)
                .build().toSourceCode();
        String after = new SourceFileReader(Resources.getResource("Message3.java")).toSourceCode();
        Assert.assertThat(javaFile, is(after));
    }

    @Test
    public void 存在しないロケール_ControlでFallbackを指定_デフォルトリソースが優先される() throws IOException {
        String resourceBaseName = "resource.message4";
        ResourceBundle.Control control = ResourceBundle.Control.getNoFallbackControl(
                ResourceBundle.Control.FORMAT_DEFAULT);
        String javaFile = ResourceEnumToJavaFile.of(resourceBaseName)
                .locale(Locale.ITALIAN)
                .control(control)
                .build().toSourceCode();
        String after = new SourceFileReader(Resources.getResource("Message4.java")).toSourceCode();
        Assert.assertThat(javaFile, is(after));
    }

    @Test
    public void ロケールを指定しない_Controlで優先ロケールを指定_英語を優先させる() throws IOException {
        String resourceBaseName = "resource.message5";
        ResourceBundle.Control control = new ResourceBundle.Control() {
            @Override
            public List<Locale> getCandidateLocales(
                    String baseName, Locale locale) {
                if (locale.equals(Locale.JAPAN)) {
                    return Arrays.asList(Locale.ENGLISH,
                                         locale,
                                         Locale.JAPANESE,
                                         Locale.ROOT);
                } else {
                    return super.getCandidateLocales(
                            baseName, locale);
                }
            }
        };
        String javaFile = ResourceEnumToJavaFile.of(resourceBaseName)
                .control(control)
                .build().toSourceCode();
        String after = new SourceFileReader(Resources.getResource("Message5.java")).toSourceCode();
        Assert.assertThat(javaFile, is(after));
    }
}
