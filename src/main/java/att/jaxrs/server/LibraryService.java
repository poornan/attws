package att.jaxrs.server;

import att.jaxrs.client.*;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by prindu on 08/10/14.
 */
@Path("/libraryService/")
public class LibraryService {

	@POST
	@Path("/library/")
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
			return Response.status(400).build();
		}
		final long content_id = Library.getExistingRecord(title, category_id);
		System.out.println("content id " + content_id);
		JSONObject response = new JSONObject();
		if (-1 != content_id) {
			JSONObject m1 = new JSONObject();
			m1.put("Library", "EXISTING_RECORD");
			m1.put("content_id", Long.toString(content_id));
			response.put("response", m1);
			return Response.notModified(response.toString()).build();
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
		return Response.ok(jsonObject.toString()).build();
	}

	@POST
	@Path("/addTag")
	@Produces("application/json")
	public Response addTag(@FormParam("tag_name") String tag_name) {
		Tag tag = new Tag(tag_name);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", Tag.addTag(tag));
		return Response.ok(jsonObject.toString()).build();
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
			return Response.status(400).build();
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
		return Response.ok(jsonObject.toString()).build();
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
		return Response.ok(jsonObject.toString()).build();
	}

}
