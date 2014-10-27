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
import att.jaxrs.util.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Contains all Restful service methods.
 */
@Path("/libraryService/")
public class LibraryService {
	//	<FormParams>
	public static final String CATEGORY_ID = "category_id";
	public static final String TITLE = "title";
	public static final String PUBLISHED_DATE = "published_date";
	public static final String URL = "url";
	public static final String PRESENTER = "presenter";
	public static final String LEVEL = "level";
	public static final String READS = "reads";
	public static final String TAG_ID = "tag_id";
	public static final String CONTENT_ID = "content_id";
	public static final String TAG_NAME = "tag_name";
	//	</FormParams>

	//	<ServiceURI>
	public static final String LIBRARY_URI = "/library";
	public static final String TAGS = "/tags";
	public static final String ADD_TAG = "/addTag";
	//	</ServiceURI>

	public static final String EMPTY_STRING = "";
	public static final String REGEX = "<.*?>";

	/**
	 * Create Library API call.
	 *
	 * @param category_id    Category ID.
	 * @param title          Title.
	 * @param published_date Published Date.
	 * @param url            URL of the content.
	 * @param presenter      Presenter Names.
	 * @param level          Level.
	 * @param reads          No. of Reads.
	 * @param tag_id         Tag IDs separated by comma.
	 * @return Query status as application/json.
	 */
	@POST
	@Path(LIBRARY_URI)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Constants.ROLE_ADMIN)
	public String addLibrary(@FormParam(CATEGORY_ID) int category_id,
	                         @FormParam(TITLE) String title,
	                         @FormParam(PUBLISHED_DATE) String published_date,
	                         @FormParam(URL) String url,
	                         @FormParam(PRESENTER) String presenter,
	                         @FormParam(LEVEL) String level,
	                         @FormParam(READS) String reads,
	                         @FormParam(TAG_ID) String tag_id) {
		System.out.println("----invoking addLibrary, Library Title is: " + title);
		System.out.println(reads);
		if ((null != title && title.isEmpty()) || null == title || category_id == 0) {
			//			return Response.status(400).header("Access-Control-Allow-Origin", "*").build();
			return "{response:{},status:400}";
		}
		if (null == published_date) {
			published_date = EMPTY_STRING;
		}
		if (null == url) {
			url = EMPTY_STRING;
		}
		if (null == reads) {
			reads = EMPTY_STRING;
		}
		if (null == tag_id) {
			tag_id = EMPTY_STRING;
		}
		if (null == presenter) {
			presenter = EMPTY_STRING;
		}
		final long content_id = Library.getExistingRecord(title, category_id);
		System.out.println("content id " + content_id);
		JSONObject response = new JSONObject();
		if (-1 != content_id) {
			JSONObject m1 = new JSONObject();
			m1.put("Library", "EXISTING_RECORD");
			m1.put(CONTENT_ID, Long.toString(content_id));
			response.put("response", m1);
			response.put("status", 304);
			/*return Response.notModified(response.toString()).header("Access-Control-Allow-Origin",
			                                                        "*").build();*/
			return response.toString();
		}

		JSONObject responseDS = new JSONObject();
		Library library = new Library(published_date, category_id, title, url);
		responseDS.put("Library", Library.addLibrary(library).replaceAll(REGEX, EMPTY_STRING));
		library.setContent_id(Library.getExistingRecord(title, category_id));

		if (library.getCategory_id() == 4) {
			Webinar webinar = new Webinar(library.getContent_id(), presenter);
			responseDS.put("Webinar", Webinar.addWebinar(webinar).replaceAll(REGEX, EMPTY_STRING));
		} else {
			Content content = new Content(library.getContent_id(), level, presenter, reads);
			responseDS.put("Content", Content.addContent(content).replaceAll(REGEX, EMPTY_STRING));
		}

		if (!tag_id.isEmpty()) {
			ArrayList<JSONObject> responseTagDS = new ArrayList<JSONObject>();
			StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
			while (stringtokenizer.hasMoreElements()) {
				String id = stringtokenizer.nextToken();
				Content_tag content_tag =
						new Content_tag(Integer.parseInt(id), library.getContent_id());
				JSONObject map = new JSONObject();
				map.put("tagID", Long.valueOf(id));
				map.put("result", Content_tag.addContent_tag(content_tag).replaceAll(REGEX,
				                                                                     EMPTY_STRING));
				responseTagDS.add(map);

			}
			responseDS.put("tags", responseTagDS);
		}
		response.put("response", responseDS);
		response.put("status", 200);
		//		return Response.ok(response.toString()).header("Access-Control-Allow-Origin", "*").build();
		return response.toString();
	}

	/**
	 * Get all Tags API method.
	 *
	 * @return tags as application/json
	 */
	@GET
	@Path(TAGS)
	@Produces(MediaType.APPLICATION_JSON)
	public String getTags() {
		JSONObject jsonObject = new JSONObject();
		TagCollection tagCollection = Tag.getTags();
		System.out.println("running getTags: " + tagCollection.getTag().length);
		if (null != tagCollection.getTag() && tagCollection.getTag().length > 0) {
			for (Tag tag : tagCollection.getTag()) {
				jsonObject.put(tag.getTag_name(), tag.getTag_id());
			}
		}
		/*return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();*/
		return jsonObject.toString();
	}

	/**
	 * API method for adding Tag.
	 *
	 * @param tag_name Name of the tag.
	 * @return Query status as application/json.
	 */
	@POST
	@Path(ADD_TAG)
	@Produces(MediaType.APPLICATION_JSON)
	public String addTag(@FormParam(TAG_NAME) String tag_name) {
		Tag tag = new Tag(tag_name);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", Tag.addTag(tag));
		/*return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();*/
		return jsonObject.toString();
	}

	/**
	 * API method for Update library.
	 *
	 * @param category_id    Category ID.
	 * @param content_id     ID of the library to be updated.
	 * @param title          Title.
	 * @param published_date Published Date.
	 * @param url            URL of the content.
	 * @param presenter      Presenter Names.
	 * @param level          Level.
	 * @param reads          No. of Reads.
	 * @param tag_id         Tag IDs separated by comma.
	 * @return Query status as application/json.
	 */
	@PUT
	@Path(LIBRARY_URI)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Constants.ROLE_ADMIN)
	public String updateLibrary(@FormParam(CATEGORY_ID) int category_id,
	                            @FormParam(CONTENT_ID) long content_id,
	                            @FormParam(TITLE) String title,
	                            @FormParam(PUBLISHED_DATE) String published_date,
	                            @FormParam(URL) String url,
	                            @FormParam(PRESENTER) String presenter,
	                            @FormParam(LEVEL) String level,
	                            @FormParam(READS) String reads,
	                            @FormParam(TAG_ID) String tag_id) {
		if ((null != title && title.isEmpty()) || null == title || category_id == 0 ||
		    content_id == 0) {
			//			return Response.status(400).header("Access-Control-Allow-Origin", "*").build();
			return "{response:{},status:400}";
		}
		if (null == published_date) {
			published_date = EMPTY_STRING;
		}
		if (null == url) {
			url = EMPTY_STRING;
		}
		if (null == reads) {
			reads = EMPTY_STRING;
		}
		if (null == tag_id) {
			tag_id = EMPTY_STRING;
		}
		if (null == presenter) {
			presenter = EMPTY_STRING;
		}

		Library dbLibrary = Library.selectWithKeyLibraryResource(content_id);
		Library library = new Library(content_id, published_date, category_id, title, url);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("libraryUpdated", Library.updateLibrary(library).replaceAll(REGEX,
		                                                                           EMPTY_STRING));

		if ((dbLibrary.getCategory_id() == library.getCategory_id()) &&
		    dbLibrary.getCategory_id() == 4) {
			jsonObject.put("webinarUpdated",
			               Webinar.updateWebinar(new Webinar(content_id, presenter))
			                      .replaceAll(REGEX, EMPTY_STRING));
		} else if (dbLibrary.getCategory_id() != library.getCategory_id() &&
		           dbLibrary.getCategory_id() == 4) {

			jsonObject.put("webinarDeleted", Webinar.deleteWebinar(content_id).replaceAll(REGEX,
			                                                                              EMPTY_STRING));
			jsonObject.put("contentAdded",
			               Content.addContent(new Content(content_id, level, presenter, reads))
			                      .replaceAll(
					                      REGEX, EMPTY_STRING));
		} else if (dbLibrary.getCategory_id() != library.getCategory_id() &&
		           library.getCategory_id() == 4) {
			jsonObject.put("contentDeleted", Content.deleteContent(content_id).replaceAll(REGEX,
			                                                                              EMPTY_STRING));
			jsonObject.put("webinarAdded",
			               Webinar.addWebinar(new Webinar(content_id, presenter)).replaceAll(
					               REGEX, EMPTY_STRING));
		} else {
			jsonObject.put("contentUpdated",
			               Content.updateContent(new Content(content_id, level, presenter, reads))
			                      .replaceAll(
					                      REGEX, EMPTY_STRING));

		}

		Set<Long> tagIDFromDB = new HashSet<Long>();
		Set<Long> tagIDFromUser = new HashSet<Long>();
		if (!tag_id.isEmpty()) {
			StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
			while (stringtokenizer.hasMoreElements()) {
				String id = stringtokenizer.nextToken();
				tagIDFromUser.add(Long.valueOf(id));

			}
		}
		System.out.println(tagIDFromUser.toString());
		Content_tagCollection collectionCT =
				Content_tagCollection.getContentTagsWithID(content_id);

		Content_tag[] tagsFromDB;
		if (null != collectionCT.getContentTags()) {
			tagsFromDB = collectionCT.getContentTags();
			for (Content_tag tag : tagsFromDB) {
				tagIDFromDB.add(tag.getTag_id());
			}

			Iterator<Long> longIteratorDB = tagIDFromDB.iterator();
			Iterator<Long> longIteratorUser = tagIDFromUser.iterator();

			while (longIteratorDB.hasNext()) {
				long id = longIteratorDB.next();

				if (tagIDFromUser.contains(id)) {
					longIteratorDB.remove();

					while (longIteratorUser.hasNext()) {
						long id1 = longIteratorUser.next();

						if (id == id1) {
							longIteratorUser.remove();

						}
					}

				}
			}
		}
		System.out.println(tagIDFromUser.toString());
		jsonObject
				.put("tagsDeleted", Content_tag.deleteContentTags(tagIDFromDB, content_id));
		jsonObject.put("tagsAdded", Content_tag.addContentTags(tagIDFromUser, content_id));
		/*return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();*/
		jsonObject.put("status", 200);
		return jsonObject.toString();
	}

	/**
	 * API method for Delete library.
	 *
	 * @param content_id ID of the library to be deleted.
	 * @return status as application/json.
	 */
	@DELETE
	@Path(LIBRARY_URI)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Constants.ROLE_ADMIN)
	public String deleteLibrary(@FormParam(CONTENT_ID) long content_id,
	                            @FormParam(CATEGORY_ID) int category_id,
	                            @FormParam(TAG_ID) String tag_id) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("libraryDeleted", Library.deleteLibrary(content_id).replaceAll(REGEX,
		                                                                              EMPTY_STRING));
		if (category_id == 4) {
			jsonObject.put("webinarDeleted", Webinar.deleteWebinar(content_id).replaceAll(REGEX,
			                                                                              EMPTY_STRING));
		} else {
			jsonObject.put("contentDeleted", Content.deleteContent(content_id).replaceAll(REGEX,
			                                                                              EMPTY_STRING));
		}

		Set<Long> tagIDs = new HashSet<Long>();
		if (null != tag_id && !tag_id.isEmpty()) {
			StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
			while (stringtokenizer.hasMoreElements()) {
				String id = stringtokenizer.nextToken();
				tagIDs.add(Long.valueOf(id));

			}
		}
		jsonObject.put("contentTagDeleted", Content_tag.deleteContentTags(tagIDs, content_id));
		/*return Response.ok(jsonObject.toString()).header("Access-Control-Allow-Origin", "*")
		               .build();*/
		jsonObject.put("status", 200);
		return jsonObject.toString();

	}

	/**
	 * API method for retrieve all library contents.
	 *
	 * @return library content as application/json.
	 */
	@GET
	@Path(LIBRARY_URI)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ Constants.ROLE_USER, Constants.ROLE_ADMIN })
	public String getLibraries() {
		Library[] libraries = Library.getLibraries();

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
			libraryDTO.setPublishedDate(library.getPublished_date());
			libraryDTO.setCategory(categoriesMap.get(library.getCategory_id()));

			if (library.getContent_id() == 4) {
				libraryDTO.setWebinar(webinarsMap.get(library.getContent_id()));

			} else {
				libraryDTO.setContent(contentsMap.get(library.getContent_id()));
			}

			List<Content_tag> contentTagList = contentTagsMap.get(library.getContent_id());
			if (null != contentTagList) {

				for (Content_tag content_tag : contentTagList) {
					libraryDTO
							.setTag(content_tag.getTag_id(), tagsMap.get(content_tag.getTag_id()));

				}
			}
			libraryDTOList.add(libraryDTO);

		}

		//return Response.ok(createLibrariesJson(libraryDTOList).toString()).build();
		return createLibrariesJson(libraryDTOList).toString();
	}

	/**
	 * Create library JSON object for transmission.
	 *
	 * @param libraryDTO LibraryDTO.
	 * @return library as application/json.
	 */
	private JSONObject createLibraryJson(LibraryDTO libraryDTO) {
		JSONObject library = new JSONObject();

		library.put(URL, libraryDTO.getUrl());
		library.put(TITLE, libraryDTO.getTitle());
		library.put("publishedDate", libraryDTO.getPublishedDate());
		library.put("contentID", libraryDTO.getContent_id()); //contentID added

		JSONObject category = new JSONObject();
		category.put("categoryID", libraryDTO.getCategory().getCategory_id());
		category.put("categoryName", libraryDTO.getCategory().getCategory_name());
		library.put("category", category);
		//Map<String, String>[] tags = new HashMap[tag.size()];
		//tags = libraryDTO.getTag().toArray(tags);
		library.put("tags", libraryDTO.getTag());

		if (libraryDTO.getCategory().getCategory_id() == 4 && null != libraryDTO.getWebinar()) {
			JSONObject webinar = new JSONObject();
			webinar.put(PRESENTER, libraryDTO.getWebinar().getPresenter());
			library.put("webinar", webinar);

		} else if (null != libraryDTO.getContent()) {
			JSONObject content = new JSONObject();
			content.put(LEVEL, libraryDTO.getContent().getLevel());
			content.put(PRESENTER, libraryDTO.getContent().getPresenter());
			content.put(READS, libraryDTO.getContent().getReads());
			library.put("content", content);
		}

		return library;
	}

	/**
	 * Create libraries JSON object for transmission.
	 *
	 * @param libraries LibraryDTO list.
	 * @return Libraries as application/json.
	 */
	private JSONArray createLibrariesJson(List<LibraryDTO> libraries) {
		//		JSONObject librariesJSON = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (LibraryDTO dto : libraries) {
			//			librariesJSON.put(Long.toString(dto.getContent_id()), createLibraryJson(dto));
			jsonArray.put(createLibraryJson(dto));
		}
		//		JSONObject result = new JSONObject();
		//		result.put("libraries", librariesJSON);
		return jsonArray;
	}

}
