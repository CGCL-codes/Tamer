    public void update(Run nu) {
        Session ssn = this.getSession(false);
        Query sql = null;
        Transaction tx = ssn.getTransaction();
        if (tx == null || !tx.isActive()) tx = ssn.beginTransaction();
        sql = ssn.createSQLQuery("update	experiment" + "	set	name			= :name," + "		description		= :description," + "		cycles			= :cycles," + "		ref_lane		= :ref_lane" + " " + "where experiment_id	= :experiment_id");
        Integer ref_lane = nu.getRefLane();
        if (ref_lane == null) {
            ref_lane = new Integer(1);
        }
        sql.setParameter("name", nu.getName());
        sql.setParameter("description", nu.getDescription());
        sql.setParameter("cycles", nu.getCycles());
        sql.setParameter("ref_lane", ref_lane, Hibernate.INTEGER);
        sql.setParameter("experiment_id", nu.getRunId(), Hibernate.INTEGER);
        if (sql.executeUpdate() != 1) {
            tx.rollback();
        } else {
            ArrayList<Lane> lanes = new ArrayList(8);
            if (nu.getLane1() != null) lanes.add(nu.getLane1());
            if (nu.getLane2() != null) lanes.add(nu.getLane2());
            if (nu.getLane3() != null) lanes.add(nu.getLane3());
            if (nu.getLane4() != null) lanes.add(nu.getLane4());
            if (nu.getLane5() != null) lanes.add(nu.getLane5());
            if (nu.getLane6() != null) lanes.add(nu.getLane6());
            if (nu.getLane7() != null) lanes.add(nu.getLane7());
            if (nu.getLane8() != null) lanes.add(nu.getLane8());
            sql = ssn.createSQLQuery("update	lane" + "	set	name			= :name," + "		organism		= :organism," + "		description		= :description," + "		tags			= :tags," + "		regions			= :regions," + "		skip			= :skip," + "		sample_name		= :sample_name," + "		sample_code		= :sample_code," + "		sample_type		= :sample_type," + "		experiment_id	= :experiment_id " + "where lane_id			= :lane_id " + "  and	(experiment_id		is null " + "		or experiment_id	= :experiment_id)");
            for (Lane lane : lanes) {
                sql.setParameter("name", lane.getName());
                sql.setParameter("organism", lane.getOrganism());
                sql.setParameter("description", lane.getDescription());
                sql.setParameter("tags", lane.getTags());
                sql.setParameter("regions", lane.getRegions());
                sql.setParameter("skip", lane.getSkip());
                sql.setParameter("sample_name", lane.getSampleName());
                sql.setParameter("sample_code", lane.getSampleCode());
                sql.setParameter("sample_type", lane.getSampleType());
                sql.setParameter("experiment_id", nu.getRunId(), Hibernate.INTEGER);
                sql.setParameter("lane_id", lane.getLaneId());
                if (sql.executeUpdate() != 1) {
                    tx.rollback();
                    if (!tx.isActive()) tx.begin();
                    return;
                }
            }
            tx.commit();
        }
        if (!tx.isActive()) tx.begin();
    }
