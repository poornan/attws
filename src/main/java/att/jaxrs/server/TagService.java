/*
 * Copyright 2011-2012 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package att.jaxrs.server;

import att.jaxrs.client.TagCollection;
import att.jaxrs.util.Constants;
import att.jaxrs.util.Marshal;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/tagService/")
public class TagService {

	@GET
	@Path("/tags")
	public Response getTags() {
		GetMethod get = new GetMethod(Constants.SELECT_ALL_TAG_OPERATION);
		TagCollection tag = null;
		try {

			HttpClient httpClient = new HttpClient();
			try {
				int result = httpClient.executeMethod(get);
				System.out.println(Constants.RESPONSE_STATUS_CODE + result);
				tag = Marshal.unmarshal(TagCollection.class, get.getResponseBodyAsStream());

			} finally {
				get.releaseConnection();

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return Response.ok(tag).build();

	}

	@GET
	@Path("/tags/{name}/")
	public Response getTag(@PathParam("name") Long name) {
		System.out.println("----invoking getTag, tag id is: " + name);

		GetMethod get = new GetMethod(Constants.SELECT_WITH_NAME_TAG_RESOURCE + name);

		TagCollection tag = new TagCollection();
		try {

			HttpClient httpClient = new HttpClient();
			try {
				int result = httpClient.executeMethod(get);
				System.out.println(Constants.RESPONSE_STATUS_CODE + result);
				tag = Marshal.unmarshal(TagCollection.class, get.getResponseBodyAsStream());

			} finally {
				get.releaseConnection();
			}
		} catch (Exception e) {

		}
		return Response.ok(tag).build();
	}

}
