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

import att.jaxrs.client.*;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/libraryService/")
public class LibraryService {

	@POST
	@Path("/library")
	@Produces("application/json")
	public Response addLibrary(@FormParam("category_id") int category_id,
	                           @FormParam("title") String title,
	                           @FormParam("published_date") String published_date,
	                           @FormParam("url") String url,
	                           @FormParam("presenter") String presenter,
	                           @FormParam("level") String level,
	                           @FormParam("reads") String reads,
	                           @FormParam("tag_id") String tag_id) {
		System.out.println("----invoking addLibrary, Library Title is: " + title);
		if ((null != title && title.isEmpty()) || null == title || category_id == 0) {
			return Response.status(400).header("Access-Control-Allow-Origin", "*").build();
		}
		final long content_id = Library.getExistingRecord(title, category_id);
		System.out.println("content id " + content_id);
		JSONObject response = new JSONObject();
		if (-1 != content_id) {
			JSONObject m1 = new JSONObject();
			m1.put("Library", "EXISTING_RECORD");
			m1.put("content_id", Long.toString(content_id));
			response.put("response", m1);
			return Response.notModified(response.toString()).header("Access-Control-Allow-Origin",
			                                                        "*").build();
		}

		JSONObject responseDS = new JSONObject();
		Library library = new Library(published_date, category_id, title, url);
		responseDS.put("Library", Library.addLibrary(library));
		library.setContent_id(Library.getExistingRecord(title, category_id));

		if (library.getCategory_id() == 4) {
			Webinar webinar = new Webinar(library.getContent_id(), presenter);
			responseDS.put("Webinar", Webinar.addWebinar(webinar));
		} else {
			Content content = new Content(library.getContent_id(), level, presenter, reads);
			responseDS.put("Content", Content.addContent(content));
		}

		if (null != tag_id && !tag_id.isEmpty()) {
			JSONObject responseTagDS = new JSONObject();
			StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
			while (stringtokenizer.hasMoreElements()) {
				String id = stringtokenizer.nextToken();
				Content_tag content_tag =
						new Content_tag(Integer.parseInt(id), library.getContent_id());
				responseTagDS.put(id, Content_tag.addContent_tag(content_tag));

			}
			responseDS.put("tags", responseTagDS);
		}
		response.put("response", responseDS);
		return Response.ok(response.toString()).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("/tags")
	@Produces("application/json")
	public Response getTags() {
		JSONObject jsonObject = new JSONObject();
		TagCollection tagCollection = Tag.getTags();
		System.out.println("running getTags: " + tagCollection.getTag().length);
		if (null != tagCollection.getTag() && tagCollection.getTag().length > 0) {
			for (Tag tag : tagCollection.getTag()) {
				jsonObject.put(tag.getTag_name(), tag.getTag_id());
			}
		}
		return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();
	}

	@POST
	@Path("/addTag")
	@Produces("application/json")
	public Response addTag(@FormParam("tag_name") String tag_name) {
		Tag tag = new Tag(tag_name);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", Tag.addTag(tag));
		return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();
	}

	@PUT
	@Path("/library")
	@Produces("application/json")
	public Response updateLibrary(@FormParam("category_id") int category_id,
	                              @FormParam("content_id") long content_id,
	                              @FormParam("title") String title,
	                              @FormParam("published_date") String published_date,
	                              @FormParam("url") String url,
	                              @FormParam("presenter") String presenter,
	                              @FormParam("level") String level,
	                              @FormParam("reads") String reads,
	                              @FormParam("tag_id") String tag_id) {
		if ((null != title && title.isEmpty()) || null == title || category_id == 0) {
			return Response.status(400).header("Access-Control-Allow-Origin", "*").build();
		}

		Library dbLibrary = Library.selectWithKeyLibraryResource(content_id);
		Library library = new Library(content_id, published_date, category_id, title, url);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("libraryUpdated", Library.updateLibrary(library));

		if ((dbLibrary.getCategory_id() == library.getCategory_id()) &&
		    dbLibrary.getCategory_id() == 4) {
			jsonObject.put("webinarUpdated",
			               Webinar.updateWebinar(new Webinar(content_id, presenter)));
		} else if (dbLibrary.getCategory_id() != library.getCategory_id() &&
		           dbLibrary.getCategory_id() == 4) {

			jsonObject.put("webinarDeleted", Webinar.deleteWebinar(content_id));
			jsonObject.put("contentAdded",
			               Content.addContent(new Content(content_id, level, presenter, reads)));
		} else if (dbLibrary.getCategory_id() != library.getCategory_id() &&
		           library.getCategory_id() == 4) {
			jsonObject.put("contentDeleted", Content.deleteContent(content_id));
			jsonObject.put("webinarAdded", Webinar.addWebinar(new Webinar(content_id, presenter)));
		} else {
			jsonObject.put("contentUpdated",
			               Content.updateContent(new Content(content_id, level, presenter, reads)));

		}

		if (null != tag_id && !tag_id.isEmpty()) {
			Set<Long> tagIDFromUser = new HashSet<Long>();
			Set<Long> tagIDFromDB = new HashSet<Long>();
			StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
			while (stringtokenizer.hasMoreElements()) {
				String id = stringtokenizer.nextToken();
				tagIDFromUser.add(Long.valueOf(id));

			}

			Tag[] tagsFromDB = Tag.getTagsWithID(content_id).getTag();
			for (Tag tag : tagsFromDB) {
				tagIDFromDB.add(tag.getTag_id());
			}

			for (Long tag : tagIDFromDB) {
				if (tagIDFromUser.contains(tag)) {
					tagIDFromDB.remove(tag);
				}
			}

			jsonObject.put("tagsAdded", Content_tag.addContentTags(tagIDFromUser, content_id));
			jsonObject.put("tagsDeleted", Content_tag.deleteContentTags(tagIDFromDB, content_id));
		}
		return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();
	}

	@DELETE
	@Path("/library")
	@Produces("application/json")
	public Response deleteLibrary(@FormParam("category_id") int category_id,
	                              @FormParam("content_id") long content_id,
	                              @FormParam("title") String title,
	                              @FormParam("published_date") String published_date,
	                              @FormParam("url") String url,
	                              @FormParam("presenter") String presenter,
	                              @FormParam("level") String level,
	                              @FormParam("reads") String reads,
	                              @FormParam("tag_id") String tag_id) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("libraryDeleted", Library.deleteLibrary(content_id));
		if (category_id == 4) {
			jsonObject.put("webinarDeleted", Webinar.deleteWebinar(content_id));
		} else {
			jsonObject.put("contentDeleted", Content.deleteContent(content_id));
		}

		Set<Long> tagIDs = new HashSet<Long>();
		StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
		while (stringtokenizer.hasMoreElements()) {
			String id = stringtokenizer.nextToken();
			tagIDs.add(Long.valueOf(id));

		}
		jsonObject.put("contentTagDeleted", Content_tag.deleteContentTags(tagIDs, content_id));
		return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();

	}

	@GET
	@Path("/library")
	@Produces("application/json")
	public Response getLibraries() {
		Library[] libraries = Library.getLibraries();
		/*Map<Long,Library> librariesMap = new HashMap<Long,Library>();
		if (null != libraries) {
			for (Library library : libraries) {
				librariesMap.put(library.getContent_id(),library);
			}
		}*/

		Content[] contents = Content.getContents();
		Map<Long, Content> contentsMap = new HashMap<Long, Content>();
		if (null != contents) {
			for (Content content : contents) {
				contentsMap.put(content.getContent_id(), content);
			}
		}

		Content_tag[] contentTags = Content_tag.getContent_tags();
		Map<Long, List<Content_tag>> contentTagsMap = new HashMap<Long, List<Content_tag>>();
		if (null != contentTags) {
			List<Content_tag> contentTagList;

			for (Content_tag contentTag : contentTags) {

				Set entrySet = contentTagsMap.entrySet();
				Iterator it = entrySet.iterator();
				System.out.println("  Object key  Object value");
				boolean hasKey = false;
				while (it.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) it.next();
					if (contentTag.getContent_id() == mapEntry.getKey()) {
						hasKey = true;
						contentTagList = contentTagsMap.get(mapEntry.getKey());
						contentTagList.add(contentTag);

					}
				}
				if (!hasKey) {
					contentTagList = new ArrayList<Content_tag>();
					contentTagList.add(contentTag);
					contentTagsMap.put(contentTag.getContent_id(), contentTagList);

				}
			}
		}

		Category[] categories = Category.getCategories();
		Map<Integer, Category> categoriesMap = new HashMap<Integer, Category>();
		if (null != categories) {
			for (Category category : categories) {
				categoriesMap.put(category.getCategory_id(), category);
			}
		}

		Webinar[] webinars = Webinar.getWebinar();
		Map<Long, Webinar> webinarsMap = new HashMap<Long, Webinar>();
		if (null != webinars) {
			for (Webinar webinar : webinars) {
				webinarsMap.put(webinar.getContent_id(), webinar);
			}
		}

		Tag[] tags = Tag.getALLTags();
		Map<Long, String> tagsMap = new HashMap<Long, String>();
		if (null != tags) {
			for (Tag tag : tags) {
				tagsMap.put(tag.getTag_id(), tag.getTag_name());
			}
		}
		List<LibraryDTO> libraryDTOList = new ArrayList<LibraryDTO>();
		for (Library library : libraries) {
			LibraryDTO libraryDTO = new LibraryDTO();
			libraryDTO.setContent_id(library.getContent_id());
			libraryDTO.setUrl(library.getUrl());
			libraryDTO.setTitle(library.getTitle());
			libraryDTO.setCategory(categoriesMap.get(library.getCategory_id()));

			if (library.getContent_id() == 4) {
				libraryDTO.setWebinar(webinarsMap.get(library.getContent_id()));

			} else {
				libraryDTO.setContent(contentsMap.get(library.getContent_id()));
			}

			List<Content_tag> contentTagList = contentTagsMap.get(library.getContent_id());
			for (Content_tag content_tag : contentTagList) {
				libraryDTO.setTag(content_tag.getTag_id(), tagsMap.get(content_tag.getTag_id()));

			}
			libraryDTOList.add(libraryDTO);

		}

		return Response.ok(createLibrariesJson(libraryDTOList).toString()).build();
	}

	private JSONObject createLibraryJson(LibraryDTO libraryDTO) {
		JSONObject library = new JSONObject();

		library.put("url", libraryDTO.getUrl());
		library.put("title", libraryDTO.getTitle());

		JsonObject category = new JsonObject();
		category.addProperty("categoryID", libraryDTO.getCategory().getCategory_id());
		category.addProperty("categoryName", libraryDTO.getCategory().getCategory_name());
		library.put("category", category);

		//JsonObject tags = libraryDTO.getTag();
		library.put("tags", libraryDTO.getTag());
		//tags.addProperty("tagID", libraryDTO.getTag());

		if (libraryDTO.getCategory().getCategory_id() == 4) {
			JsonObject webinar = new JsonObject();
			webinar.addProperty("presenter", libraryDTO.getWebinar().getPresenter());
			library.put("webinar", webinar);

		} else {
			JsonObject content = new JsonObject();
			content.addProperty("level", libraryDTO.getContent().getLevel());
			content.addProperty("presenter", libraryDTO.getContent().getPresenter());
			content.addProperty("reads", libraryDTO.getContent().getReads());
			library.put("content", content);
		}

		return library;
	}

	private JSONObject createLibrariesJson(List<LibraryDTO> libraries) {
		JSONObject librariesJSON = new JSONObject();
		for (LibraryDTO dto : libraries) {
			librariesJSON.put(Long.toString(dto.getContent_id()), createLibraryJson(dto));
		}
		JSONObject result = new JSONObject();
		result.put("libraries", librariesJSON);
		return result;
	}

}
