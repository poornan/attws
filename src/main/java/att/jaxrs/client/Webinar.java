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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Webinar {
	private long content_id;
	private String presenter;

	public Webinar() {
	}

	public Webinar(long content_id, String presenter) {
		this.content_id = content_id;
		this.presenter = presenter;
	}

	public static String addWebinar(Webinar webinar) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", webinar.getContent_id().toString()));
		urlParameters.add(new BasicNameValuePair("presenter", webinar.getPresenter()));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.INSERT_WEBINAR_RESOURCE);
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

	public static String updateWebinar(Webinar webinar) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", webinar.getContent_id().toString()));
		urlParameters.add(new BasicNameValuePair("presenter", webinar.getPresenter()));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.UPDATE_WEBINAR_RESOURCE);
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

	public static String deleteWebinar(long content_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", Long.toString(content_id)));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DELETE_WEBINAR_RESOURCE);
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

	public static Webinar[] getWebinar() {
		// Sent HTTP GET request to query Webinar table
		GetMethod get = new GetMethod(Constants.SELECT_ALL_WEBINAR_OPERATION);
		WebinarCollection collection = new WebinarCollection();

		HttpClient httpClient = new HttpClient();
		try {
			int result = httpClient.executeMethod(get);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			collection = Marshal.unmarshal(WebinarCollection.class, get.getResponseBodyAsStream());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();

		}
		if (null != collection.getWebinar() && collection.getWebinar().length > 0) {
			return collection.getWebinar();
		} else {
			System.out.println("unmarshalling returned empty collection");
		}
		return null;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(long content_id) {
		this.content_id = content_id;
	}

	public String getPresenter() {
		return presenter;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}

	@Override public String toString() {
		return "Webinar with id: " + content_id;
	}
}
