    public boolean createUsersFeatures(String userClient, String userOrg) {
        String name = m_clientName + " Admin";
        MRole admin = new MRole(m_ctx, 0, m_trx.getTrxName());
        admin.setClientOrg(m_client);
        admin.setName(name);
        admin.setUserLevel(MRole.USERLEVEL_ClientPlusOrganization);
        admin.setPreferenceType(MRole.PREFERENCETYPE_Client);
        admin.setIsShowAcct(true);
        if (!admin.save()) {
            String err = "Admin Role A NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        MRoleOrgAccess adminClientAccess = new MRoleOrgAccess(admin, 0);
        if (!adminClientAccess.save()) {
            log.log(Level.SEVERE, "Admin Role_OrgAccess 0 NOT created");
        }
        MRoleOrgAccess adminOrgAccess = new MRoleOrgAccess(admin, m_org.getAD_Org_ID());
        if (!adminOrgAccess.save()) {
            log.log(Level.SEVERE, "Admin Role_OrgAccess NOT created");
        }
        m_info.append(Msg.translate(m_lang, "AD_Role_ID")).append("=").append(name).append("\n");
        name = m_clientName + " User";
        MRole user = new MRole(m_ctx, 0, m_trx.getTrxName());
        user.setClientOrg(m_client);
        user.setName(name);
        if (!user.save()) {
            String err = "User Role A NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        MRoleOrgAccess userOrgAccess = new MRoleOrgAccess(user, m_org.getAD_Org_ID());
        if (!userOrgAccess.save()) {
            log.log(Level.SEVERE, "User Role_OrgAccess NOT created");
        }
        m_info.append(Msg.translate(m_lang, "AD_Role_ID")).append("=").append(name).append("\n");
        name = userClient;
        if ((name == null) || (name.length() == 0)) {
            name = m_clientName + "Client";
        }
        AD_User_ID = getNextID(m_client.getAD_Client_ID(), "AD_User");
        AD_User_Name = name;
        name = DB.TO_STRING(name);
        String sql = "INSERT INTO AD_User(" + m_stdColumns + ",AD_User_ID," + "Name,Description,Password)" + " VALUES (" + m_stdValues + "," + AD_User_ID + "," + name + "," + name + "," + name + ")";
        int no = DB.executeUpdate(sql, m_trx.getTrxName());
        if (no != 1) {
            String err = "Admin User NOT inserted - " + AD_User_Name;
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        m_info.append(Msg.translate(m_lang, "AD_User_ID")).append("=").append(AD_User_Name).append("/").append(AD_User_Name).append("\n");
        name = userOrg;
        if ((name == null) || (name.length() == 0)) {
            name = m_clientName + "Org";
        }
        AD_User_U_ID = getNextID(m_client.getAD_Client_ID(), "AD_User");
        AD_User_U_Name = name;
        name = DB.TO_STRING(name);
        sql = "INSERT INTO AD_User(" + m_stdColumns + ",AD_User_ID," + "Name,Description,Password)" + " VALUES (" + m_stdValues + "," + AD_User_U_ID + "," + name + "," + name + "," + name + ")";
        no = DB.executeUpdate(sql, m_trx.getTrxName());
        if (no != 1) {
            String err = "Org User NOT inserted - " + AD_User_U_Name;
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        m_info.append(Msg.translate(m_lang, "AD_User_ID")).append("=").append(AD_User_U_Name).append("/").append(AD_User_U_Name).append("\n");
        sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)" + " VALUES (" + m_stdValues + "," + AD_User_ID + "," + admin.getAD_Role_ID() + ")";
        no = DB.executeUpdate(sql, m_trx.getTrxName());
        if (no != 1) {
            log.log(Level.SEVERE, "UserRole ClientUser+Admin NOT inserted");
        }
        sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)" + " VALUES (" + m_stdValues + "," + AD_User_ID + "," + user.getAD_Role_ID() + ")";
        no = DB.executeUpdate(sql, m_trx.getTrxName());
        if (no != 1) {
            log.log(Level.SEVERE, "UserRole ClientUser+User NOT inserted");
        }
        sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)" + " VALUES (" + m_stdValues + "," + AD_User_U_ID + "," + user.getAD_Role_ID() + ")";
        no = DB.executeUpdate(sql, m_trx.getTrxName());
        if (no != 1) {
            log.log(Level.SEVERE, "UserRole OrgUser+Org NOT inserted");
        }
        MAcctProcessor ap = new MAcctProcessor(m_client, AD_User_ID);
        ap.save();
        MRequestProcessor rp = new MRequestProcessor(m_client, AD_User_ID);
        rp.save();
        log.info("fini");
        return true;
    }
