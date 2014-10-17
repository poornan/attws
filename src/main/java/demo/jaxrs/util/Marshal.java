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

package demo.jaxrs.util;

import att.jaxrs.util.Util;
import org.apache.http.HttpResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ananthaneshan on 10/8/14.
 */
public class Marshal {
	public static <T> T unmarshal(Class<T> xmlType, InputStream inputStream)
			throws JAXBException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder out = new StringBuilder();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		String namespace = out.toString();

		System.out.println(namespace);
		namespace = namespace.replaceAll("xmlns=\"http://ws.wso2.org/dataservice\"", "");
		System.out.println(namespace);
		InputStream stream = Util.getInputStreamFromString(namespace);
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlType);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		T doc = (T) unmarshaller.unmarshal(stream);
		return doc;
	}

	public static <T> T unmarshal(Class<T> xmlType, HttpResponse httpResponse)
			throws JAXBException {
		String namespace = "";
		try {
			namespace = Util.getStringFromInputStream(httpResponse.getEntity().getContent());
			System.out.println(namespace);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println(namespace);
		namespace = namespace.replaceAll("xmlns=\"http://ws.wso2.org/dataservice\"", "");
		System.out.println(namespace);
		InputStream stream = Util.getInputStreamFromString(namespace);
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlType);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		T doc = (T) unmarshaller.unmarshal(stream);
		return doc;
	}

	public static <T> T unmarshal(Class<T> xmlType, String string)
			throws JAXBException {
		String namespace = "";

		namespace = new String(string);
		System.out.println(namespace);

		namespace = namespace.replaceAll(" xmlns=\"http://ws.wso2.org/dataservice\"", "");
		System.out.println(namespace);

		InputStream stream = Util.getInputStreamFromString(namespace);
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlType);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		T doc = (T) unmarshaller.unmarshal(stream);
		return doc;
	}
}
