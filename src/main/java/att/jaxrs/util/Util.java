/*
 * Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package att.jaxrs.util;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.http.HttpResponse;

import java.io.*;

/**
 * Created by ananthaneshan on 10/10/14.
 */
public class Util {
	// convert InputStream to String
	public static String getStringFromInputStream(InputStream inputStream) {
		/*BufferedReader bufferedReader = null;
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

		return stringBuilder.toString();*/

		CachedOutputStream bos = new CachedOutputStream();
		try {
			IOUtils.copy(inputStream, bos);
			inputStream.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.getOut().toString();

	}

	public static String getStringFromInputStream(HttpResponse httpResponse) {
		/*BufferedReader bufferedReader = null;
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

		return stringBuilder.toString();*/

		CachedOutputStream bos = new CachedOutputStream();
		try {
			IOUtils.copy(httpResponse.getEntity().getContent(), bos);
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.getOut().toString();

	}

	public static InputStream getInputStreamFromString(String string) {
		return new ByteArrayInputStream(string.getBytes());
	}
}
