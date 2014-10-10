package demo.jaxrs.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by prindu on 08/10/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WebinarCollection {
	@XmlElement
	private Webinar[] webinar;

	public Webinar[] getWebinar() {
		return webinar;
	}

	public void setWebinar(Webinar[] webinar) {
		this.webinar = webinar;
	}
}



