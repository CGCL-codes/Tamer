    public String runCommand(long serviceId, int port, String commandToRun) throws Exception {
        SMTPClient client = new SMTPClient();
        client.setConnectTimeout(timeout);
        Session session = org.zkoss.zkplus.hibernate.HibernateUtil.getSessionFactory().getCurrentSession();
        ServiceFacade serviceFacade = new ServiceFacade();
        Service service = (Service) session.load(Service.class, serviceId);
        String hostname = service.getDevice().getName();
        Login login = service.getLogin();
        String username = login.getArg1();
        String password = login.getArg2();
        try {
            client.connect(hostname, port);
            int result = client.getReplyCode();
            if (!SMTPReply.isPositiveCompletion(result)) {
                service.setStatus("down");
                serviceFacade.saveService(service);
                omnilog.error("Couldn't connect to SMTP Service");
            } else {
                client.login();
                client.sendCommand(commandToRun);
                commandResult = client.getReplyString();
                service.setStatus("up");
                serviceFacade.saveService(service);
            }
        } catch (Exception e) {
            omnilog.error("Error trying to connect to SMTP server at " + hostname + " on port " + port);
            service.setStatus("down");
            serviceFacade.saveService(service);
            throw e;
        } finally {
            client.logout();
            client.disconnect();
        }
        return commandResult;
    }
