package edu.ucdavis.genomics.metabolomics.binbase.cluster.test.integration.complex;

import org.apache.log4j.Logger;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.handler.AbstractClusterHandler;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.test.integration.simple.SimpleClusterHandler;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.test.integration.simple.SimpleClusterJob;
import edu.ucdavis.genomics.metabolomics.util.status.ReportEvent;
import edu.ucdavis.genomics.metabolomics.util.status.ReportType;

/**
 * a more complex cluster handler which starts sub jobs to do the work.
 * i is based on the simple cluster job handler
 * @author wohlgemuth
 *
 */
public class ComplexClusterHandler extends AbstractClusterHandler {

    private Logger logger = Logger.getLogger(getClass());

    public static final ReportEvent FINISHED_EVENT = new ReportEvent(ComplexClusterHandler.class.getName(), ComplexClusterJob.class.getName());

    public static final ReportType FINISHED_TYPE = new ReportType(ComplexClusterHandler.class.getName(), ComplexClusterJob.class.getName());

    @Override
    protected boolean startProcessing() throws Exception {
        logger.info("received object of type: " + this.getObject().getClass().getName());
        logger.info("is it of expspected class:" + (this.getObject() instanceof ComplexClusterJob));
        ComplexClusterJob object = (ComplexClusterJob) this.getObject();
        if (object.getJobs() == null) {
            throw new Exception("you need to provide some values");
        }
        if (object.getJobs().length == 0) {
            throw new Exception("you need to provide some values");
        }
        SimpleClusterJob[] jobs = object.getJobs();
        for (SimpleClusterJob job : jobs) {
            this.startSubJob(job, SimpleClusterHandler.class.getName());
        }
        this.startLocalNode(false);
        this.waitForSubjobs();
        logger.info("we are done with the calculation");
        getReport().report(getObject().getClass().getName() + " - " + object.getId(), FINISHED_EVENT, FINISHED_TYPE);
        return true;
    }
}
