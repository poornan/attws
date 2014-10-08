package demo.jaxrs.server;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by prindu on 08/10/14.
 */
@XmlType
public class Content {

	private long content_id;
	private String level, presenter, reads;

	public long getContent_id() {
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
