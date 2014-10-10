package demo.jaxrs.client;

import demo.jaxrs.util.Constants;
import demo.jaxrs.util.Util;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
public class Content {

	private long content_id;
	private String level, presenter, reads;

	public Content(long content_id, String level, String presenter, String reads) {
		this.content_id = content_id;
		this.level = level;
		this.presenter = presenter;
		this.reads = reads;
	}

	public static String addContent(Content content) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("content_id", content.getContent_id().toString()));
		urlParameters.add(new BasicNameValuePair("level", content.getLevel()));
		urlParameters.add(new BasicNameValuePair("presenter", content.getPresenter()));
		urlParameters.add(new BasicNameValuePair("reads", content.getReads()));

		HttpResponse result;
		String resultStr = new String();

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DATA_SERVICE_URI + "insertContentResource");
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			result = httpClient.execute(post);
			System.out.println("Response status code: " + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println("Response body: " + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(long content_id) {
		this.content_id = content_id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPresenter() {
		return presenter;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}
}
