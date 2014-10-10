package demo.jaxrs.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ananthaneshan on 10/7/14.
 */

@XmlRootElement(namespace = "http://ws.wso2.org/dataservice")
@XmlAccessorType(XmlAccessType.FIELD)
public class TagCollection {
	@XmlElement
	private Tag[] tag;

	public Tag[] getTag() {
		return tag;
	}

	public void setTag(Tag[] tags) {
		this.tag = tags;
	}
}