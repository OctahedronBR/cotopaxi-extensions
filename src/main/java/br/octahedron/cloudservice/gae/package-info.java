/**
 * Provides the CloudServices implementation for Google App Engine.
 * 
 * Only entities on this package should invoke GAE api directly.
 * 
 * <b>Datastore</b> sub-package (<code>tnb.googleservice.datacontrole</code>) 
 * provides a set of classes necessary to store and recovery data from GAE's 
 * Datastore Service, such as mentawai Filter, exceptions, actions annotations,
 * an Adapter for provides a High-Level to the <code>DatastoreFacade</code>.
 * 
 * <b>Note for Developers:</b> All public Facades/Adapters MUST provides a java 
 * interface to be possible to create mocks for them.
 */
package br.octahedron.cloudservice.gae;

