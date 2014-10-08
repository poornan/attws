package demo.jaxrs.client;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
public class Webinar {
	private long content_id;
	private String presenter;

	public long getContent_id() {
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
