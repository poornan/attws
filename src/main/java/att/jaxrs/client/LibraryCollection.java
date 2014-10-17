/*
 * Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package att.jaxrs.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by prindu on 08/10/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryCollection implements att.jaxrs.client.XmlRootElement<Library> {
	@XmlElement
	private Library[] library;

	public Library[] getLibrary() {
		return library;
	}

	public void setLibrary(Library[] library) {
		this.library = library;
	}

	public Library[] getElements() {
		return library;
	}
}



