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
/**
 * Pluggable Annotation Processing API を用いてコードを自動生成するクラス.<br>
 * <br>
 * 実装の流れ<br>
 * <ol>
 * <li>{@link AbstractClassFactory}を拡張してJavaFileを編集するクラスを実装する</li>
 * <li>{@link ClassFactoryProcessor}のコンストラクタにて{@link JavaFileFactory}に自動生成クラスとして設定する</li>
 * </ol>
 */
package org.vermeer1977.infrastructure.annotation.processor;
