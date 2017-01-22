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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.vermeer1977.infrastructure.annotation.processor.resource.GenerateResourceEnumFactory;

/**
 * AnnotationProcessorにてコード生成を行うクラスです.
 * <P>
 * @author Yamashita,Takahiro
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"*"})
public class ClassFactoryProcessor extends AbstractProcessor {

    private final boolean isDebug;
    private Filer filer;
    private JavaFileFactory javaFileFactory;

    /**
     * インスタンスを構築します.
     * <P>
     * デフォルトではデバッグ出力はしません.
     */
    public ClassFactoryProcessor() {
        this(false);
    }

    /**
     * デバッグ出力の有無を指定してインスタンスを構築します.
     * <P>
     * テストユニットにて生成コードの確認をしたい場合に使用することを想定しています.
     *
     * @param isDebug true:デバッグ出力をする
     */
    public ClassFactoryProcessor(boolean isDebug) {
        this.isDebug = isDebug;
    }

    /**
     * 初期化をします.
     * <P>
     * クラス生成をするFactoryクラスを{@link ClassFactoryManager}の引数として実装をしてください.
     *
     * @see ClassFactoryManager
     * @param processingEnv 環境情報
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();

        ClassFactoryManager classFactoryManager = ClassFactoryManager
                .of(processingEnv)
                .append(new GenerateResourceEnumFactory())
                .build();
        this.javaFileFactory = JavaFileFactory.of(classFactoryManager);
    }

    /**
     * Annotationマーカーしたクラスからクラスを生成します.
     *
     * @param annotations SupportedAnnotationTypesで検出されたAnnotationセット
     * @param roundEnv 注釈処理ラウンド
     * @return 生成が正常終了した場合はtrue、異常終了した場合はfalse. 生成した結果が０件またはNullの場合は該当なしとして無視するためtrueとします.
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<JavaFile> javaFiles = this.javaFileFactory.create(annotations, roundEnv);
        if (javaFiles.isEmpty()) {
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
     * AnnotationProcessorで生成したコードを確認するためにコンソールに出力します.
     * <P>
     * Debugの時のみ出力します.
     *
     * @param javaFile コンソールに出力したいJavaFile
     */
    void debug(JavaFile javaFile) {
        if (this.isDebug == false) {
            return;
        }
        System.out.println();
        System.out.println(javaFile.toString());
    }
}
