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
package br.octahedron.cloudservice.gae;

import br.octahedron.cloudservice.gae.ds.DatastoreFacadeImpl;
import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.cloudservice.DistributedCounter;
import br.octahedron.cotopaxi.cloudservice.DistributedLock;
import br.octahedron.cotopaxi.cloudservice.EmailFacade;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;
import br.octahedron.cotopaxi.cloudservice.TaskManagerFacade;
import br.octahedron.cotopaxi.cloudservice.URLFetchFacade;

/**
 * Google Cloud Services Factory.
 * 
 * @see CloudServicesFactory
 * 
 * @author Danilo Penna Queiroz - email@octahedron.com.br
 * 
 */
public class GoogleCloudServicesFactory implements CloudServicesFactory {

	private static final GoogleCloudServicesFactory instance = new GoogleCloudServicesFactory();

	public static GoogleCloudServicesFactory getInstance() {
		return instance;
	}

	private static final String DEFAULT_URL_PREFIX = "/tasks/";

	private GoogleCloudServicesFactory() {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.CloudServicesFactory#createDistributedCounter(java.lang.String)
	 */
	@Override
	public DistributedCounter createDistributedCounter(String counterName) {
		return new DistributedCounterImpl(counterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.CloudServicesFactory#createDistributedLock(java.lang.String)
	 */
	@Override
	public DistributedLock createDistributedLock(String lockName) {
		return new DistributedLockImpl(lockName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.CloudServicesFactory#createDatastoreFacade()
	 */
	@Override
	public DatastoreFacade createDatastoreFacade() {
		return new DatastoreFacadeImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.CloudServicesFactory#createEmailFacade()
	 */
	@Override
	public EmailFacade createEmailFacade() {
		return new EmailFacadeImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.CloudServicesFactory#createMemcacheFacade()
	 */
	@Override
	public MemcacheFacade createMemcacheFacade() {
		return new MemcacheFacadeImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.CloudServicesFactory#createURLFetchFacade()
	 */
	@Override
	public URLFetchFacade createURLFetchFacade() {
		return new URLFetchFacadeImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.CloudServicesFactory#createTaskManagerFacade()
	 */
	@Override
	public TaskManagerFacade createTaskManagerFacade() {
		return this.createTaskManagerFacade(DEFAULT_URL_PREFIX);
	}

	public TaskManagerFacade createTaskManagerFacade(String urlPrefix) {
		return new TaskManagerFacadeImpl(urlPrefix);
	}

}
