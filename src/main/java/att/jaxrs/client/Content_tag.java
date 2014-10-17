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
import org.json.JSONObject;

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
import java.util.Set;

/**
 * Created by ananthaneshan on 10/11/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Content_tag {
	private long tag_id;
	private long content_id;

	public Content_tag() {
	}

	public Content_tag(int tag_id, long content_id) {
		this.tag_id = tag_id;
		this.content_id = content_id;
	}

	public static String addContent_tag(Content_tag content_tag) {

		return addContent_tag(content_tag.getContent_id(), content_tag.getTag_id());
	}

	public static String addContent_tag(long content_id, long tag_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", Long.toString(content_id)));
		urlParameters.add(new BasicNameValuePair("tag_id", Long.toString(tag_id)));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.INSERT_CONTENT_TAG_RESOURCE);
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

	public static JSONObject addContentTags(Set<Long> tagIDs, long content_id) {
		//StringBuilder result = new StringBuilder("{ 'tags':{'");
		JSONObject jsonObject = new JSONObject();
		for (long tagID : tagIDs) {
			//result.append(tagID).append(",").append(addContent_tag(content_id, tagID));
			jsonObject.put(Long.toString(tagID), addContent_tag(content_id, tagID));
		}
		//result.append("'}}");
		//return result.toString();
		return jsonObject;
	}

	public static String deleteContentTags(Set<Long> tagIDs, long content_id) {

		StringBuilder result = new StringBuilder("{ 'tags':{'");
		for (long tagID : tagIDs) {
			result.append(tagID).append(",").append(deleteContent_tag(content_id, tagID));
		}
		result.append("'}}");
		return result.toString();
	}

	public static String deleteContent_tag(long content_id, long tag_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", Long.toString(content_id)));
		urlParameters.add(new BasicNameValuePair("tag_id", Long.toString(tag_id)));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DELETE_CONTENT_TAG_RESOURCE);
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

	public long getTag_id() {
		return tag_id;
	}

	public void setTag_id(long tag_id) {
		this.tag_id = tag_id;
	}

	public long getContent_id() {
		return content_id;
	}

	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}

	@Override public String toString() {
		return "Content Tag with content id: " + content_id + "tag id: " + tag_id;
	}

	public static Content_tag[] getContent_tags() {
		// Sent HTTP GET request to query Content_tag table
		System.out.println("Sent HTTP GET request to query Content_tag table");

		URL url;
		InputStream inputStream = null;
		try {
			url = new URL(Constants.SELECT_ALL_CONTENT_TAG_RESOURCE);
			inputStream = url.openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String contentTagResponse = Util.getStringFromInputStream(inputStream);
		System.out.println(contentTagResponse);
		Content_tagCollection contentTagCollection = null;
		try {
			contentTagCollection =
					Marshal.unmarshal(Content_tagCollection.class, contentTagResponse);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (null != contentTagCollection && contentTagCollection.getContentTags().length > 0) {
			return contentTagCollection.getContentTags();
		}
		return null;
	}
}
