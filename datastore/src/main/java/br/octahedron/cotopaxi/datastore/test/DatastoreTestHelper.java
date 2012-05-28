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
package br.octahedron.cotopaxi.datastore.test;

import static br.octahedron.cotopaxi.datastore.test.Fixtures.load;
import static br.octahedron.cotopaxi.inject.DependencyManager.registerImplementation;

import java.io.IOException;

import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade;
import br.octahedron.cotopaxi.datastore.jdo.GenericDAO;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.inject.DependencyManager;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.test.CotopaxiTestHelper;

/**
 * This class provides utility methods to create tests using the datastore. You
 * can setup two different kind of tests using the data base, using fixtures or
 * using the mocks for DAOs objects or for the {@link DatastoreFacade}.
 * 
 * <strong>Using fixtures</strong>
 * 
 * You can use fixtures to populate your test database with predefined data, to
 * be used by your tests.
 * 
 * To use fixtures you'll need:
 * 
 * - The fixtures objects description: Objects should be described in YAML
 * format, as describe at {@link Fixtures}.
 * 
 * - A {@link DatastoreLoader} - This entity is responsible by setup the test
 * datastore and configure your application to use it. In most cases, this will
 * be provided with the datastore specific implementation extension.
 * 
 * To use fixtures, call the method
 * {@link DatastoreTestHelper#setUpFixturesDatastore(DatastoreLoader, String...)}
 * on your test setup method.
 * 
 * <strong>Using mocks</strong>
 * 
 * You can use mock to simulate the behavior of the DAO objects or of the
 * {@link DatastoreFacade}. To see more details of how setup the expected
 * behavior of your mock objects, <a href="http://easymock.org/EasyMock3_1_Documentation.html">
 *  Easymock documentation</a>.
 * 
 * Instead of create the mock by yourself, you should relay on the methods
 * provided by this helper to do this. Besides create the mocks it self, that
 * isn't a hard task, it also register the mocks as the reference implementation
 * at the {@link DependencyManager}. It makes mocks by transparently inject on
 * dependent class using {@link Inject} annotation.
 * 
 * For example, assuming you are writing a test case for a controller and had
 * extend {@link CotopaxiTestHelper}, to create and manage a Datastore mock
 * properly, instead of call:
 * 
 * <pre>
 * Datastore dsMock = this.createMock(DatastoreFacade.class);
 * </pre>
 * 
 * You should call:
 * 
 * <pre>
 * Datastore dsMock = DatastoreMockHelper.mockDatastoreFacade(this);
 * </pre>
 * 
 * That way the mock lifecycle will be managed by the {@link CotopaxiTestHelper}
 * , as well as the Mock will be used by the Dependency Injection mechanism.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class DatastoreTestHelper {

	/**
	 * Setup the fixtures to be used by tests. It setups the test database and
	 * then load the fixtures to be used.
	 * 
	 * @param dsLoader
	 *            The {@link DatastoreLoader} for the test datastore.
	 * @param fixtures
	 *            The fixtures YAML files. This should be the path for the
	 *            files, relative to your resources dir. Eg.: If you fixture
	 *            file is placed under the
	 *            test/resoures/fixtures/myFixture.yaml, you should pass
	 *            "fixtures/myFixture.yaml".
	 * 
	 * @throws IOException
	 *             if any error occurs during the setup.
	 */
	public static void setUpFixturesDatastore(DatastoreLoader dsLoader, String... fixtures) throws IOException {
		dsLoader.load();
		DatastoreFacade ds = dsLoader.datastoreFacade();
		load(ds, fixtures);
	}

	/**
	 * Creates and register a mock for {@link DatastoreFacade}
	 * 
	 * @param testHelper
	 *            The {@link CotopaxiTestHelper} that will manage the created
	 *            mock
	 * @return The {@link DatastoreFacade} mock object.
	 */
	public static DatastoreFacade mockDatastoreFacade(CotopaxiTestHelper testHelper) {
		DatastoreFacade ds = testHelper.createMock(DatastoreFacade.class);
		registerImplementation(DatastoreFacade.class, ds);
		return ds;
	}

	/**
	 * Creates and register a mock for a given {@link GenericDAO}
	 * 
	 * @param testHelper
	 *            The {@link CotopaxiTestHelper} that will manage the created
	 *            mock
	 * @param daoKlass
	 *            The {@link GenericDAO} subclass to be mocked.
	 * 
	 * @return The {@link GenericDAO} mock object.
	 */
	public static <T> GenericDAO<T> mockDAO(CotopaxiTestHelper testHelper, Class<? extends GenericDAO<T>> daoKlass) {
		GenericDAO<T> dao = testHelper.createMock(daoKlass);
		registerImplementation(daoKlass, dao);
		return dao;
	}

	/**
	 * Creates and register a mock for {@link NamespaceManager}
	 * 
	 * @param testHelper
	 *            The {@link CotopaxiTestHelper} that will manage the created
	 *            mock
	 * 
	 * @return The {@link NamespaceManager} mock object.
	 */
	public static NamespaceManager mockNamespaceManager(CotopaxiTestHelper testHelper) {
		NamespaceManager ns = testHelper.createMock(NamespaceManager.class);
		registerImplementation(NamespaceManager.class, ns);
		return ns;
	}
}
