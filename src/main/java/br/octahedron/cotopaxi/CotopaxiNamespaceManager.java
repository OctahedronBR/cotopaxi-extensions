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
package br.octahedron.cotopaxi;

import java.util.ArrayList;
import java.util.List;

import br.octahedron.util.Log;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;

/**
 * This entity is responsible performs the namespace management.
 * 
 * @author Erick Moreno
 * @author Vitor Avelino
 * @author Danilo Queiroz
 */
public class CotopaxiNamespaceManager {
	
	private static final String GLOBAL_NAMESPACE = ""; 
	private static final ThreadLocal<String> previousNamespaces = new ThreadLocal<String>();
	private static Log log = new Log(NamespaceManager.class);


	/**
	 * Changes current namespace to the given one, storing the actual namespace to be restored
	 * later.
	 */
	public static void changeToNamespace(String namespace) {
		log.debug("Changing namespace to namespace: %s", namespace);
		previousNamespaces.set((NamespaceManager.get() != null) ? NamespaceManager.get() : "");
		NamespaceManager.set(namespace);
	}

	/**
	 * Changes current namespace to global namespace, storing the actual namespace to be restored
	 * later.
	 */
	public static void changeToGlobalNamespace() {
		changeToNamespace(GLOBAL_NAMESPACE);

	}

	/**
	 * Changes the current namespace to the previous one namespace
	 */
	public static void changeToPreviousNamespace() {
		String previous = previousNamespaces.get();
		if (previous!= null) {
			log.debug("Changing namespace from global to original namespace: %s", previous);
			NamespaceManager.set(previous);
		} else {
			log.debug("No previous namespace stored, keeping the actual one.");
		}
	}
	
	/**
	 * Retrieves all namespaces created to domains on application
	 */
	public static List<String> getNamespaces() {
		Query q = new Query(Query.NAMESPACE_METADATA_KIND);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<String> results = new ArrayList<String>();
	    for (com.google.appengine.api.datastore.Entity e : ds.prepare(q).asIterable()) {
	        // A zero numeric id is used for the non-default namespaces
	        if (e.getKey().getId() == 0) { 
	        	results.add(e.getKey().getName());
	        }
	    }
	    log.debug("Quering all namespaces. Found %d namespaces", results.size());
		return results;
	}
}
