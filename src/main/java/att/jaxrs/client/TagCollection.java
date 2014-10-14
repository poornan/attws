package att.jaxrs.client;

import att.jaxrs.util.Marshal;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ananthaneshan on 10/7/14.
 */

@XmlRootElement
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

	public static void main(String[] args) {
		try {
			TagCollection tagCollection = Marshal.unmarshal(TagCollection.class,
			                                                "<tagCollection ><tag><tag_id >1</tag_id><tag_name >Esb</tag_name></tag><tag><tag_id >2</tag_id><tag_name >Cloud App</tag_name></tag></tagCollection>");
			System.out.println(tagCollection.getTag().length);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
