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

package att.jaxrs.client;

import att.jaxrs.util.Constants;
import att.jaxrs.util.Marshal;
import att.jaxrs.util.Util;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {

	private int category_id;
	private String category_name;

	public Category() {
	}

	public Category(int category_id, String category_name) {
		this.category_id = category_id;
		this.category_name = category_name;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	@Override public String toString() {
		return "Category with id: " + category_id + "and name: " + category_name;
	}

	public static Category[] getCategories() {
		// Sent HTTP GET request to query category table
		System.out.println("Sent HTTP GET request to query category table");

		URL url;
		InputStream inputStream = null;
		try {
			url = new URL(Constants.SELECT_ALL_CATEGORY_RESOURCE);
			inputStream = url.openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String categoryResponse = Util.getStringFromInputStream(inputStream);
		System.out.println(categoryResponse);
		CategoryCollection categoryCollection = null;
		try {
			categoryCollection = Marshal
					.unmarshal(CategoryCollection.class, categoryResponse);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (null != categoryCollection && categoryCollection.getCategories().length > 0) {
			return categoryCollection.getCategories();
		}
		return null;
	}
}
