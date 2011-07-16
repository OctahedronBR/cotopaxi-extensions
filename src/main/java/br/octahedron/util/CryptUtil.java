/*
 *  This file is part of Cotopaxi.
 *
 *  Cotopaxi is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Cotopaxi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the Lesser GNU General Public License
 *  along with Cotopaxi. If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * This entity provides static methods to encrypt strings and provide a secure random number.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class CryptUtil {

	private static SecureRandom random = new SecureRandom();

	/**
	 * Generates a random string.
	 * 
	 * @return a random string.
	 */
	public static String generateRandonString() {
		return new BigInteger(130, random).toString(32);
	}

	/**
	 * Encrypts the given plain text
	 * 
	 * @param plaintext
	 *            the plain text to be encrypted
	 * @return the string encrypted
	 */
	public static String encrypt(String plaintext) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");

			md.update(plaintext.getBytes("UTF-8"));

			byte raw[] = md.digest();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < raw.length; i++) {
				if ((raw[i] & 0xff) < 0x10) {
					buf.append("0");
				}
				buf.append(Long.toString(raw[i] & 0xff, 16));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			// ERROR
		} catch (UnsupportedEncodingException e) {
			// ERROR
		}
		return "";
	}

}
