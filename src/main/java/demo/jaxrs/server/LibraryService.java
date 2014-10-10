package demo.jaxrs.server;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prindu on 08/10/14.
 */
@Path("/libraryService/")
public class LibraryService {
	private static String SERVICE_URL =
			"https://appserver.dev.cloud.wso2.com/services/t/naasheerwso2/attdataservice-default-SNAPSHOT/";

	@POST
	@Path("/Library/")
	public Response addLibrary(@FormParam("category_id") Integer category_id,
	                           @FormParam("title") String title,
	                           @FormParam("published_date") String published_date,
	                           @FormParam("url") String url) {
		System.out.println("----invoking addLibrary, Library Title is: " + title);
		/*PostMethod postMethod = new PostMethod(SERVICE_URL + "insertLibraryResource");*/

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("category_id", category_id.toString()));
		urlParameters.add(new BasicNameValuePair("title", title));
		urlParameters.add(new BasicNameValuePair("published_date", published_date));
		urlParameters.add(new BasicNameValuePair("url", url));

		/*HttpMethodParams params = new HttpMethodParams();
		params.setParameter("category_id", category_id);
		params.setParameter("title", title);
		params.setParameter("published_date", published_date);
		params.setParameter("url", url);

		postMethod.setParams(params);*/

		HttpResponse result = null;
		String resultStr = new String();

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_URL + "insertLibraryResource");
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			result = httpClient.execute(post);
			System.out.println("Response status code: " + result);
			resultStr = getStringFromInputStream(result.getEntity().getContent());
			System.out.println("Response body: " + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

		return Response.ok(resultStr).build();
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
}
