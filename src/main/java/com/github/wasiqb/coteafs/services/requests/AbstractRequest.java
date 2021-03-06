/**
 * Copyright 2017, Wasiq Bhamla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wasiqb.coteafs.services.requests;

import static com.github.wasiqb.coteafs.services.config.ConfigConstants.SERVICE_CONFIG_DEFAULT_FILE_NAME;
import static com.github.wasiqb.coteafs.services.config.ConfigConstants.SERVICE_CONFIG_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.wasiqb.coteafs.config.loader.ConfigLoader;
import com.github.wasiqb.coteafs.services.config.RequestMethod;
import com.github.wasiqb.coteafs.services.config.ServiceSetting;
import com.github.wasiqb.coteafs.services.config.ServicesSetting;
import com.github.wasiqb.coteafs.services.helper.RequestHandler;
import com.github.wasiqb.coteafs.services.helper.ResponseHandler;

/**
 * @author wasiq.bhamla
 * @since Aug 20, 2017 3:00:51 PM
 */
public abstract class AbstractRequest {
	private static ServicesSetting settings;

	static {
		settings = ConfigLoader.settings ()
			.withKey (SERVICE_CONFIG_KEY)
			.withDefault (SERVICE_CONFIG_DEFAULT_FILE_NAME)
			.load (ServicesSetting.class);
	}

	private final List <String>			files;
	private final Map <String, Object>	formParams;
	private final Map <String, Object>	headers;
	private final Map <String, Object>	params;
	private final Map <String, Object>	pathParams;
	private final Map <String, Object>	queryParams;
	private final String				resourcePath;
	private ResponseHandler				response;
	private final ServiceSetting		setting;
	private final Map <String, Object>	values;

	/**
	 * @author wasiq.bhamla
	 * @param serviceName
	 * @since Feb 12, 2018 4:19:46 PM
	 */
	public AbstractRequest (final String serviceName) {
		this (serviceName, null);
	}

	/**
	 * @author wasiq.bhamla
	 * @param serviceName
	 * @param resourcePath
	 * @since Aug 20, 2017 3:00:51 PM
	 */
	public AbstractRequest (final String serviceName, final String resourcePath) {
		this.setting = settings.getService (serviceName);
		this.headers = new HashMap <> ();
		this.params = new HashMap <> ();
		this.pathParams = new HashMap <> ();
		this.formParams = new HashMap <> ();
		this.queryParams = new HashMap <> ();
		this.values = new HashMap <> ();
		this.files = new ArrayList <> ();
		this.resourcePath = resourcePath == null ? "" : resourcePath;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:13:32 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest delete (final boolean shouldWork) {
		this.response = execute (RequestMethod.DELETE, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:13:24 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest get (final boolean shouldWork) {
		this.response = execute (RequestMethod.GET, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:13:18 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest head (final boolean shouldWork) {
		this.response = execute (RequestMethod.HEAD, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:13:11 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest options (final boolean shouldWork) {
		this.response = execute (RequestMethod.OPTIONS, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:13:01 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest patch (final boolean shouldWork) {
		this.response = execute (RequestMethod.PATCH, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:12:54 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest post (final boolean shouldWork) {
		this.response = execute (RequestMethod.POST, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @return request
	 * @since Sep 21, 2017 10:38:06 AM
	 */
	public abstract RequestElement prepare ();

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:12:48 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest put (final boolean shouldWork) {
		this.response = execute (RequestMethod.PUT, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Mar 19, 2018 10:12:30 PM
	 * @param shouldWork
	 * @return response
	 */
	public AbstractRequest trace (final boolean shouldWork) {
		this.response = execute (RequestMethod.TRACE, shouldWork);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Apr 1, 2018 9:59:56 PM
	 * @param expression
	 * @return response verify
	 */
	public ResponseVerify verifyThat (final String expression) {
		return new ResponseVerify (this.response, expression);
	}

	/**
	 * @author wasiq.bhamla
	 * @since May 12, 2018 7:07:54 PM
	 * @param filePath
	 * @return instance
	 */
	public AbstractRequest withFile (final String filePath) {
		this.files.add (filePath);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 9, 2017 10:18:17 PM
	 * @param name
	 * @param value
	 * @return instance
	 */
	public AbstractRequest withFormParameter (final String name, final Object value) {
		this.formParams.put (name, value);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @param name
	 * @param value
	 * @since Aug 25, 2017 10:15:07 PM
	 * @return instance
	 */
	public AbstractRequest withHeader (final String name, final Object value) {
		this.headers.put (name, value);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @param name
	 * @param value
	 * @since Aug 25, 2017 10:15:12 PM
	 * @return instance
	 */
	public AbstractRequest withParameter (final String name, final Object value) {
		this.params.put (name, value);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 9, 2017 10:18:30 PM
	 * @param name
	 * @param value
	 * @return instance
	 */
	public AbstractRequest withPathParameter (final String name, final Object value) {
		this.pathParams.put (name, value);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 9, 2017 10:18:37 PM
	 * @param name
	 * @param value
	 * @return instance
	 */
	public AbstractRequest withQueryParameter (final String name, final Object value) {
		this.queryParams.put (name, value);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 21, 2017 11:27:56 AM
	 * @param fieldName
	 * @param value
	 * @return instance
	 */
	public AbstractRequest withValue (final String fieldName, final Object value) {
		this.values.put (fieldName, value);
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 21, 2017 11:30:07 AM
	 * @param fieldName
	 * @return value
	 */
	@SuppressWarnings ("unchecked")
	protected <V> V get (final String fieldName) {
		return (V) this.values.get (fieldName);
	}

	private ResponseHandler execute (final RequestMethod method, final boolean shouldWork) {
		RequestHandler handler = RequestHandler.build ()
			.setting (this.setting)
			.using ()
			.resource (this.resourcePath);
		if (method != RequestMethod.GET) {
			handler = handler.with (prepare ());
		}
		setHeaders (handler);
		setParams (handler);
		setFormParams (handler);
		setQueryParams (handler);
		setPathParams (handler);
		setFiles (handler);
		return handler.execute (method.getMethod (), shouldWork)
			.response ();
	}

	/**
	 * @author wasiq.bhamla
	 * @since May 12, 2018 7:08:20 PM
	 * @param handler
	 */
	private void setFiles (final RequestHandler handler) {
		handler.multiPart (this.files);
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 9, 2017 10:39:36 PM
	 * @param handler
	 */
	private void setFormParams (final RequestHandler handler) {
		handler.formParams (this.params);
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 4, 2017 10:11:24 PM
	 * @param handler
	 */
	private void setHeaders (final RequestHandler handler) {
		handler.headers (this.headers);
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 4, 2017 10:12:05 PM
	 * @param handler
	 */
	private void setParams (final RequestHandler handler) {
		handler.params (this.params);
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 9, 2017 10:39:44 PM
	 * @param handler
	 */
	private void setPathParams (final RequestHandler handler) {
		handler.pathParams (this.pathParams);
	}

	/**
	 * @author wasiq.bhamla
	 * @since Sep 9, 2017 10:39:41 PM
	 * @param handler
	 */
	private void setQueryParams (final RequestHandler handler) {
		handler.queryParams (this.queryParams);
	}
}