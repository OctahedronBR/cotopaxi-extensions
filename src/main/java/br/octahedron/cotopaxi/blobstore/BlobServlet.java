/*
 *  Straight - A system to manage financial demands for small and decentralized
 *  organizations.
 *  Copyright (C) 2011  Octahedron 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.cotopaxi.blobstore;


/**
 * @author Vítor Avelino
 *
 */
public class BlobServlet {
//	extends HttpServlet {
//
//
//	private static final long serialVersionUID = -315053284649272497L;
//
//	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//
//	private UserService userService = UserServiceFactory.getUserService();
//
//	private static final Log log = Logger.getLogger(BlobServlet.class.getName());
//
//	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
//		String key = parseKey(req.getRequestURI());
//		BlobKey blobKey = new BlobKey(key);
//		blobstoreService.serve(blobKey, res);
//	}
//
//
//	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		logger.info("Uploading avatar to blobstore");
//
//		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
//		BlobKey blobKey = blobs.get("file");
//		try {
//			UploadTypeEnum uploadType = UploadTypeEnum.valueOf(parseType(req.getRequestURI()));
//
//			if (blobKey == null) {
//				res.sendRedirect(uploadType.getFailUrl());
//			} else {
//				publishEvent(uploadType, parseDomainName(req.getServerName()), blobKey.getKeyString());
//				res.sendRedirect(uploadType.getSuccessUrl());
//			}
//		} catch (IllegalArgumentException e) {
//			res.sendRedirect("/404");
//		}
//	}
//
//	/**
//	 * @param requestURI
//	 * @return
//	 */
//	private String parseKey(String requestURI) {
//		return requestURI.substring(requestURI.lastIndexOf('/')+1);
//	}
//
//	/**
//	 * @param requestURI
//	 * @return
//	 */
//	private String parseType(String requestURI) {
//		String partialPath = requestURI.substring(0, requestURI.lastIndexOf('/'));
//		return partialPath.substring(partialPath.lastIndexOf('/')+1).toUpperCase();
//	}
//
//	/**
//	 * 
//	 * @param serverName
//	 * @return
//	 */
//	private String parseDomainName(String serverName) {
//		return (serverName.indexOf('.') >= 0) ? serverName.substring(0, serverName.indexOf('.')) : serverName;
//	}
//
//	/**
//	 * @param uploadType
//	 * @param blobKey
//	 */
//	private void publishEvent(UploadTypeEnum uploadType, String serverName, String blobKey) {
//		switch (uploadType) {
//		case USER:
//			publish(new UserUploadEvent(userService.getCurrentUser().getEmail(), blobKey));
//			break;
//			
//		case DOMAIN:
//			publish(new DomainUploadEvent(serverName, blobKey));
//			break;
//
//		default:
//			break;
//		}
//		
//	}
}

