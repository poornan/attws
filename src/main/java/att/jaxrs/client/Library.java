package att.jaxrs.client;

import att.jaxrs.util.Constants;
import att.jaxrs.util.Marshal;
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

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Library {

	private long content_id;

	private int category_id;

	private String title, url, published_date;

	public Library(String published_date, int category_id, String title, String url) {
		this.published_date = published_date;
		this.category_id = category_id;
		this.title = title;
		this.url = url;
	}

	public Library(long content_id, String published_date, int category_id, String title,
	               String url) {
		this.published_date = published_date;
		this.category_id = category_id;
		this.content_id = content_id;
		this.title = title;
		this.url = url;
	}

	public Library() {

	}

	public static String addLibrary(Library library) {
		final long content_id = getExistingRecord(library.getTitle(), library.getCategory_id());
		if (-1 != content_id) {
			return "{response:{Library: 'EXISTING_RECORD'},content_id:" + content_id + "}";
		}
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("category_id",
				                            Integer.toString(library.getCategory_id())));
		urlParameters.add(new BasicNameValuePair("title", library.getTitle()));
		urlParameters.add(new BasicNameValuePair("published_date", library.getPublished_date()));
		urlParameters.add(new BasicNameValuePair("url", library.getUrl()));

		HttpResponse result;
		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.INSERT_LIBRARY_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		return resultStr;
	}

	public static long getExistingRecord(String title, int category_id) {

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("title", title));
		urlParameters
				.add(new BasicNameValuePair("category_id", Long.valueOf(category_id).toString()));

		HttpResponse result;
		String resultStr;
		LibraryCollection libraryCollection = new LibraryCollection();

		try {
			System.out.println("invoking getExistingRecord, Title: " + title + ", Category ID: " +
			                   category_id);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SELECT_LAST_ADDED_LIBRARY_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
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

	public static String updateLibrary(Library library) {

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("category_id",
				                            Integer.toString(library.getCategory_id())));
		urlParameters.add(new BasicNameValuePair("title", library.getTitle()));
		urlParameters.add(new BasicNameValuePair("published_date", library.getPublished_date()));
		urlParameters.add(new BasicNameValuePair("url", library.getUrl()));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.UPDATE_LIBRARY_RESOURCE);
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

	public static Library selectWithKeyLibraryResource(long content_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", Long.toString(content_id)));

		LibraryCollection libraryCollection = new LibraryCollection();
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.UPDATE_LIBRARY_RESOURCE);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse result = httpClient.execute(post);
			System.out.println(Constants.RESPONSE_STATUS_CODE + result);
			String resultStr = Util.getStringFromInputStream(result.getEntity().getContent());
			System.out.println(Constants.RESPONSE_BODY + resultStr);

			libraryCollection = Marshal.unmarshal(LibraryCollection.class, resultStr);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		if (libraryCollection.getLibrary().length == 1) {
			return libraryCollection.getLibrary()[0];
		}
		return null;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(long content_id) {
		this.content_id = content_id;
	}

	public int getCategory_id() {
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

	public static String deleteLibrary(long content_id) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair("content_id", Long.toString(content_id)));

		String resultStr = "";

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.DELETE_LIBRARY_RESOURCE);
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
}



