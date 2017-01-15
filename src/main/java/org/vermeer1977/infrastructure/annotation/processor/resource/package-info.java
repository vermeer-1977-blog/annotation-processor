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
 * ResourceBundleファイルからEnumClassの作成を行うクラスを提供します.<br>
 * 使用について
 * <ol>
 * <li>
 * 生成対象の情報を管理するクラスに{@link org.vermeer1977.infrastructure.annotation.processor.resource.GenerateResourceEnum}をアノテート
 * </li>
 * <li>
 * アノテートしたクラスのフィールドに{@link org.vermeer1977.infrastructure.annotation.processor.resource.TargetResource}のフィールド値をリソース名としてEnumクラスを生成
 * </li>
 * </ol>
 * <p>
 * 生成されるEnumClassについて
 * <ul>
 * <li>
 * クラス名：ResourceNameの先頭一文字を大文字にした名称<br>
 * （例：message→Message）<br>
 * 注意事項：CamelCaseではなく、あくまで１文字目のみ
 * </li>
 * <li>
 * Enum定数値：Resoueceのkeyをすべて大文字にした値.<br>
 * （例：msg001→MSG001）<br>
 * 理由：定数として可読性を持たせるため
 * </li>
 * </ul>
 *
 * <p>
 * 実装例
 * <pre>{@code
 * @GenerateResourceEnum
 * public class SampleEnum {
 *
 * @TargetResource
 * final String resourceName = "resource.message";
 * }
 * code}
 * </pre>
 * <p>
 * 補足事項
 * <ul>
 * <li>
 * {@link org.vermeer1977.infrastructure.annotation.processor.resource.TargetResource}をアノテートしたフィールドは同一クラス内に複数指定して良い.
 * </li>
 * </ul>
 */
package org.vermeer1977.infrastructure.annotation.processor.resource;
