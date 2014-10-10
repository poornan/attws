package demo.jaxrs.client;

import demo.jaxrs.util.Constants;
import demo.jaxrs.util.Util;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Webinar {
	private long content_id;
	private String presenter;

	public Webinar() {
	}

	public Webinar(long content_id, String presenter) {
		this.content_id = content_id;
		this.presenter = presenter;
	}

	public static String addWebinar(Webinar webinar) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", webinar.getContent_id().toString()));
		urlParameters.add(new BasicNameValuePair("presenter", webinar.getPresenter()));

		HttpResponse result;
		String resultStr = new String();

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DATA_SERVICE_URI + "insertWebinarResource");
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

	public String getPresenter() {
		return presenter;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}
}
