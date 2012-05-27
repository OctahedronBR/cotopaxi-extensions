package br.octahedron.cotopaxi.datastore.test;

import static org.easymock.EasyMock.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade;

import com.esotericsoftware.yamlbeans.YamlException;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 *
 */
public class FixturesTest {

	private DatastoreFacade ds;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.ds = createMock(DatastoreFacade.class);
	}

	@Test(expected = IOException.class)
	public void testLoadFail1() throws IOException {
		DatastoreTestHelper.setUpFixturesDatastore(new DSLoader(), "fixtures/lalala.yaml");
	}
	
	@Test(expected = YamlException.class)
	public void testLoadFail2() throws IOException {
		DatastoreTestHelper.setUpFixturesDatastore(new DSLoader(), "fixtures/wrong.yaml");
	}
	
	@Test
	public void testLoadOK1() throws IOException {
		User u = new User();
		u.setName("Danilo Queiroz");
		Address a = new Address();
		a.setStreetName("Avenida Cabo Branco");
		a.setZipcode("58045");
		u.setAddress(a);
		ds.saveObject(eq(u));
		replay(ds);
		DatastoreTestHelper.setUpFixturesDatastore(new DSLoader(), "fixtures/simple1.yaml");
		verify(ds);
	}
	
	@Test
	public void testLoadOK2() throws IOException {
		User u = new User();
		u.setName("Danilo Queiroz");
		Address a = new Address();
		a.setStreetName("Avenida Cabo Branco");
		a.setZipcode("58045");
		u.setAddress(a);
		ds.saveObject(eq(u));
		ds.saveObject(eq(u));
		u = new User();
		u.setName("Vitor Avelino");
		a = new Address();
		a.setStreetName("Rua Augusta");
		a.setZipcode("00000");
		u.setAddress(a);
		ds.saveObject(eq(u));
		replay(ds);
		DatastoreTestHelper.setUpFixturesDatastore(new DSLoader(), "fixtures/simple1.yaml" , "fixtures/simple2.yaml");
		verify(ds);
	}
	
	private class DSLoader implements DatastoreLoader {
		@Override
		public void load() {
		}

		@Override
		public DatastoreFacade datastoreFacade() throws IllegalStateException {
			return FixturesTest.this.ds;
		}
	}
}
