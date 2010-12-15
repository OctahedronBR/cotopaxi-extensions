/*
 *  This file is part of Cotopaxi.
 *
 *  Cotopaxi is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Cotopaxi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the Lesser GNU General Public License
 *  along with Cotopaxi. If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.cloudservice.gae;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import br.octahedron.cotopaxi.cloudservice.URLFetchFacade;
import br.octahedron.cotopaxi.cloudservice.common.HTTPMethod;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public class URLFetchFacadeImpl implements URLFetchFacade {

	private static final String NAMESPACE = "URLFETCH";
	private DocumentBuilder documentBuilder;
	private MemcacheService cache;

	public URLFetchFacadeImpl() {
		try {
			this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
		this.cache = MemcacheServiceFactory.getMemcacheService(NAMESPACE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.URLFetchFacade#fetchAsDocument(java.lang.String)
	 */
	public Document fetchAsDocument(String url) throws SAXException, IOException {
		return this.fetchAsDocument(url, HTTPMethod.GET, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.URLFetchFacade#fetchAsDocument(java.lang.String,
	 * br.octahedron.cs.common.HTTPMethod, java.util.Map)
	 */
	public Document fetchAsDocument(String url, HTTPMethod method, Map<String, String> params) throws SAXException, IOException {
		return this.documentBuilder.parse(this.fetchAsInputStream(url, method, params));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.URLFetchFacade#fetchAsInputStream(java.lang.String)
	 */
	public InputStream fetchAsInputStream(String url) throws IOException {
		return this.fetchAsInputStream(url, HTTPMethod.GET, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.URLFetchFacade#fetchAsInputStream(java.lang.String,
	 * br.octahedron.cs.common.HTTPMethod, java.util.Map)
	 */
	public InputStream fetchAsInputStream(String url, HTTPMethod method, Map<String, String> params) throws IOException {
		HttpURLConnection connection = this.getConnection(url, method);
		if (params != null) {
			this.addParamsToRequest(connection, params);
		}
		InputStream response = connection.getInputStream();
		this.cache.put(url, connection.getLastModified());

		return response;
	}

	/**
	 * Adds parameters to http request.
	 * 
	 * @param connection
	 *            The connection opened
	 * @param params
	 *            The parameters stored in a Map (parameter => value)
	 * @throws IOException
	 */
	private void addParamsToRequest(HttpURLConnection connection, Map<String, String> params) throws IOException {
		connection.setDoOutput(true);
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(connection.getOutputStream());
			String data = "";
			for (String key : params.keySet()) {
				data += "&" + key + "=" + params.get(key);
			}
			writer.write(data);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.URLFetchFacade#hasChanged(java.lang.String)
	 */
	public boolean hasChanged(String url) throws IOException {
		HttpURLConnection connection = this.getConnection(url, HTTPMethod.HEAD);
		connection.connect();

		Long cachedTime = (Long) this.cache.get(url);
		long newTime = connection.getLastModified();
		if (cachedTime == null || cachedTime < newTime) {
			this.cache.put(url, newTime);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Open a http connection with remote object refered by an URL to be used during a request.
	 * 
	 * @param url
	 *            The url you want to open connection.
	 * @param method
	 *            The method type you want to use.
	 * @return
	 * @throws IOException
	 */
	private HttpURLConnection getConnection(String url, HTTPMethod method) throws IOException {
		URL urlRequested = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlRequested.openConnection();
		connection.setDoInput(true);
		connection.setRequestMethod(method.name());

		return connection;
	}

}