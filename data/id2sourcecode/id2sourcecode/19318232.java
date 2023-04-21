    private void SearchPersons(int start) {
        transferFromIdAut ldap = transferFromIdAut.getInstance();
        ch.unibe.a3ubAdmin.control.DatabaseManager man = new ch.unibe.a3ubAdmin.control.DatabaseManager();
        for (int i = start; i < 150000; i++) {
            if (i % 500 == 0) {
                Date d = new Date();
            }
            String userDN = "";
            try {
                userDN = ldap.getUserDN("" + i);
            } catch (LdapException e) {
            }
            if (!"".equals(userDN)) {
                Map map = null;
                try {
                    map = ldap.getPersonsValues(userDN);
                } catch (LdapException e) {
                }
                if (map != null && map.keySet().size() > 3) {
                    String[] uidNumberArr = (String[]) map.get("uidnumber");
                    String[] uidArr = (String[]) map.get("uid");
                    String[] cnArr = (String[]) map.get("cn");
                    String[] gidArr = (String[]) map.get("gidnumber");
                    ch.unibe.a3ubAdmin.model.ViewUser user = null;
                    try {
                        user = man.loadViewUser(uidArr[0]);
                    } catch (HibernateException e) {
                        if (log.isWarnEnabled()) {
                            log.warn("transferFromIdAut: ", e);
                        }
                    } catch (NumberFormatException e) {
                        if (log.isWarnEnabled()) {
                            log.warn("transferFromIdAut: ", e);
                        }
                    } catch (ValidationException e) {
                        if (log.isWarnEnabled()) {
                            log.warn("transferFromIdAut: ", e);
                        }
                    }
                    String snArr[] = { "-" };
                    if (user.getUidnumber() != null) {
                    } else {
                        writeSqlFile("INSERT INTO ub_a3ubadmin2_dbm.vw_user VALUES(" + uidNumberArr[0] + ",'" + uidArr[0] + "','" + snArr[0] + "','" + cnArr[0] + "','" + gidArr[0] + "');");
                    }
                    writeGroupfile(gidArr[0]);
                }
            }
        }
    }
