package br.octahedron.cotopaxi.datastore.test;

import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade;
import br.octahedron.cotopaxi.datastore.jdo.GenericDAO;

/**
 * This entity is responsible by load the datastore to be used by tests, when
 * not using mocks for {@link DatastoreFacade} or {@link GenericDAO}.
 * 
 * See
 * {@link DatastoreTestHelper#setUpFixturesDatastore(DatastoreLoader, String...)}
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public interface DatastoreLoader {

	/**
	 * Loads the test database to be used to load fixtures and run tests.
	 * 
	 * This method should be responsible by the entire datastore setup,
	 * including drop and create tables, adjust password, configure application
	 * to use the proper datastore, etc.
	 */
	public void load();
	
	// TODO should it have a destroy?

	/**
	 * Gets the facade for the loaded datastore. This method should be invoked
	 * after the {@link DatastoreLoader#load()}.
	 * 
	 * @return The {@link DatastoreFacade} to the test datastore.
	 * @throws IllegalStateException
	 *             If the test datastore was not loaded yet.
	 */
	public DatastoreFacade datastoreFacade() throws IllegalStateException;
}
