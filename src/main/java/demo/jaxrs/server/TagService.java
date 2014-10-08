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

package demo.jaxrs.server;

import demo.jaxrs.client.TagCollection;
import demo.jaxrs.util.Marshal;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/tagservice/")
public class TagService {
	private static String SERVICE_URL =
			"https://appserver.dev.cloud.wso2.com/services/t/naasheerwso2/attdataservice-default-SNAPSHOT/";

	@GET
	@Path("/tags")
	public Response getTags() {
		GetMethod get = new GetMethod(SERVICE_URL + "select_all_tag_operation");
		TagCollection tag = null;
		try {

			HttpClient httpClient = new HttpClient();
			try {
				int result = httpClient.executeMethod(get);
				System.out.println("Response status code: " + result);
				System.out.print("Response body: ");
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
	@Path("/tags/{id}/")
	public Response getTag(@PathParam("id") Long id) {
		System.out.println("----invoking getTag, tag id is: " + id);

		GetMethod get = new GetMethod(SERVICE_URL + "select_with_key_tag_operation?tag_id=" + id);

		TagCollection tag = new TagCollection();
		try {

			HttpClient httpClient = new HttpClient();
			try {
				int result = httpClient.executeMethod(get);
				System.out.println("Response status code: " + result);
				System.out.print("Response body: ");
				tag = Marshal.unmarshal(TagCollection.class, get.getResponseBodyAsStream());

			} finally {
				get.releaseConnection();
			}
		} catch (Exception e) {

		}
		return Response.ok(tag).build();
	}

}
