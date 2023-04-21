    public boolean update(DaoParam param) {
        boolean ret = false;
        if (param == null) param = new DaoParam();
        String sql = param.getSql();
        if (sql == null || sql.length() < 10) {
            _log.showWarn("update sql param is null! ");
            return false;
        }
        List<String> lsType = param.getType();
        List<String> lsValue = param.getValue();
        if (lsType.size() != lsValue.size()) {
            _log.showWarn("update type and value size differ! ");
            return false;
        }
        String dataSource = param.getDsName();
        Connection con = null;
        PreparedStatement ps = null;
        TransactionObject tranObj = null;
        try {
            if (param.isUseTransaction()) {
                tranObj = _tranMng.getTransactionObject();
                con = tranObj.getConnection(dataSource);
            } else {
                con = PooledConnection.getInstance().getConnection(dataSource);
                if (con != null) con.setAutoCommit(true);
            }
            if (con == null) {
                _log.showWarn("connection is null sql=" + sql);
                return false;
            }
            ps = con.prepareStatement(sql);
            if (!lsValue.isEmpty()) {
                ps = DaoUtil.setPreStmParams(lsValue, lsType, ps);
            }
            long curTime = System.currentTimeMillis();
            int iret = ps.executeUpdate();
            if (iret >= 0) ret = true;
            if (param.isUseTransaction()) {
                if (iret >= 0) {
                    tranObj.commit();
                } else {
                    tranObj.rollback();
                }
            }
            DaoUtil.showUpdateTime(curTime, sql);
        } catch (TransactionException e) {
            DaoUtil.closeTranObj(tranObj);
            DaoUtil.showException(e, sql);
            return false;
        } catch (SQLException e) {
            DaoUtil.closeTranObj(tranObj);
            DaoUtil.showException(e, sql);
            return false;
        } catch (Exception e) {
            DaoUtil.closeTranObj(tranObj);
            DaoUtil.showException(e, sql);
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                ps = null;
                if (!param.isUseTransaction()) {
                    if (con != null) {
                        con.close();
                    }
                    con = null;
                }
            } catch (SQLException e) {
                _log.showError(e);
            }
        }
        return ret;
    }
