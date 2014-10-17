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

import att.jaxrs.util.Marshal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ananthaneshan on 10/7/14.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TagCollection implements att.jaxrs.client.XmlRootElement<Tag> {
	@XmlElement
	private Tag[] tag;

	public static void main(String[] args) {

		TagCollection tagCollection = Marshal.unmarshal(TagCollection.class,
		                                                "<tagCollection ><tag><tag_id >1</tag_id><tag_name >Esb</tag_name></tag><tag><tag_id >2</tag_id><tag_name >Cloud App</tag_name></tag></tagCollection>");
		System.out.println(tagCollection.getTag().length);

	}

	public Tag[] getTag() {
		return tag;
	}

	public void setTag(Tag[] tags) {
		this.tag = tags;
	}

	public Tag[] getElements() {
		return tag;
	}
}
