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
package br.octahedron.cotopaxi.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import br.octahedron.cotopaxi.interceptor.TemplateInterceptor;
import br.octahedron.cotopaxi.view.OutputStreamBuilder;
import br.octahedron.cotopaxi.view.response.TemplateResponse;
import br.octahedron.util.Log;

/**
 * A {@link TemplateInterceptor} that compress template output using GZip
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class GZipTemplatenterceptor extends TemplateInterceptor {
	
	private static final Log logger = new Log(GZipTemplatenterceptor.class);

	private static final String ACCEPT_ENCODING = "Accept-Encoding";
	private static final String CONTENT_ENCODING = "Content-Encoding";
	private static final String GZIP_ENCODING = "gzip";

	/**
	 * A simple {@link OutputStreamBuilder} that decorates the given {@link OutputStream} with a
	 * {@link GZIPOutputStream}
	 */
	private OutputStreamBuilder gzipBuilder = new OutputStreamBuilder() {
		@Override
		public OutputStream createOutputStream(OutputStream servletOutput) throws IOException {
			return new GZIPOutputStream(servletOutput);
		}
	};

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void preRender(TemplateResponse response) {
		String encoding = this.header(ACCEPT_ENCODING);
		if (encoding != null && encoding.contains(GZIP_ENCODING)) {
			logger.debug("Browser accept gzip format, enabling gzip response.");
			response.addHeader(CONTENT_ENCODING, GZIP_ENCODING);
			response.setOutputStreamBuilder(this.gzipBuilder);
		}
	}
}
