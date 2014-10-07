
package demo.jaxrs.server;

import javax.xml.bind.annotation.*;

/**
 * Created by ananthaneshan on 10/7/14.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TagCollection {
 @XmlElement
 private Tag[] tags;

	public Tag[] getTag() {
		return tags;
	}

	public void setTag(Tag[] tags) {
		this.tags = tags;
	}
}
