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

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Tag {

	private long tag_id;

	private String tag_name;

	public Tag() {
	}

	public Tag(long tag_id, String tag_name) {
		this.tag_id = tag_id;
		this.tag_name = tag_name;
	}

	public Tag(String tag_name) {
		this.tag_name = tag_name;
	}

	public static String addTag(Tag tag) {
		final long tag_id = getExistingRecord(tag.getTag_name());
		if (-1L != tag_id) {
			return "{response:{Tag: 'EXISTING_RECORD'},tag_id:" + tag_id + "}";
		}
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("tag_name", tag.getTag_name()));

		HttpResponse result;
		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.INSERT_TAG_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public static long getExistingRecord(String tag_name) {

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("tag_name", tag_name));

		HttpResponse result;
		String resultStr;
		TagCollection tagCollection = new TagCollection();

		try {
			System.out.println("invoking last added: " + tag_name);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SELECT_LAST_ADDED_TAG_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result);

			tagCollection =
					Marshal.unmarshal(TagCollection.class, resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();

		}

		if (null != tagCollection.getTag()) {
			return tagCollection.getTag()[0].getTag_id();
		}
		return -1L;
	}

	public static TagCollection getTags() {
		GetMethod get = new GetMethod(Constants.SELECT_ALL_TAG_OPERATION);
		TagCollection tag = new TagCollection();

		HttpClient httpClient = new HttpClient();
		try {
			int result = httpClient.executeMethod(get);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			tag = Marshal.unmarshal(TagCollection.class, get.getResponseBodyAsStream());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();

		}
		return tag;

	}

	public static Tag[] getALLTags() {
		GetMethod get = new GetMethod(Constants.SELECT_ALL_TAG_OPERATION);
		TagCollection tag = new TagCollection();

		HttpClient httpClient = new HttpClient();
		try {
			int result = httpClient.executeMethod(get);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			tag = Marshal.unmarshal(TagCollection.class, get.getResponseBodyAsStream());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();

		}
		if (null != tag.getTag() && tag.getTag().length > 0) {
			return tag.getTag();
		} else {
			System.out.println("unmarshalling returned empty collection");
		}
		return null;

	}

	public long getTag_id() {
		return tag_id;
	}

	public void setTag_id(long id) {
		this.tag_id = id;
	}

	public String getTag_name() {
		return tag_name;
	}

	public void setTag_name(String name) {
		this.tag_name = name;
	}

	@Override public String toString() {
		return "TAG: id=" + getTag_id() + ", name=" + getTag_name();
	}

}
