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

package att.jaxrs.util;

/**
 * Created by ananthaneshan on 10/10/14.
 */
public final class Constants {
	private Constants() {
	}

	public static final String DATA_SERVICE_URI =
			"https://appserver.dev.cloud.wso2.com/services/t/naasheerwso2/attdataservice-default-SNAPSHOT/";

	public static final String DATA_SERVICE_XMLNS = " xmlns=\"http://ws.wso2.org/dataservice\"";

	public static final String RESPONSE_STATUS_CODE = "Response status code: ";

	public static final String RESPONSE_BODY = "Response body: ";

	public static final String SELECT_ALL_TAG_OPERATION =
			DATA_SERVICE_URI + "select_all_tag_operation";

	public static final String SELECT_WITH_KEY_TAG_OPERATION =
			DATA_SERVICE_URI + "select_with_key_tag_operation?tag_id=";

	public static final String INSERT_LIBRARY_RESOURCE = DATA_SERVICE_URI + "insertLibraryResource";

	public static final String SELECT_LAST_ADDED_LIBRARY_RESOURCE =
			DATA_SERVICE_URI + "selectLastAddedLibraryResource";
	public static final String SELECT_LAST_ADDED_TAG_RESOURCE =
			DATA_SERVICE_URI + "selectLastAddedTagResource";

	public static final String INSERT_WEBINAR_RESOURCE = DATA_SERVICE_URI + "insertWebinarResource";

	public static final String INSERT_TAG_RESOURCE = DATA_SERVICE_URI + "insertTagResource";

	public static final String UPDATE_LIBRARY_RESOURCE = DATA_SERVICE_URI + "updateLibraryResource";

	public static final String SELECT_WITH_KEY_LIBRARY_RESOURCE =
			DATA_SERVICE_URI + "selectWithKeyLibraryResource";

	public static final String SELECT_WITH_KEY_CONTENT_TAG_RESOURCE =
			DATA_SERVICE_URI + "selectWithKeyContentTagResource";

	public static final String INSERT_CONTENT_RESOURCE = DATA_SERVICE_URI + "insertContentResource";

	public static final String UPDATE_CONTENT_RESOURCE = DATA_SERVICE_URI + "updateContentResource";

	public static final String INSERT_CONTENT_TAG_RESOURCE =
			DATA_SERVICE_URI + "insertContentTagResource";

	public static final String DELETE_CONTENT_TAG_RESOURCE =
			DATA_SERVICE_URI + "deleteContentTagResource";

	public static final String UPDATE_WEBINAR_RESOURCE = DATA_SERVICE_URI + "updateWebinarResource";

	public static final String DELETE_WEBINAR_RESOURCE = DATA_SERVICE_URI + "deleteWebinarResource";

	public static final String DELETE_LIBRARY_RESOURCE = DATA_SERVICE_URI + "deleteLibraryResource";

	public static final String DELETE_CONTENT_RESOURCE = DATA_SERVICE_URI + "deleteContentResource";

	public static final String SELECT_WITH_NAME_TAG_RESOURCE =
			DATA_SERVICE_URI + "selectWithNameTagResource?tag_name=";

	public static final String SELECT_ALL_LIBRARY_RESOURCE =
			DATA_SERVICE_URI + "selectAllLibraryResource";

	public static final String SELECT_ALL_LIBRARY_OPERATION =
			DATA_SERVICE_URI + "select_all_library_operation";

	public static final String SELECT_ALL_CONTENT_RESOURCE =
			DATA_SERVICE_URI + "selectAllContentResource";

	public static final String SELECT_ALL_CONTENT_OPERATION =
			DATA_SERVICE_URI + "select_all_content_operation";

	public static final String SELECT_ALL_WEBINAR_RESOURCE =
			DATA_SERVICE_URI + "selectAllWebinarResource";

	public static final String SELECT_ALL_WEBINAR_OPERATION =
			DATA_SERVICE_URI + "select_all_webinar_operation";

	public static final String SELECT_ALL_CONTENT_TAG_RESOURCE =
			DATA_SERVICE_URI + "selectAllContentTagResource";

	public static final String SELECT_ALL_CONTENT_TAG_OPERATION =
			DATA_SERVICE_URI + "select_all_content_tag_operation";

	public static final String SELECT_ALL_CATEGORY_RESOURCE =
			DATA_SERVICE_URI + "selectAllCategoryResource";

	public static final String SELECT_ALL_CATEGORY_OPERATION =
			DATA_SERVICE_URI + "select_all_category_operation";

	public static final String ROLE_USER = "ROLE_USER";

	public static final String ROLE_ADMIN = "ROLE_ADMIN";

}
