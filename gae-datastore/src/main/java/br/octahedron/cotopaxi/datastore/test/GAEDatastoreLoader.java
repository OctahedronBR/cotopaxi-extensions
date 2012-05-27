package br.octahedron.cotopaxi.datastore.test;

import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade;
import br.octahedron.cotopaxi.inject.Injector;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 * 
 */
public class GAEDatastoreLoader implements DatastoreLoader {

	private DatastoreFacade ds;

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.datastore.test.DatastoreLoader#load()
	 */
	@Override
	public void load() {
		LocalDatastoreServiceTestConfig dsConfig = new LocalDatastoreServiceTestConfig();
		dsConfig.setNoStorage(true);
		LocalServiceTestHelper helper = new LocalServiceTestHelper(dsConfig);
		helper.setUp();
		try {
			this.ds = Injector.getInstance(DatastoreFacade.class);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.datastore.test.DatastoreLoader#datastoreFacade()
	 */
	@Override
	public DatastoreFacade datastoreFacade() throws IllegalStateException {
		if (this.ds != null) {
			return this.ds;
		} else {
			throw new IllegalStateException();
		}
	}

}
