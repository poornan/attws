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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@Path("/tagservice/")
public class TagService {
	private static String SERVICE_URL =
			"https://appserver.dev.cloud.wso2.com/services/t/naasheerwso2/attdataservice-default-SNAPSHOT/";
	long currentId = 001;
	Map<Long, Tag> tags = new HashMap<Long, Tag>();

	public TagService() {
		init();
	}

	private static String getStringFromInputStream(InputStream in) throws Exception {
		CachedOutputStream bos = new CachedOutputStream();
		IOUtils.copy(in, bos);
		in.close();
		bos.close();
		return bos.getOut().toString();
	}

	public static void main(String[] args) throws Exception {

		GetMethod get = new GetMethod(SERVICE_URL + "select_all_tag_operation");
		get.addRequestHeader("accept", "application/json");

		HttpClient httpClient = new HttpClient();
		try {
			int result = httpClient.executeMethod(get);
			System.out.println("Response status code: " + result);
			System.out.print("Response body: ");
			System.out.println(get.getResponseBodyAsString());
		} finally {

			get.releaseConnection();
		}
		System.out.println("getTag");
		GetMethod get1 = new GetMethod(SERVICE_URL + "select_all_tag_operation");

		HttpClient httpClient1 = new HttpClient();
		try {
			int result = httpClient1.executeMethod(get1);
			System.out.println("Response status code: " + result);
			System.out.print("Response body: ");
			System.out.println(get1.getResponseBodyAsString());
		} finally {
			get1.releaseConnection();
		}

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TagCollection.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StringReader xml = new StringReader(get1.getResponseBodyAsString());
			TagCollection tag = (TagCollection) jaxbUnmarshaller.unmarshal(xml);
			System.out.println((tag.getTag())[0].toString());

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("/tags")
	public Response getTags() {

		GetMethod get = new GetMethod(SERVICE_URL + "select_all_tag_operation");

		Tag tag = null;
		try {
			System.out.println("getTags");

			HttpClient httpClient = new HttpClient();
			try {
				int result = httpClient.executeMethod(get);
				System.out.println("Response status code: " + result);
				System.out.print("Response body: ");

				JAXBContext jaxbContext = JAXBContext.newInstance(Tag.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				StringReader xml = new StringReader(get.getResponseBodyAsString());
				tag = (Tag) jaxbUnmarshaller.unmarshal(xml);
			} finally {
				get.releaseConnection();
			}
		} catch (Exception e) {

		}
		return Response.ok(tag).build();
	}

	@GET
	@Path("/tags/{id}/")
	public Response getTag(@PathParam("id") Long id) {
		System.out.println("----invoking getTag, tag id is: " + id);

		GetMethod get = new GetMethod(SERVICE_URL + "select_with_key_tag_operation?tag_id=" + id);

		Tag tag = null;
		try {
			System.out.println("getTag");

			HttpClient httpClient = new HttpClient();
			try {
				int result = httpClient.executeMethod(get);
				System.out.println("Response status code: " + result);
				System.out.print("Response body: ");

				JAXBContext jaxbContext = JAXBContext.newInstance(Tag.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				StringReader xml = new StringReader(get.getResponseBodyAsString());
				tag = (Tag) jaxbUnmarshaller.unmarshal(xml);

			} finally {
				get.releaseConnection();
			}
		} catch (Exception e) {

		}
		return Response.ok(tag).build();
	}

	final void init() {
		Tag c = new Tag();
		c.setTag_name("book");
		c.setTag_id(currentId);
		tags.put(c.getTag_id(), c);
	}
}
