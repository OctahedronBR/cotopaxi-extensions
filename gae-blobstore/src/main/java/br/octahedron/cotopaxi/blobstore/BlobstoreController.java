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

import br.octahedron.cotopaxi.controller.Controller;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * A Controller version that contains utilities methods to handler the GAE Blobstore facilities.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class BlobstoreController extends Controller {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	/**
	 * Gets the blobservice upload url.
	 * 
	 * @param successPath
	 *            The url to blobservice redirect after a successful upload.
	 * @return the blobservice upload url.
	 */
	protected final String uploadUrl(String successPath) {
		return this.blobstoreService.createUploadUrl(successPath);
	}

	/**
	 * Gets the {@link BlobKey} a uploaded file.
	 * 
	 * @param file
	 *            the uploaded file's name - the name used by the form to define the file
	 * @return The {@link BlobKey} fot the given file, or <code>null</code> if there's no file
	 *         uploaded with the given name
	 * 
	 * @throws IllegalStateException
	 *             If not called from a blob upload callback request.
	 */
	protected final BlobKey blobKey(String file) {
		return this.blobstoreService.getUploadedBlobs(this.request()).get(file);
	}

	/**
	 * Serves a file with the given {@link BlobKey}
	 * 
	 * @param blobKey the file's {@link BlobKey}
	 */
	protected final void serve(String blobKey) {
		this.setControllerResponse(new BlobResponse(blobstoreService, new BlobKey(blobKey)));
	}

}
