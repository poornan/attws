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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ananthaneshan on 10/17/14.
 */
@XmlRootElement(name = "content_tagCollection")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content_tagCollection implements att.jaxrs.client.XmlRootElement<Content_tag> {
	@XmlElement(name = "content_tag")
	private Content_tag[] contentTags;

	public Content_tag[] getContentTags() {
		return contentTags;
	}

	public void setContentTags(Content_tag[] contentTags) {
		this.contentTags = contentTags;
	}

	public Content_tag[] getElements() {
		return contentTags;
	}

	public static Content_tagCollection getContentTagsWithID(long content_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("content_id", Long.toString(content_id)));

		Content_tagCollection collectionCT = new Content_tagCollection();

		try {
			System.out.println("invoking getTagsWithID: " + content_id);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SELECT_WITH_KEY_CONTENT_TAG_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			String resultStr = Util.getStringFromInputStream(result);

			collectionCT =
					Marshal.unmarshal(Content_tagCollection.class, resultStr);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return collectionCT;
	}
}
