package demo.jaxrs.server;

import demo.jaxrs.client.Content;
import demo.jaxrs.client.Content_tag;
import demo.jaxrs.client.Library;
import demo.jaxrs.client.Webinar;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.StringTokenizer;

/**
 * Created by prindu on 08/10/14.
 */
@Path("/libraryService/")
public class LibraryService {
	private static String SERVICE_URL =
			"https://appserver.dev.cloud.wso2.com/services/t/naasheerwso2/attdataservice-default-SNAPSHOT/";

	@POST
	@Path("/Library/")
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
		StringBuffer response = new StringBuffer("{'response':{");

		Library library = new Library(published_date, category_id, title, url);
		response.append("'Library': '" + Library.addLibrary(library) + "', ");

		library.setContent_id(Library.getLastAddedRecord(title));

		if (library.getCategory_id().longValue() == 4) {
			Webinar webinar = new Webinar(library.getContent_id(), presenter);
			response.append("'Webinar': '" + Webinar.addWebinar(webinar) + "' , ");
		} else {
			Content content = new Content(library.getContent_id(), level, presenter, reads);
			response.append("'Content': '" + Content.addContent(content) + "' , ");
		}

		response.append("'Content_tag': { ");
		StringTokenizer stringtokenizer = new StringTokenizer(tag_id, ",");
		while (stringtokenizer.hasMoreElements()) {
			String id = stringtokenizer.nextToken();
			Content_tag content_tag =
					new Content_tag(Integer.parseInt(id), library.getContent_id());
			response.append(
					"'" + id + "':{ 'result': '" + Content_tag.addContent_tag(content_tag) + "'} ");
			if (stringtokenizer.hasMoreElements()) {
				response.append(", ");
			} else {
				response.append("}}} ");
			}
		}

		return Response.ok(response.toString().replaceAll("<[^>]+>|\\s", "")
		                           .replaceAll("'", "\"")).build();
	}

}
