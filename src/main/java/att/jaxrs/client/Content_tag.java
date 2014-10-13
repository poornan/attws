package att.jaxrs.client;

import att.jaxrs.util.Constants;
import att.jaxrs.util.Util;
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
import java.util.Set;

/**
 * Created by ananthaneshan on 10/11/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Content_tag {
	private long tag_id;
	private long content_id;

	public Content_tag() {
	}

	public Content_tag(int tag_id, long content_id) {
		this.tag_id = tag_id;
		this.content_id = content_id;
	}

	public static String addContent_tag(Content_tag content_tag) {

		return addContent_tag(content_tag.getContent_id(), content_tag.getTag_id());
	}

	public static String addContent_tag(long content_id, long tag_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", Long.toString(content_id)));
		urlParameters.add(new BasicNameValuePair("tag_id", Long.toString(tag_id)));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.INSERT_CONTENT_TAG_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public static String addContentTags(Set<Long> tagIDs, long content_id) {
		StringBuilder result = new StringBuilder("{ 'tags':{'");
		for (long tagID : tagIDs) {
			result.append(tagID).append(",").append(addContent_tag(content_id, tagID));
		}
		result.append("'}}");
		return result.toString();
	}

	public static String deleteContentTags(Set<Long> tagIDs, long content_id) {

		StringBuilder result = new StringBuilder("{ 'tags':{'");
		for (long tagID : tagIDs) {
			result.append(tagID).append(",").append(deleteContent_tag(content_id, tagID));
		}
		result.append("'}}");
		return result.toString();
	}

	public static String deleteContent_tag(long content_id, long tag_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", Long.toString(content_id)));
		urlParameters.add(new BasicNameValuePair("tag_id", Long.toString(tag_id)));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DELETE_CONTENT_TAG_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public long getTag_id() {
		return tag_id;
	}

	public void setTag_id(long tag_id) {
		this.tag_id = tag_id;
	}

	public long getContent_id() {
		return content_id;
	}

	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}

	@Override public String toString() {
		return "Content Tag with content id: " + content_id + "tag id: " + tag_id;
	}
}
