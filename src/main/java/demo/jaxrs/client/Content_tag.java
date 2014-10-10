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
 * Created by ananthaneshan on 10/11/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Content_tag {
	private int tag_id;
	private int content_id;

	public Content_tag() {
	}

	public Content_tag(int tag_id, int content_id) {
		this.tag_id = tag_id;
		this.content_id = content_id;
	}

	public static String addContent_tag(Content_tag content_tag) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", content_tag.getContent_id().toString()));
		urlParameters.add(new BasicNameValuePair("tag_id", content_tag.getTag_id().toString()));

		HttpResponse result;
		String resultStr = new String();

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DATA_SERVICE_URI + "insertContentTagResource");
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

	public Integer getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public Integer getContent_id() {
		return content_id;
	}

	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}
}
