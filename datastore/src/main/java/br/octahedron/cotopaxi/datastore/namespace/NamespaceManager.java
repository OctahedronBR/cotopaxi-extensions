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
package br.octahedron.cotopaxi.datastore.namespace;

import java.util.List;

/**
 * A utility interface to deal with different data namespaces.
 * 
 * Namespaces are used to implements logical data separation. It's very useful for multitenancy
 * architectures.
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 */
public interface NamespaceManager {
	
	/**
	 * Gets the current namespace
	 * 
	 * @return the current namespace
	 */
	public abstract String currentNamespace();

	/**
	 * Changes the current namespace to the global namespace.
	 */
	public abstract void changeToGlobalNamespace();

	/**
	 * Changes the current namespace to the given one.
	 * 
	 * @param namespace
	 *            The namespace to be used.
	 */
	public abstract void changeToNamespace(String namespace);

	/**
	 * Changes the current namespace to the previous one. If there's no previous namespace, the
	 * namespace isn't modified.
	 */
	public abstract void changeToPreviousNamespace();

	/**
	 * Gets all existent namespace.
	 * 
	 * @return A list with all existent namespaces.
	 */
	public abstract List<String> getNamespaces();

	/**
	 * Checks if a given namespace exists.
	 * 
	 * @param namespace
	 *            The namespace to be checked.
	 * @return <code>true</code> if exists the given namespace, <code>false</code> if doesn't
	 *         exists.
	 */
	public abstract boolean exists(String namespace);
}
