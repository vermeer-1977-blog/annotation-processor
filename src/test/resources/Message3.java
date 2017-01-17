import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Generated by ClassFactoryProcessor.
 * @see org.vermeer1977.infrastructure.annotation.processor.ClassFactoryProcessor
 * @see org.vermeer1977.infrastructure.annotation.processor.resource
 */
enum Message3 {
  /**
   * メッセージ331<br>
   * parameter count = 0
   */
  MSG331("msg331", 0, "メッセージ331"),

  /**
   * メッセージ332{0}and{1}<br>
   * parameter count = 2
   */
  MSG332("msg332", 2, "メッセージ332{0}and{1}");

  private static ResourceBundle.Control control;

  private static Locale locale;

  private final String key;

  private final Integer paramCount;

  private final String value;

  private Message3(String key, Integer paramCount, String value) {
    this.key = key;
    this.paramCount = paramCount;
    this.value = value;
  }

  /**
   * 任意設定項目：ResourceBundleから値を取得する際に使用するControlを設定します.<br>
   * 未設定の場合、ResourceBundleのデフォルトで処理します.<br>
   * 本設定はロケールのFallbackを設定したい場合などに使用します.<br>Caution:this feild is Thread-Unsafe.
   *
   */
  public static void setControl(ResourceBundle.Control control) {
    Message3.control = control;
  }

  /**
   * 任意設定項目：ResourceBundleから値を取得する際に使用するLocaleを設定します.<br>
   * 未設定の場合、デフォルトロケールで処理します.<br>
   * Caution:this feild is Thread-Unsafe.
   *
   */
  public static void setLocale(Locale locale) {
    Message3.locale = locale;
  }

  /**
   * リソースの値を返却します.<br>
   * 例外捕捉時の対応については、リソースの取得が出来なかった場合に最低限状況判別が出来うるメッセージを表示させるための措置です.<br>
   * あわせてメッセージIDを付与してリソースの取得が出来ていなかったことを可視できるようにしています.<br>
   * @return 当該定数に該当するリソースの値
   */
  @Override
  public String toString() {
    try {
      java.util.Locale _locale = Message3.locale == null ? java.util.Locale.getDefault() : Message3.locale;
      if (Message3.control == null) {
        return java.util.ResourceBundle.getBundle("resource.message3", _locale).getString(this.key);
      }
      return java.util.ResourceBundle.getBundle("resource.message3", _locale, Message3.control).getString(this.key);
    } catch(Exception ex) {
      return this.value;
    }
  }

  /**
   * 埋め込み文字の置換をした文字列を返却します.<br>
   * @return 埋め込み文字を置換した文字列
   */
  public String format(Object... params) {
    return java.text.MessageFormat.format(toString(), params);
  }
}
