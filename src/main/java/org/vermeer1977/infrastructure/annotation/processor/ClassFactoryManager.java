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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Messager;

/**
 * AnnotationProcessorでクラスを生成するFactoryクラスのFirstClassCollection管理クラスです.
 *
 * @author Yamashita,Takahiro
 */
public class ClassFactoryManager {

    private final List<AbstractClassFactory> classFactories;

    ClassFactoryManager(Messager messager, List<AbstractClassFactory> classFactories) {
        classFactories.stream().forEach(classFactory -> {
            classFactory.setMessager(messager);
        });
        this.classFactories = classFactories;
    }

    /**
     * クラス生成Factoryクラスのリストを返却します.
     *
     * @return クラス生成Factoryリスト
     */
    public List<AbstractClassFactory> getClassFactories() {
        return this.classFactories;
    }

    /**
     * 必須項目を設定します.
     *
     * @param messager JavaFile作成時のメッセージ出力に使用する{@link javax.annotation.processing.Messager}（必須）
     * @return {@link ClassFactoryManager.Builder}
     * @throws ClassFactoryException 必須項目が未設定の場合
     */
    public static Builder of(Messager messager) {
        if (messager == null) {
            throw new ClassFactoryException("ClassFactoryManager must set messager.");
        }
        return new ClassFactoryManager.Builder(messager);

    }

    /**
     * {@link ClassFactoryManager}のBuilder
     */
    public static class Builder {

        private final Messager messager;
        private final List<AbstractClassFactory> classFactories;

        /**
         * 必須項目を設定します.
         *
         * @param messager JavaFile作成時のメッセージ出力に使用する{@link javax.annotation.processing.Messager}
         */
        public Builder(Messager messager) {
            this.messager = messager;
            this.classFactories = new ArrayList<>();
        }

        /**
         * JavaFileを作成するFactoryクラスを追加します.
         *
         * @param <E> AnnotationProcessorにてクラス生成を行うクラスの型
         * @param classFactory AnnotationProcessorにてクラス生成を行うクラス
         * @return chainに使用するbuilderクラス
         */
        public <E extends AbstractClassFactory> Builder append(E classFactory) {
            this.classFactories.add(classFactory);
            return this;
        }

        /**
         * JavaFileを作成するFactoryクラスリストを追加します.
         *
         * @param <E> AnnotationProcessorにてクラス生成を行うクラスの型
         * @param classFactories AnnotationProcessorにてクラス生成を行うクラスのリスト
         * @return chainに使用するbuilderクラス
         */
        public <E extends AbstractClassFactory> Builder append(List<E> classFactories) {
            this.classFactories.addAll(classFactories);
            return this;
        }

        /**
         * ClassFactoryManagerのインスタンスを構築します.
         *
         * @return ClassFactoryManagerインスタンス
         */
        public ClassFactoryManager build() {
            return new ClassFactoryManager(this.messager, this.classFactories);
        }
    }
}
