/*
 * Copyright 2011-2012 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo.jaxrs.server;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/tagservice/")
public class TagService {
    long currentId = 001;
    Map<Long, Tag> tags = new HashMap<Long, Tag>();

    public TagService() {
        init();
    }

    @GET
    @Path("/tags/{id}/")
    public Tag getCustomer(@PathParam("id") String id) {
        System.out.println("----invoking getTag, tag id is: " + id);
        long idNumber = Long.parseLong(id);
        return tags.get(idNumber);
    }

    final void init() {
        Tag c = new Tag();
        c.setTag_name("book");
        c.setTag_id(currentId);
        tags.put(c.getTag_id(), c);
    }

}
