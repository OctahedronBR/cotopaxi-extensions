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
package br.octahedron.cotopaxi.datastore;

import static br.octahedron.cotopaxi.inject.DependencyManager.registerImplementation;
import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade;
import br.octahedron.cotopaxi.datastore.jdo.GenericDAO;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.inject.DependencyManager;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.test.CotopaxiTestHelper;

/**
 * This class provides utility methods to create and register mocks for
 * Datastore classes.
 * 
 * These mocks are intent to be used by tests, providing a easy way to create
 * mocks to be used while writing tests usign {@link CotopaxiTestHelper}
 * utilities.
 * 
 * Besides create the mocks it self, that isn't a hard task, it also register
 * the mocks as the reference implementation at the {@link DependencyManager}.
 * It makes mocks by transparently inject on dependent class using
 * {@link Inject} annotation.
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
	 * @param daoKlass The {@link GenericDAO} subclass to be mocked.
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
