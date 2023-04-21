package playground.mrieser.pt.application;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.population.PopulationReader;
import org.matsim.core.population.PopulationWriter;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.population.algorithms.PlansFilterByLegMode;
import org.matsim.population.algorithms.PlansFilterByLegMode.FilterType;
import org.matsim.population.filters.PersonIntersectAreaFilter;
import playground.mrieser.PseudoScenario;

public class PlansPrepare {

    private static final String NETWORK_FILENAME = "/Volumes/Data/VSP/svn/shared-svn/studies/schweiz-ivtch/baseCase/network/ivtch-osm.xml";

    private static final String ALL_PLANS_FILENAME = "/Volumes/Data/VSP/svn/shared-svn/studies/schweiz-ivtch/baseCase/plans/plans_complete/plans.xml.gz";

    private static final String DILUTED_100PCT_PLANS_FILENAME = "/Volumes/Data/VSP/coding/eclipse35/thesis-data/application/plans.census2000ivtch100pct.dilZh30km.xml.gz";

    private static final String DILUTED_10PCT_PLANS_FILENAME = "/Volumes/Data/VSP/coding/eclipse35/thesis-data/application/plans.census2000ivtch10pct.dilZh30km.xml.gz";

    private static final String DILUTED_1PCT_PLANS_FILENAME = "/Volumes/Data/VSP/coding/eclipse35/thesis-data/application/plans.census2000ivtch1pct.dilZh30km.xml.gz";

    private static final String DILUTED_CAR_1PCT_PLANS_FILENAME = "/Volumes/Data/VSP/coding/eclipse35/thesis-data/application/plans.census2000ivtch1pct.dilZh30km.car.xml.gz";

    private static final String DILUTED_PT_1PCT_PLANS_FILENAME = "/Volumes/Data/VSP/coding/eclipse35/thesis-data/application/plans.census2000ivtch1pct.dilZh30km.pt.xml.gz";

    private final ScenarioImpl scenario;

    private static final Logger log = Logger.getLogger(PlansPrepare.class);

    public PlansPrepare() {
        this.scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
    }

    public void loadNetwork(final String filename) {
        log.info("reading network: " + filename);
        new MatsimNetworkReader(this.scenario).readFile(filename);
        log.info("network-statistics:");
        log.info("  # nodes = " + this.scenario.getNetwork().getNodes().size());
        log.info("  # links = " + this.scenario.getNetwork().getLinks().size());
    }

    public void createDilutedPlans(final CoordImpl center, final double radius, final String fromFile, final String toFile) {
        final Map<Id, Link> areaOfInterest = new HashMap<Id, Link>();
        Network network = this.scenario.getNetwork();
        log.info("extracting aoi:");
        log.info("  center: " + center.getX() + " / " + center.getY());
        log.info("  radius: " + radius);
        for (Link link : network.getLinks().values()) {
            final Node from = link.getFromNode();
            final Node to = link.getToNode();
            if ((CoordUtils.calcDistance(from.getCoord(), center) <= radius) || (CoordUtils.calcDistance(to.getCoord(), center) <= radius)) {
                System.out.println("    link " + link.getId().toString());
                areaOfInterest.put(link.getId(), link);
            }
        }
        log.info("  # links in aoi: " + areaOfInterest.size());
        log.info("creating diluted dpopulation:");
        log.info("  input-file:  " + fromFile);
        log.info("  output-file: " + toFile);
        PopulationImpl pop = (PopulationImpl) ((ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig())).getPopulation();
        pop.setIsStreaming(true);
        PopulationWriter writer = new PopulationWriter(pop, this.scenario.getNetwork());
        writer.startStreaming(toFile);
        final PersonIntersectAreaFilter filter = new PersonIntersectAreaFilter(writer, areaOfInterest, network);
        filter.setAlternativeAOI(center, radius);
        pop.addAlgorithm(filter);
        new MatsimPopulationReader(new PseudoScenario(this.scenario, pop)).readFile(fromFile);
        writer.closeStreaming();
        pop.printPlansCount();
        log.info("persons in output: " + filter.getCount());
    }

    public void createSamplePopulation(final String fromFile, final String toFile, final double percentage) {
        PopulationImpl pop = (PopulationImpl) ((ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig())).getPopulation();
        pop.setIsStreaming(true);
        final PopulationWriter plansWriter = new PopulationWriter(pop, this.scenario.getNetwork(), percentage);
        plansWriter.startStreaming(toFile);
        pop.addAlgorithm(plansWriter);
        PopulationReader plansReader = new MatsimPopulationReader(new PseudoScenario(this.scenario, pop));
        log.info("extracting sample from population:");
        log.info("  input-file:  " + fromFile);
        log.info("  output-file: " + toFile);
        log.info("  sample-size: " + percentage);
        plansReader.readFile(fromFile);
        pop.printPlansCount();
        plansWriter.closeStreaming();
    }

    public void filterMode(final String fromFile, final String toFile, final String mode) {
        Population pop = ((ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig())).getPopulation();
        log.info("reading plans from file: " + fromFile);
        new MatsimPopulationReader(new PseudoScenario(this.scenario, pop)).readFile(fromFile);
        log.info("filter plans with " + mode + "-legs");
        new PlansFilterByLegMode(mode, FilterType.keepAllPlansWithMode).run(pop);
        log.info("# persons remaining: " + pop.getPersons().size());
        log.info("writing plans to file: " + toFile);
        new PopulationWriter(pop, this.scenario.getNetwork()).write(toFile);
    }

    public static void main(final String[] args) {
        PlansPrepare app = new PlansPrepare();
        app.loadNetwork(NETWORK_FILENAME);
        app.filterMode(DILUTED_1PCT_PLANS_FILENAME, DILUTED_CAR_1PCT_PLANS_FILENAME, TransportMode.car);
        app.filterMode(DILUTED_1PCT_PLANS_FILENAME, DILUTED_PT_1PCT_PLANS_FILENAME, TransportMode.pt);
        log.info("done.");
    }
}
