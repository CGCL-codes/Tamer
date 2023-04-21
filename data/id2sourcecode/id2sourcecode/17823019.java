    public int tryToConnect(long serviceId, int port) throws IOException, Exception {
        SMTPClient client = new SMTPClient();
        client.setConnectTimeout(timeout);
        ServiceFacade serviceFacade = new ServiceFacade();
        Session session = org.zkoss.zkplus.hibernate.HibernateUtil.getSessionFactory().getCurrentSession();
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
                service.setStatus("up");
                serviceFacade.saveService(service);
                omnilog.debug("Connected to SMTP service on " + hostname);
            }
        } catch (Exception e) {
            omnilog.error("Error trying to connect to SMTP server at " + hostname + " on port " + port);
            connectionResult = 1;
            service.setStatus("down");
            serviceFacade.saveService(service);
            throw e;
        } finally {
            client.logout();
            client.disconnect();
        }
        return connectionResult;
    }
