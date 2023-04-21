    private Ftpuser register(PrintWriter writer, String username, String password, String email) throws javax.naming.NamingException, java.sql.SQLException, ResourceNotFoundException, Exception {
        Ftpuser user = null;
        if (findDeployedApp(util.toContext(username)) != null) {
            writer.println("FAIL - User exists");
            return null;
        }
        try {
            new InternetAddress(email).validate();
        } catch (javax.mail.internet.AddressException aex) {
            writer.println("FAIL - " + aex.getMessage());
            return null;
        }
        String userHome = BASE + File.separatorChar + username;
        if (new File(userHome).exists()) {
            writer.println("FAIL - User home exists");
            return null;
        }
        if ((DAOFactory.getUserDAO().usernameExists(username))) {
            writer.println("FAIL - User already registered");
            return null;
        }
        String contextTmpFolder = getServletConfig().getServletContext().getRealPath("/WEB-INF/config");
        String contextFile = contextTmpFolder + File.separatorChar + util.toContext(username) + ".xml";
        if (!new File(contextTmpFolder).exists() && !new File(contextTmpFolder).mkdir()) {
            writer.println("FAIL - Unable to create temporary context information");
            return null;
        }
        try {
            DAOFactory.getFSDAO().savectx(username, password, contextFile, BASE);
            DAOFactory.getUserDAO().createUser(username, email, password);
            DAOFactory.getDBDAO().createNewSchema(username, password);
            DAOFactory.getFSDAO().createHome(BASE, username);
            super.deploy(writer, contextFile, util.toContext(username), null, false);
            user = logon(writer, username, password);
            MailHelper.sendConfirmationMail(username, email, false);
        } catch (Exception e) {
            writer.println("FAIL - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return user;
    }
