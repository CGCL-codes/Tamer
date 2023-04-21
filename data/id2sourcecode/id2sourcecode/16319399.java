    public int tryToConnect(long serviceId, int port) throws IOException, Exception {
        POP3Client client = new POP3Client();
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
            if (!username.trim().equals("") && !password.trim().equals("")) {
                if (client.login(username, password)) {
                    connectionResult = 0;
                } else {
                    connectionResult = 1;
                    service.setStatus("down");
                    serviceFacade.saveService(service);
                }
            } else {
                connectionResult = 0;
                service.setStatus("up");
                serviceFacade.saveService(service);
            }
        } catch (Exception e) {
            omnilog.error("Error trying to connect to POP3 server at " + hostname + " on port " + port);
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
