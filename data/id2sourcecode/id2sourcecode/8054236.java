    public void merge(Lane nu) {
        Integer id = nu.getLaneId();
        Lane old = null;
        if (id != null) {
            old = findByID(id);
            if (old == null) {
                nu.setLaneId(null);
            }
        }
        if (old == null) {
            Session ssn = this.getSession(false);
            Query sql = null;
            Transaction tx = ssn.getTransaction();
            if (tx == null || !tx.isActive()) tx = ssn.beginTransaction();
            {
                sql = ssn.createSQLQuery("insert into lane (" + "	name, organism, description, tags, regions, skip," + "	sample_name, sample_code, sample_type," + "	real_experiment_id, experiment_id" + ") values (" + "	:name," + "	:organism," + "	:description," + "	:tags," + "	:regions," + "	:skip," + "	:sample_name," + "	:sample_code," + "	:sample_type," + "	:real_experiment_id," + "	:experiment_id " + ")");
                sql.setParameter("name", nu.getName());
                sql.setParameter("organism", nu.getOrganism());
                sql.setParameter("description", nu.getDescription());
                sql.setParameter("tags", nu.getTags());
                sql.setParameter("regions", nu.getRegions());
                sql.setParameter("skip", nu.getSkip());
                sql.setParameter("sample_name", nu.getSampleName());
                sql.setParameter("sample_code", nu.getSampleCode());
                sql.setParameter("sample_type", nu.getSampleType());
                Integer runId = null;
                Run run = nu.getRun();
                if (run != null) runId = run.getRunId();
                Integer expId = null;
                Experiment experiment = nu.getExperiment();
                if (experiment != null) expId = experiment.getExperimentId();
                sql.setParameter("experiment_id", runId, Hibernate.INTEGER);
                sql.setParameter("real_experiment_id", expId, Hibernate.INTEGER);
            }
            if (sql.executeUpdate() != 1) {
                tx.rollback();
            } else {
                tx.commit();
            }
            if (!tx.isActive()) tx.begin();
        } else {
            Session ssn = this.getSession(false);
            Query sql = null;
            Transaction tx = ssn.getTransaction();
            if (tx == null || !tx.isActive()) tx = ssn.beginTransaction();
            {
                sql = ssn.createSQLQuery("update	lane" + "	set	name			= :name," + "		organism		= :organism," + "		description		= :description," + "		tags			= :tags," + "		regions			= :regions," + "		skip			= :skip," + "		sample_name		= :sample_name," + "		sample_code		= :sample_code," + "		sample_type		= :sample_type," + "		experiment_id	= :experiment_id " + "where lane_id			= :lane_id");
                sql.setParameter("name", nu.getName());
                sql.setParameter("organism", nu.getOrganism());
                sql.setParameter("description", nu.getDescription());
                sql.setParameter("tags", nu.getTags());
                sql.setParameter("regions", nu.getRegions());
                sql.setParameter("skip", nu.getSkip());
                sql.setParameter("sample_name", nu.getSampleName());
                sql.setParameter("sample_code", nu.getSampleCode());
                sql.setParameter("sample_type", nu.getSampleType());
                Integer runId = null;
                Run run = nu.getRun();
                if (run != null) runId = run.getRunId();
                sql.setParameter("experiment_id", runId, Hibernate.INTEGER);
                sql.setParameter("lane_id", nu.getLaneId());
            }
            if (sql.executeUpdate() != 1) {
                tx.rollback();
            } else {
                tx.commit();
            }
            if (!tx.isActive()) tx.begin();
        }
    }
