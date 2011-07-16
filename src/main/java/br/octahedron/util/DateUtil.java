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
package br.octahedron.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilitaria para lidar com datas
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class DateUtil {

	/**
	 * Gets the current datetime
	 * @return the current datetime
	 */
	public static Date getDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Recupera uma data futura, dado o numero de dias.
	 * 
	 * @param numberOfDays
	 *            o numero de dias ate a data que se deseja.
	 * @return a data futura.
	 */
	public static Date getFutureDate(int numberOfDays) {
		if (numberOfDays > 0) {
			// convert number of days to milliseconds
			long millis = (long) numberOfDays * 24 * 60 * 1000;
			Date future = (Date) getDate().clone();
			future.setTime(future.getTime() + millis);
			return future;
		} else {
			return getDate();
		}
	}

}
