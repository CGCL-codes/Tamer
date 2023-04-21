    public TimmonId doCreate(TimmonId parent_id, int type) throws SecurityException, RemoteException, IllegalArgumentException, TimmonBackEndException {
        boolean commit = false;
        try {
            commit = connection_.getAutoCommit();
            if (commit) connection_.setAutoCommit(false);
        } catch (SQLException exc) {
            throw (new TimmonBackEndException(exc));
        }
        try {
            sql_create_timmon_id_stmt_.executeUpdate();
            ResultSet rs = sql_create_timmon_id_stmt_.getGeneratedKeys();
            int id = -1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            if (id < 0) throw (new TimmonBackEndException("Could not create id!"));
            SQLTimmonId timmon_id = new SQLTimmonId(id, type);
            if (isTypeOf(TYPE_TASK_MASK, type)) {
                sql_set_parent_of_task_stmt_.setInt(1, id);
                sql_set_parent_of_task_stmt_.setInt(2, user_id_);
                sql_set_parent_of_task_stmt_.setInt(3, (int) ((SQLTimmonId) parent_id).getTimmonId());
                sql_set_parent_of_task_stmt_.executeUpdate();
                createDefaultTaskProperties((SQLTimmonId) parent_id, timmon_id);
            } else if (isTypeOf(TYPE_ACTIVITY_MASK, type)) {
                sql_set_parent_of_activity_stmt_.setInt(1, id);
                sql_set_parent_of_activity_stmt_.setInt(2, user_id_);
                sql_set_parent_of_activity_stmt_.setInt(3, (int) ((SQLTimmonId) parent_id).getTimmonId());
                sql_set_parent_of_activity_stmt_.executeUpdate();
                craeteDefaultActivityProperties((SQLTimmonId) parent_id, timmon_id);
            } else if (isTypeOf(TYPE_USER_MASK, type)) {
                sql_add_user_stmt_.setInt(1, id);
                sql_add_user_stmt_.executeUpdate();
                createDefaultUserProperties(timmon_id);
                SQLTimmonId sql_id = (SQLTimmonId) doGetGlobalProperty(GPROP_KEY_ROOT_GROUP_ID);
                sql_add_user_to_group_stmt_.setInt(1, (int) sql_id.getTimmonId());
                sql_add_user_to_group_stmt_.setInt(2, id);
                sql_add_user_to_group_stmt_.executeUpdate();
                if ((int) sql_id.getTimmonId() != (int) ((SQLTimmonId) parent_id).getTimmonId()) {
                    sql_add_user_to_group_stmt_.setInt(1, (int) ((SQLTimmonId) parent_id).getTimmonId());
                    sql_add_user_to_group_stmt_.setInt(2, id);
                    sql_add_user_to_group_stmt_.executeUpdate();
                }
            } else if (isTypeOf(TYPE_GROUP_MASK, type)) {
                sql_add_group_stmt_.setInt(1, id);
                sql_add_group_stmt_.setInt(2, (int) ((SQLTimmonId) parent_id).getTimmonId());
                sql_add_group_stmt_.executeUpdate();
                createDefaultGroupProperties(timmon_id);
            } else throw (new TimmonBackEndException("unknown type: " + type));
            connection_.commit();
            return (new SQLTimmonId(id, type));
        } catch (SQLException e) {
            if (Debug.DEBUG) Debug.println("exc", e + "\n" + Debug.getStackTrace(e));
            if (commit) {
                try {
                    connection_.rollback();
                } catch (SQLException e1) {
                    throw (new TimmonBackEndException(e1));
                }
            }
            throw (new TimmonBackEndException(e));
        } finally {
            try {
                if (commit) connection_.setAutoCommit(true);
            } catch (SQLException e1) {
            }
        }
    }
