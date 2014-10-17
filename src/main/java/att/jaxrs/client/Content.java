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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Content {

	private long content_id;
	private String level, presenter, reads;

	public Content(long content_id, String level, String presenter, String reads) {
		this.content_id = content_id;
		this.level = level;
		this.presenter = presenter;
		this.reads = reads;
	}

	public Content() {
	}

	public static String addContent(Content content) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("content_id", content.getContent_id().toString()));
		urlParameters.add(new BasicNameValuePair("level", content.getLevel()));
		urlParameters.add(new BasicNameValuePair("presenter", content.getPresenter()));
		urlParameters.add(new BasicNameValuePair("reads", content.getReads()));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.INSERT_CONTENT_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public static String updateContent(Content content) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("content_id", content.getContent_id().toString()));
		urlParameters.add(new BasicNameValuePair("level", content.getLevel()));
		urlParameters.add(new BasicNameValuePair("presenter", content.getPresenter()));
		urlParameters.add(new BasicNameValuePair("reads", content.getReads()));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.UPDATE_CONTENT_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(long content_id) {
		this.content_id = content_id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPresenter() {
		return presenter;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}

	@Override public String toString() {
		return "Content with id: " + content_id;
	}

	public static String deleteContent(long content_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("content_id", Long.toString(content_id)));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DELETE_CONTENT_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public static Content[] getContents() {
		// Sent HTTP GET request to query Content table
		System.out.println("Sent HTTP GET request to query Content table");

		URL url;
		InputStream inputStream = null;
		try {
			url = new URL(Constants.SELECT_ALL_CONTENT_RESOURCE);
			inputStream = url.openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String contentResponse = Util.getStringFromInputStream(inputStream);
		System.out.println(contentResponse);
		ContentCollection contentCollection = null;
		try {
			contentCollection = Marshal
					.unmarshal(ContentCollection.class, contentResponse);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (null != contentCollection && contentCollection.getContents().length > 0) {
			return contentCollection.getContents();
		}
		return null;
	}
}
