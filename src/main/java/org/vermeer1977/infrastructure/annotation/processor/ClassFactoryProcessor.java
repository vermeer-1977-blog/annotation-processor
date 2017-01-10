package org.vermeer1977.infrastructure.annotation.processor;

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
import com.squareup.javapoet.JavaFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.vermeer1977.infrastructure.annotation.processor.resource.GenerateResourceEnumFactory;

/**
 * Enumの引数つきの定数のフィールドから enum 型の enum 定数を返すユーティリティクラスを生成する.<br>
 * コード生成をしたい場合、AbstractTargetClassFactoryをextendしたクラスを作成してprocessのtargetClassFactoriesの処理対象クラスとして追加する.
 *
 * @see AbstractTargetClassFactory
 *
 * @author Yamashita,Takahiro
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"*"})
public class ClassFactoryProcessor extends AbstractProcessor {

    private Filer filer;

    private final boolean isDebug;

    public ClassFactoryProcessor() {
        this.isDebug = false;
    }

    public ClassFactoryProcessor(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
    }

    /**
     * マーカーのannotationからクラスを生成する
     *
     * @param annotations SupportedAnnotationTypesで検出されたAnnotationセット
     * @param roundEnv 注釈処理ラウンド
     * @return 生成が正常終了した場合はtrue、異常終了した場合はfalse。生成した結果が０件またはNullの場合は該当なしとして無視するためtrueとする
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<AbstractTargetClassFactory> targetClassFactories = Arrays.asList(new GenerateResourceEnumFactory());

        List<JavaFile> javaFiles = new ArrayList<>();

        for (TypeElement typeElement : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                List<JavaFile> elementJavaFiles = targetClassFactories.stream()
                        .filter(target -> target.filter(element))
                        .map(target -> target.toJavaFile(element))
                        .collect(Collectors.toList());
                javaFiles.addAll(elementJavaFiles);
            }
        }

        if (javaFiles.isEmpty() || javaFiles.get(0) == null) {
            return true;
        }

        try {
            for (JavaFile javaFile : javaFiles) {
                this.debug(javaFile);
                javaFile.writeTo(this.filer);
            }
        } catch (IOException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
            return false;
        }
        return true;
    }

    /**
     * AnnotationProcessorで生成したコードを確認するためにコンソールに出力する.<br>
     * Debugの時のみ出力する.
     *
     * @param javaFile コンソールに出力したいJavaFile
     */
    private void debug(JavaFile javaFile) {
        if (this.isDebug == false) {
            return;
        }
        System.out.println();
        System.out.println(javaFile.toString());
    }

}
