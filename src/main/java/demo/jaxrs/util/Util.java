package demo.jaxrs.util;

import org.apache.http.HttpResponse;

import java.io.*;

/**
 * Created by ananthaneshan on 10/10/14.
 */
public class Util {
	// convert InputStream to String
	public static String getStringFromInputStream(InputStream inputStream) {
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = new StringBuilder();

		String line;
		try {

			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return stringBuilder.toString();

	}

	public static String getStringFromInputStream(HttpResponse httpResponse) {
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = new StringBuilder();

		String line;
		try {

			bufferedReader = new BufferedReader(
					new InputStreamReader(httpResponse.getEntity().getContent()));
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return stringBuilder.toString();

	}

	public static InputStream getInputStreamFromString(String string) {
		return new ByteArrayInputStream(string.getBytes());
	}
}
