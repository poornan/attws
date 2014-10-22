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

package att.jaxrs.server;

import att.jaxrs.client.Category;
import att.jaxrs.client.Content;
import att.jaxrs.client.Webinar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ananthaneshan on 10/17/14.
 */
public class LibraryDTO {
	private long content_id;
	private String url;
	private String title;
	private String publishedDate;
	private Category category;
	//private Map<Long, String> tag = new HashMap<Long, String>();
	private ArrayList<Map<String, String>> tag = new ArrayList<Map<String, String>>();
	private Webinar webinar;
	private Content content;

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public long getContent_id() {
		return content_id;
	}

	public void setContent_id(long content_id) {
		this.content_id = content_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ArrayList<Map<String, String>> getTag() {

		return tag;
	}

	public void setTag(long tagID, String tagName) {
		Map<String, String> tag1 = new HashMap<String, String>();
		tag1.put("tagID", Long.toString(tagID));
		tag1.put("tagName", tagName);
		tag.add(tag1);
	}

	public Webinar getWebinar() {
		return webinar;
	}

	public void setWebinar(Webinar webinar) {
		this.webinar = webinar;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
}
