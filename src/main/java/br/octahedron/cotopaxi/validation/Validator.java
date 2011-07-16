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
package br.octahedron.cotopaxi.validation;

import static br.octahedron.cotopaxi.CotopaxiProperty.getProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import br.octahedron.cotopaxi.CotopaxiProperty;
import br.octahedron.cotopaxi.controller.BaseController;
import br.octahedron.util.Log;

/**
 * This entity is responsible by Validates inputs using validation rules;
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public final class Validator extends BaseController {
	
	private static final Log log = new Log(Validator.class);
	private Map<String, Collection<ValidationRule>> rules = new HashMap<String, Collection<ValidationRule>>();
	private Map<String, String> messages = new HashMap<String, String>();

	public void add(String attribute, ValidationRule rule, String message) {
		Collection<ValidationRule> rulesCollection = this.rules.get(attribute);
		if (rulesCollection == null) {
			rulesCollection = new LinkedList<ValidationRule>();
			this.rules.put(attribute, rulesCollection);
		}
		rulesCollection.add(rule);
		this.messages.put(attribute, message);
	}
	
	/**
	 * Check if this validator is valid. If no input parameter was set previously, it should return
	 * true.
	 * 
	 * @return <code>true</code> if all input parameters are valid, <code>false</code> otherwise.
	 */
	public boolean isValid() {
		Map<String, String> invalidMessages = new HashMap<String, String>();
		boolean valid = true;
		for(Map.Entry<String, Collection<ValidationRule>> entry : this.rules.entrySet()) {
			for(ValidationRule rule : entry.getValue()) {
				boolean validRule = rule.isValid(this.in(entry.getKey()));
				if (!validRule) {
					valid = false;
					invalidMessages.put(entry.getKey(), this.messages.get(entry.getKey()));
				} 
			}
		}
		log.debug("Validation result: %b. Invalid attributes: count %d; list %s", valid, invalidMessages.size(), invalidMessages.keySet().toString());
		this.out(getProperty(CotopaxiProperty.INVALID_PROPERTY), invalidMessages);
		return valid;	
	}
}
