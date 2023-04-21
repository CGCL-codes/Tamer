    public String[] runCommands(long serviceId, int port, List commandsToRun) throws Exception {
        String[] commandResults = new String[commandsToRun.size()];
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
                for (int i = 0; i < commandsToRun.size(); i++) {
                    client.sendCommand((String) commandsToRun.get(i));
                    commandResults[i] = client.getReplyString();
                }
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
        return commandResults;
    }
