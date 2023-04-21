    public static void writeStatData(int siteid, int uvCount, int source) throws Exception {
        Calendar cal = Calendar.getInstance();
        int statDate = cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE);
        Session ssn = getSession();
        try {
            beginTransaction();
            Query update_q = ssn.getNamedQuery((siteid > 0) ? "UPDATE_SITE_STAT_1" : "UPDATE_SITE_STAT_2");
            update_q.setInteger(0, uvCount);
            update_q.setInteger(1, statDate);
            update_q.setInteger(2, source);
            if (siteid > 0) update_q.setInteger(3, siteid);
            if (update_q.executeUpdate() < 1) {
                SiteStatBean ssb = new SiteStatBean();
                ssb.setSiteId(siteid);
                ssb.setUvCount(uvCount);
                ssb.setUpdateTime(new Date());
                ssb.setSource(source);
                ssb.setStatDate(statDate);
                ssn.save(ssb);
            }
            commit();
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }
