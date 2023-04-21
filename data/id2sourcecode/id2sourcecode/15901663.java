    public static int userLogout(int userid, Timestamp lastLogin, boolean manual_logout) {
        Session ssn = getSession();
        if (ssn == null) return -1;
        try {
            beginTransaction();
            Query q = ssn.getNamedQuery(manual_logout ? "USER_LOGOUT_1" : "USER_LOGOUT_2");
            q.setInteger("online_status", UserBean.STATUS_OFFLINE);
            if (manual_logout) q.setInteger("keep_day", 0);
            q.setInteger("user_id", userid);
            q.setTimestamp("last_time", lastLogin);
            int er = q.executeUpdate();
            commit();
            return er;
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }
