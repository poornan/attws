package att.jaxrs.server;

import att.jaxrs.client.*;

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
	public Response addLibrary(@FormParam("category_id") Integer category_id,
	                           @FormParam("title") String title,
	                           @FormParam("published_date") String published_date,
	                           @FormParam("url") String url,
	                           @FormParam("presenter") String presenter,
	                           @FormParam("level") String level,
	                           @FormParam("reads") String reads,
	                           @FormParam("tag_id") String tag_id) {
		System.out.println("----invoking addLibrary, Library Title is: " + title);
		StringBuilder response = new StringBuilder("{'response':{");

		Library library = new Library(published_date, category_id, title, url);
		response.append("'Library': '").append(Library.addLibrary(library)).append("', ");

		library.setContent_id(Library.getExistingRecord(title, category_id));

		if (library.getCategory_id() == 4) {
			Webinar webinar = new Webinar(library.getContent_id(), presenter);
			response.append("'Webinar': '").append(Webinar.addWebinar(webinar)).append("' , ");
		} else {
			Content content = new Content(library.getContent_id(), level, presenter, reads);
			response.append("'Content': '").append(Content.addContent(content)).append("' , ");
		}

		response.append("'Content_tag': { ");
		StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
		while (stringtokenizer.hasMoreElements()) {
			String id = stringtokenizer.nextToken();
			Content_tag content_tag =
					new Content_tag(Integer.parseInt(id), library.getContent_id());
			response.append("'").append(id).append("':{ 'result': '")
			        .append(Content_tag.addContent_tag(content_tag)).append("'} ");
			if (stringtokenizer.hasMoreElements()) {
				response.append(", ");
			} else {
				response.append("}}} ");
			}
		}

		return Response.ok(response.toString().replaceAll("<[^>]+>|\\s", "")
		                           .replaceAll("'", "\"")).build();
	}

	@GET
	@Path("/tags")
	public Response getTags() {
		return Tag.getTags();
	}

	@POST
	@Path("/addTag")
	@Produces("application/json")
	public Response addTag(@FormParam("tag_name") String tag_name) {
		Tag tag = new Tag(tag_name);
		String response = "{'response':{";
		response = response.concat("'Tag': '" + Tag.addTag(tag) + "'}}");
		return Response.ok(response.replaceAll("<[^>]+>|\\s", "")
		                           .replaceAll("'", "\"")).build();
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

		Library dbLibrary = Library.selectWithKeyLibraryResource(content_id);
		Library library = new Library(content_id, published_date, category_id, title, url);
		StringBuilder response = new StringBuilder("{'response':{");
		response.append("'Library': '").append(Library.updateLibrary(library)).append("', ");

		if ((dbLibrary.getCategory_id() == library.getCategory_id()) &&
		    dbLibrary.getCategory_id() == 4) {
			response.append(Webinar.updateWebinar(new Webinar(content_id, presenter)));
		} else if (dbLibrary.getCategory_id() != library.getCategory_id() &&
		           dbLibrary.getCategory_id() == 4) {
			response.append(Webinar.deleteWebinar(content_id));
			response.append(Content.addContent(new Content(content_id, level, presenter, reads)));
		} else if (dbLibrary.getCategory_id() != library.getCategory_id() &&
		           library.getCategory_id() == 4) {
			response.append(Content.deleteContent(content_id));
			response.append(Webinar.addWebinar(new Webinar(content_id, presenter)));
		} else {
			response.append(
					Content.updateContent(new Content(content_id, level, presenter, reads)));

		}

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

		response.append(Content_tag.addContentTags(tagIDFromUser, content_id));
		response.append(Content_tag.deleteContentTags(tagIDFromDB, content_id));
		return Response.ok(response.toString().replaceAll("<[^>]+>|\\s", "")
		                           .replaceAll("'", "\"")).build();
	}

	@PUT
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
		StringBuilder response = new StringBuilder("{'ContentTag':'");
		response.append(Library.deleteLibrary(content_id));
		if (category_id == 4) {
			response.append(Webinar.deleteWebinar(content_id));
		}
		response.append(Content.deleteContent(content_id));

		Set<Long> tagIDs = new HashSet<Long>();
		StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
		while (stringtokenizer.hasMoreElements()) {
			String id = stringtokenizer.nextToken();
			tagIDs.add(Long.valueOf(id));

		}
		response.append(Content_tag.deleteContentTags(tagIDs, content_id));
		return Response.ok(response.toString().replaceAll("<[^>]+>|\\s", "")
		                           .replaceAll("'", "\"")).build();
	}

}
