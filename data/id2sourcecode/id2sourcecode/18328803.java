    private void createTabAccess() throws Exception {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        MRole valueRol;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        String sql;
        int nro;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                valueRol = (MRole) this.getRoles().get(key);
                List<MTabAccess> tabRoles = MTabAccess.getOfRoleInList(key.intValue(), this.getTrxName());
                for (MTabAccess auxMta : tabRoles) {
                    sql = "INSERT INTO ad_tab_access (ad_tab_access_id,ad_tab_id,ad_window_id,ad_role_id,ad_client_id,ad_org_id,isactive,createdby,updatedby,isreadwrite) VALUES (?,?,?,?,?,?,?,?,?,?)";
                    nro = MSequence.getNextID(this.getNewClient(), "AD_Tab_Access", this.getTrxName());
                    psmt = DB.prepareStatement(sql, this.getTrxName());
                    psmt.setInt(1, nro);
                    psmt.setInt(2, auxMta.getAD_Tab_ID());
                    psmt.setInt(3, auxMta.getAD_Window_ID());
                    psmt.setInt(4, valueRol.getID());
                    psmt.setInt(5, this.getNewClient());
                    psmt.setInt(6, 0);
                    psmt.setString(7, (auxMta.isActive()) ? "Y" : "N");
                    psmt.setInt(8, 100);
                    psmt.setInt(9, 100);
                    psmt.setString(10, (auxMta.isReadWrite()) ? "Y" : "N");
                    psmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                rs.close();
                psmt.close();
                psmt = null;
            } catch (Exception e) {
                psmt = null;
            }
        }
    }
