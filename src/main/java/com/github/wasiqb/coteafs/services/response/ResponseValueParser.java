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
package com.github.wasiqb.coteafs.services.response;

/**
 * @author wasiq.bhamla
 * @since Aug 24, 2017 8:38:20 PM
 */
public interface ResponseValueParser {
	/**
	 * @author wasiq.bhamla
	 * @since Aug 24, 2017 8:38:28 PM
	 * @param name
	 * @param path
	 * @return value from response
	 */
	<T> T valueOf (String name, String path);
}