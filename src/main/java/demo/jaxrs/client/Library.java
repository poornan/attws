package demo.jaxrs.client;

import demo.jaxrs.util.Constants;
import demo.jaxrs.util.Marshal;
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
public class Library {

	private int content_id;

	private int category_id;

	private String title;

	private String url;

	private String published_date;

	public Library(String published_date, int category_id, String title, String url) {
		this.published_date = published_date;
		this.category_id = category_id;
		this.title = title;
		this.url = url;
	}

	public Library() {

	}

	public static String addLibrary(Library library) {
		final int content_id = getLastAddedRecord(library.getTitle());
		if (-1 != content_id) {
			return "{response:{Library: 'EXISTING_RECORD'},content_id:" + content_id + "}";
		}
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("category_id", library.getCategory_id().toString()));
		urlParameters.add(new BasicNameValuePair("title", library.getTitle()));
		urlParameters.add(new BasicNameValuePair("published_date", library.getPublished_date()));
		urlParameters.add(new BasicNameValuePair("url", library.getUrl()));

		HttpResponse result;
		String resultStr = new String();

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DATA_SERVICE_URI + "insertLibraryResource");
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

	public static int getLastAddedRecord(String title) {

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("title", title));

		HttpResponse result;
		String resultStr = new String();
		LibraryCollection libraryCollection = new LibraryCollection();

		try {
			System.out.println("invoking last added: " + title);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DATA_SERVICE_URI + "selectLastAddedResource");
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			result = httpClient.execute(post);
			System.out.println("Response status code: " + result);
			resultStr = Util.getStringFromInputStream(result);

			libraryCollection =
					Marshal.unmarshal(LibraryCollection.class, resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();

		}

		if (null != libraryCollection.getLibrary()) {
			return libraryCollection.getLibrary()[0].getContent_id();
		}
		return -1;
	}

	public static void main(String[] args) {
		System.out.println(getLastAddedRecord("memememe"));
	}

	public Integer getContent_id() {
		return content_id;
	}

	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	private void setUrl(String url) {
		this.url = url;
	}

	public String getPublished_date() {
		return published_date;
	}

	public void setPublished_date(String published_date) {
		this.published_date = published_date;
	}

	@Override public String toString() {
		return "Content: id=" + getContent_id() + ", Title=" + getTitle();
	}
}



