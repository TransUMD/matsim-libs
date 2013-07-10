/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2012 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package playground.droeder.ptTutorial;

import org.apache.log4j.Logger;
import org.matsim.core.controler.Controler;

/**
 * @author droeder
 *
 */
public class PtTutorialControler {

	@SuppressWarnings("unused")
	private static final Logger log = Logger
			.getLogger(PtTutorialControler.class);

	public static void main(String[] args) {
		Controler c = new Controler("E:/sandbox/org.matsim/examples/pt-tutorial/config.xml");
		c.setOverwriteFiles(true);
		c.run();
	}
}

