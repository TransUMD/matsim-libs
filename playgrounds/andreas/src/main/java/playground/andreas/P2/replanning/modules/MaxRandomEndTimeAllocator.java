/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2011 by the members listed in the COPYING,        *
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

package playground.andreas.P2.replanning.modules;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.gbl.MatsimRandom;

import playground.andreas.P2.operator.Cooperative;
import playground.andreas.P2.replanning.PPlan;
import playground.andreas.P2.replanning.PStrategy;

/**
 * 
 * Sets a new endTime between the old endTime and midnight.
 * 
 * @author aneumann
 *
 */
public class MaxRandomEndTimeAllocator extends PStrategy {
	
	private final static Logger log = Logger.getLogger(MaxRandomEndTimeAllocator.class);	
	public static final String STRATEGY_NAME = "MaxRandomEndTimeAllocator";
	
	private final int mutationRange;
	
	public MaxRandomEndTimeAllocator(ArrayList<String> parameter) {
		super(parameter);
		if(parameter.size() != 1){
			log.error("Missing parameter: 1 - Mutation range in seconds");
		}
		this.mutationRange = Integer.parseInt(parameter.get(0));
	}
	
	@Override
	public PPlan run(Cooperative cooperative) {
		if (cooperative.getBestPlan().getNVehicles() <= 1) {
			return null;
		}
		
		// enough vehicles to test, change endTime
		PPlan newPlan = new PPlan(new IdImpl(cooperative.getCurrentIteration()), this.getName());
		newPlan.setStopsToBeServed(cooperative.getBestPlan().getStopsToBeServed());
		newPlan.setStartTime(cooperative.getBestPlan().getStartTime());
		
		// get a valid new end time
		double newEndTime = Math.min(24 * 3600.0, cooperative.getBestPlan().getEndTime() + MatsimRandom.getRandom().nextDouble() * this.mutationRange);
		newEndTime = Math.max(newEndTime, cooperative.getBestPlan().getStartTime() + cooperative.getMinOperationTime());
		newPlan.setEndTime(newEndTime);
		
		newPlan.setLine(cooperative.getRouteProvider().createTransitLine(cooperative.getId(), newPlan.getStartTime(), newPlan.getEndTime(), 1, newPlan.getStopsToBeServed(), new IdImpl(cooperative.getCurrentIteration())));
		
		return newPlan;
	}

	@Override
	public String getName() {
		return MaxRandomEndTimeAllocator.STRATEGY_NAME;
	}

}
