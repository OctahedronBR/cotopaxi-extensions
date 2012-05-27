package br.octahedron.cotopaxi.datastore.test;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * Provides method do handle the fixtures to the datastore.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class Fixtures {

	/**
	 * Loads the given fixture to the datastore.
	 * 
	 * @param dsFacade
	 *            The {@link DatastoreFacade} - used to persist the loaded
	 *            fixtures.
	 * @param fixturesPath
	 *            The path for the fixtures. Fixtures should be at the resources
	 *            folder - the will be loaded using
	 *            {@link ClassLoader#getResourceAsStream(String)} method.
	 * @throws IOException
	 *             If some error occurs loading or parsing the fixtures files.
	 */
	public static void load(DatastoreFacade dsFacade, String... fixturesPath) throws IOException {
		for (String fixture : fixturesPath) {
			InputStream in = ClassLoader.getSystemResourceAsStream(fixture);
			try {
				if (in != null) {
					parseFixtures(dsFacade, new InputStreamReader(in));
				} else {
					throw new IOException(format("Unable to load fixture %s", fixture));
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}
	}

	/**
	 * Parses the objects from the given yaml readers and save at the datastore
	 */
	private static void parseFixtures(DatastoreFacade dsFacade, Reader reader) throws YamlException {
		YamlReader yaml = new YamlReader(reader);
		Object obj;
		while ((obj = yaml.read()) != null) {
			dsFacade.saveObject(obj);
		}
	}

}
