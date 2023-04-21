    public String install() {
        try {
            webinfRoot = getAppRoot() + "WEB-INF" + File.separator;
            installTempConfPath = webinfRoot + "install" + File.separator + "conf" + File.separator;
            destPropConfRoot = webinfRoot + "classes" + File.separator;
            String appTempFile = installTempConfPath + APP_PROP_FILE;
            String destAppFile = destPropConfRoot + APP_PROP_FILE;
            appProp.setAuthDomain(getServerQName());
            Installer.writeAppConfig(appProp, appTempFile, destAppFile);
            String jdbcTempFile = installTempConfPath + JDBC_PROP_FILE;
            String destJdbcFile = destPropConfRoot + JDBC_PROP_FILE;
            Installer.writeDbConfig(jdbcTempFile, jdbcProp.getDbType(), jdbcProp.getDbHost(), jdbcProp.getDbPort(), jdbcProp.getDbName(), jdbcProp.getDbUserName(), jdbcProp.getDbPassword(), destJdbcFile);
            String mailTempFile = installTempConfPath + MAIL_PROP_FILE;
            String destMailFile = destPropConfRoot + MAIL_PROP_FILE;
            Installer.writeMailConfig(mailTempFile, mailProp.getMailServer(), mailProp.getMailServerPort(), mailProp.isAuthenticated(), mailProp.isTlsEnabled(), mailProp.getUserName(), mailProp.getPassword(), destMailFile);
            String ldapTempFile = installTempConfPath + LDAP_PROP_FILE;
            String destLdapFile = destPropConfRoot + LDAP_PROP_FILE;
            Installer.writeLdapConfig(ldapProp, ldapTempFile, destLdapFile);
            String springConfFile = installTempConfPath + SPRING_CONF_FILE;
            String destSpringConfFile = destPropConfRoot + SPRING_CONF_FILE;
            String strutsFile = installTempConfPath + STRUTS_FILE;
            String destStrutsFile = destPropConfRoot + STRUTS_FILE;
            String webxmlConfFile = installTempConfPath + WEB_XML_FILE;
            String destWebxmlFile = webinfRoot + WEB_XML_FILE;
            FileUtils.copyFile(new File(springConfFile), new File(destSpringConfFile));
            FileUtils.copyFile(new File(strutsFile), new File(destStrutsFile));
            FileUtils.copyFile(new File(webxmlConfFile), new File(destWebxmlFile));
            System.out.println("Finished");
        } catch (Exception e) {
            addActionError(e.getMessage());
            setDefaultMaps();
            logger.error(e);
            return INPUT;
        }
        return SUCCESS;
    }
