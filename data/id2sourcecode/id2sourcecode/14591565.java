    public boolean createClient(String clientName, String orgName, String userClient, String userOrg) {
        log.info(clientName);
        m_trx.start();
        m_info = new StringBuffer();
        String name = null;
        String sql = null;
        int no = 0;
        name = clientName;
        if (name == null || name.length() == 0) name = "newClient";
        m_clientName = name;
        m_client = new MClient(m_ctx, 0, true, m_trx.getTrxName());
        m_client.setValue(m_clientName);
        m_client.setName(m_clientName);
        if (!m_client.save()) {
            String err = "Client NOT created";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        int AD_Client_ID = m_client.getAD_Client_ID();
        Env.setContext(m_ctx, m_WindowNo, "AD_Client_ID", AD_Client_ID);
        Env.setContext(m_ctx, "#AD_Client_ID", AD_Client_ID);
        m_stdValues = String.valueOf(AD_Client_ID) + ",0,'Y',SysDate,0,SysDate,0";
        m_info.append(Msg.translate(m_lang, "AD_Client_ID")).append("=").append(name).append("\n");
        if (!MSequence.checkClientSequences(m_ctx, AD_Client_ID, m_trx.getTrxName())) {
            String err = "Sequences NOT created";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        if (!m_client.setupClientInfo(m_lang)) {
            String err = "Client Info NOT created";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        m_AD_Tree_Account_ID = m_client.getSetup_AD_Tree_Account_ID();
        name = orgName;
        if (name == null || name.length() == 0) name = "newOrg";
        m_org = new MOrg(m_client, name);
        if (!m_org.save()) {
            String err = "Organization NOT created";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        Env.setContext(m_ctx, m_WindowNo, "AD_Org_ID", getAD_Org_ID());
        Env.setContext(m_ctx, "#AD_Org_ID", getAD_Org_ID());
        m_stdValuesOrg = AD_Client_ID + "," + getAD_Org_ID() + ",'Y',SysDate,0,SysDate,0";
        m_info.append(Msg.translate(m_lang, "AD_Org_ID")).append("=").append(name).append("\n");
        name = m_clientName + " Admin";
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
        if (!adminClientAccess.save()) log.log(Level.SEVERE, "Admin Role_OrgAccess 0 NOT created");
        MRoleOrgAccess adminOrgAccess = new MRoleOrgAccess(admin, m_org.getAD_Org_ID());
        if (!adminOrgAccess.save()) log.log(Level.SEVERE, "Admin Role_OrgAccess NOT created");
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
        if (!userOrgAccess.save()) log.log(Level.SEVERE, "User Role_OrgAccess NOT created");
        m_info.append(Msg.translate(m_lang, "AD_Role_ID")).append("=").append(name).append("\n");
        name = userClient;
        if (name == null || name.length() == 0) name = m_clientName + "Client";
        AD_User_ID = getNextID(AD_Client_ID, "AD_User");
        AD_User_Name = name;
        name = DB.TO_STRING(name);
        sql = "INSERT INTO AD_User(" + m_stdColumns + ",AD_User_ID," + "Name,Description,Password)" + " VALUES (" + m_stdValues + "," + AD_User_ID + "," + name + "," + name + "," + name + ")";
        no = DB.executeUpdate(sql, m_trx.getTrxName());
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
        if (name == null || name.length() == 0) name = m_clientName + "Org";
        AD_User_U_ID = getNextID(AD_Client_ID, "AD_User");
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
        if (no != 1) log.log(Level.SEVERE, "UserRole ClientUser+Admin NOT inserted");
        sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)" + " VALUES (" + m_stdValues + "," + AD_User_ID + "," + user.getAD_Role_ID() + ")";
        no = DB.executeUpdate(sql, m_trx.getTrxName());
        if (no != 1) log.log(Level.SEVERE, "UserRole ClientUser+User NOT inserted");
        sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)" + " VALUES (" + m_stdValues + "," + AD_User_U_ID + "," + user.getAD_Role_ID() + ")";
        no = DB.executeUpdate(sql, m_trx.getTrxName());
        if (no != 1) log.log(Level.SEVERE, "UserRole OrgUser+Org NOT inserted");
        MAcctProcessor ap = new MAcctProcessor(m_client, AD_User_ID);
        ap.save();
        MRequestProcessor rp = new MRequestProcessor(m_client, AD_User_ID);
        rp.save();
        log.info("fini");
        return true;
    }
