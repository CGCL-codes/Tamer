    public void merge(Experiment nu) {
        Integer id = nu.getExperimentId();
        Experiment old = null;
        if (id != null) {
            old = findByID(id);
            if (old == null) {
                nu.setExperimentId(null);
            }
        }
        if (old == null) {
            this.getHibernateTemplate().save(nu);
        } else {
            Session ssn = this.getSession(false);
            Query sql = null;
            Transaction tx = ssn.getTransaction();
            if (tx == null || !tx.isActive()) tx = ssn.beginTransaction();
            {
                sql = ssn.createSQLQuery("update	real_experiment" + "	set	name			= :name," + "		description		= :description" + " " + "where experiment_id	= :experiment_id");
                sql.setParameter("name", nu.getName());
                sql.setParameter("description", nu.getDescription());
                sql.setParameter("experiment_id", nu.getExperimentId());
            }
            if (sql.executeUpdate() != 1) {
                tx.rollback();
            } else {
                tx.commit();
            }
            if (!tx.isActive()) tx.begin();
        }
    }
