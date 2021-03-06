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

package org.matsim.contrib.dynagent.util;

import java.util.Iterator;

import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contrib.dynagent.DynAction;
import org.matsim.contrib.dynagent.DynActivity;
import org.matsim.contrib.dynagent.DynAgent;
import org.matsim.contrib.dynagent.DynAgentLogic;
import org.matsim.contrib.dynagent.IdleDynActivity;
import org.matsim.contrib.dynagent.StaticDriverDynLeg;
import org.matsim.core.population.routes.NetworkRoute;

/**
 * This class could be useful for jUnit testing of compatibility of DynAgent with PersonDriverAgentImpl (i.e. comparing
 * events thrown during 2 different QSims, one with {@code PlanToDynAgentLogicAdapter} while the other with
 * {@code PersonDriverAgentImpl}).
 *
 * @author michalm
 */
public class PlanToDynAgentLogicAdapter implements DynAgentLogic {
	private DynAgent agent;
	private final Iterator<PlanElement> planElemIter;

	/**
	 * @param plan
	 *            (always starts with Activity)
	 */
	public PlanToDynAgentLogicAdapter(Plan plan) {
		planElemIter = plan.getPlanElements().iterator();
	}

	@Override
	public DynActivity computeInitialActivity(DynAgent adapterAgent) {
		this.agent = adapterAgent;

		Activity act = (Activity)planElemIter.next();
		return new IdleDynActivity(act.getType(), act.getEndTime().seconds());
	}

	@Override
	public DynAgent getDynAgent() {
		return agent;
	}

	@Override
	public DynAction computeNextAction(DynAction oldAction, double now) {
		PlanElement planElem = planElemIter.next();

		if (planElem instanceof Activity) {
			Activity act = (Activity)planElem;
			return new IdleDynActivity(act.getType(), act.getEndTime().seconds());
		}

		// only the 'car' mode supported right now
		Leg leg = (Leg)planElem;
		return new StaticDriverDynLeg(leg.getMode(), (NetworkRoute)leg.getRoute());
	}
}
