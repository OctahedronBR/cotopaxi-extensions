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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.octahedron.cotopaxi.cloudservice.EmailFacade;

/**
 * EmailFacade implementation for GAE.
 * 
 * @see EmailFacade
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class EmailFacadeImpl implements EmailFacade {

	private Logger logger = Logger.getLogger(EmailFacadeImpl.class.getName());

	@Override
	public void sendMail(String senderName, String senderAddress, String dest, String subject, String content, String mimeType) {
		try {
			// prepare session and create the message
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			Message msg = new MimeMessage(session);
			// set the from field
			msg.setFrom(new InternetAddress(senderAddress, senderName));
			// set the to field
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
			// set subject
			msg.setSubject(subject);
			// set email content and mime type
			msg.setContent(content, mimeType);

			// now, send it! :-)
			Transport.send(msg);

		} catch (Exception ex) {
			this.logger.log(Level.WARNING, "Error sendind email to " + dest + ". Error message: " + ex.getLocalizedMessage(), ex);
		}
	}

}
