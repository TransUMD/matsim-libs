/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2017 by the members listed in the COPYING,        *
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

package org.matsim.contrib.taxi.run;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.*;
import org.matsim.contrib.dvrp.data.file.VehicleReader;
import org.matsim.contrib.dvrp.optimizer.VrpOptimizer;
import org.matsim.contrib.dvrp.passenger.PassengerRequestCreator;
import org.matsim.contrib.dvrp.run.*;
import org.matsim.contrib.dvrp.vrpagent.VrpAgentLogic.DynActionCreator;
import org.matsim.contrib.taxi.benchmark.DvrpBenchmarkModule;
import org.matsim.contrib.taxi.optimizer.*;
import org.matsim.contrib.taxi.passenger.TaxiRequestCreator;
import org.matsim.contrib.taxi.vrpagent.TaxiActionCreator;
import org.matsim.core.config.Config;
import org.matsim.core.controler.AbstractModule;

import com.google.inject.*;
import com.google.inject.name.Named;

public class TaxiOptimizerModules {//rename to TaxiModules? or DvrpModulesForTaxi?
	public static final String TAXI_MODE = "taxi";

	public static AbstractModule createDefaultModule() {
		return new DvrpModule(createModuleForQSimPlugin(DefaultTaxiOptimizerProvider.class), TaxiOptimizer.class) {
			@Provides
			@Singleton
			private Fleet provideVehicles(@Named(DvrpModule.DVRP_ROUTING) Network network, Config config,
					TaxiConfigGroup taxiCfg) {
				FleetImpl fleet = new FleetImpl();
				new VehicleReader(network, fleet).parse(taxiCfg.getTaxisFileUrl(config.getContext()));
				return fleet;
			}
		};
	}

	public static AbstractModule createModule(Class<? extends Provider<? extends TaxiOptimizer>> providerClass) {
		return new DvrpModule(createModuleForQSimPlugin(providerClass), TaxiOptimizer.class) {
			@Provides
			@Singleton
			private Fleet provideVehicles(@Named(DvrpModule.DVRP_ROUTING) Network network, Config config,
					TaxiConfigGroup taxiCfg) {
				FleetImpl fleet = new FleetImpl();
				new VehicleReader(network, fleet).parse(taxiCfg.getTaxisFileUrl(config.getContext()));
				return fleet;
			}
		};
	}

	public static AbstractModule createBenchmarkModule() {
		return new DvrpBenchmarkModule(createModuleForQSimPlugin(DefaultTaxiOptimizerProvider.class),
				TaxiOptimizer.class) {
			@Provides
			@Singleton
			private Fleet provideVehicles(@Named(DvrpModule.DVRP_ROUTING) Network network, Config config,
					TaxiConfigGroup taxiCfg) {
				FleetImpl fleet = new FleetImpl();
				new VehicleReader(network, fleet).parse(taxiCfg.getTaxisFileUrl(config.getContext()));
				return fleet;
			}
		};
	}

	private static com.google.inject.AbstractModule createModuleForQSimPlugin(
			final Class<? extends Provider<? extends TaxiOptimizer>> providerClass) {
		return new com.google.inject.AbstractModule() {
			@Override
			protected void configure() {
				bind(TaxiOptimizer.class).toProvider(providerClass).asEagerSingleton();
				bind(VrpOptimizer.class).to(TaxiOptimizer.class);
				bind(DynActionCreator.class).to(TaxiActionCreator.class).asEagerSingleton();
				bind(PassengerRequestCreator.class).to(TaxiRequestCreator.class).asEagerSingleton();
			}
		};
	}
}
