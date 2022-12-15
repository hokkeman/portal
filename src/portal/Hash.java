package portal;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;


public class Hash implements Serializable {
	/**
	 * パスワードのハッシュ化用ソルトを生成するメソッド(10桁)
	 * @return
	 * ソルト
	 */
	public static String makeSalt() {
		SecureRandom sRandom = new SecureRandom();
        // ランダムのバイト配列を格納
        byte[] bytes = new byte[20];

        // ランダムなバイト配列を作成
        sRandom.nextBytes(bytes);

        // 文字化け防止の為一度Base64にエンコード
        byte[] encoded = Base64.getEncoder().encode(bytes);
        String salt = null;
		try {
			salt = new String(encoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return salt;

	}

	/**
	 * パスワードにソルトとペッパーを付加しSHA-256アルゴリズムで1万回ハッシュ化し返すメソッド
	 * @param pass
	 * 入力フォームに入力されたパスワード
	 * @param salt
	 * ソルト
	 * @return
	 * ハッシュ値
	 */
	public static String hashing(String pass , String salt) {
		// パスワードとソルトとペッパーを連結
		String passSaltPepper = pass + salt + "a5c7004a1dd17a7a06a97f8086e0ce7d452322cf3eb34ce95cfe55d1b317cc8a";
		StringBuilder sb = new StringBuilder();
		byte[] cipher_byte;
		try {
			// 10000回繰返しハッシュ化する
			for(int i = 0; i<=10000; i++) {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(passSaltPepper.getBytes());
				cipher_byte = md.digest();
				sb = new StringBuilder(2 * cipher_byte.length);
				for (byte b : cipher_byte) {
					sb.append(String.format("%02x", b & 0xff));
				}
				passSaltPepper = sb.toString();
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
		return passSaltPepper;
	}


}