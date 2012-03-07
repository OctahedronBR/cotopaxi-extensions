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
package br.octahedron.cotopaxi.blobstore;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.controller.ControllerResponse;
import br.octahedron.cotopaxi.view.response.ServletGenericResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;

/**
 * A {@link ControllerResponse} responsible by serve files from blobstore.
 * 
 * This {@link ControllerResponse} isn't a {@link InterceptableResponse}.
 *  
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class BlobResponse extends ServletGenericResponse {
	
	private BlobstoreService blobstoreService;
	private BlobKey blobKey;
	

	/**
	 * @param blobstoreService2
	 * @param blobKey
	 */
	public BlobResponse(BlobstoreService blobstoreService, BlobKey blobKey) {
		this.blobstoreService = blobstoreService;
		this.blobKey = blobKey;
	}


	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.view.response.ServletGenericResponse#dispatch(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void dispatch(HttpServletResponse servletResponse) throws IOException {
		this.blobstoreService.serve(this.blobKey, servletResponse);
	}

}
