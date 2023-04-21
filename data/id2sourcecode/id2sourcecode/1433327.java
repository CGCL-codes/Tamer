    public Subscription getSubscription() {
        TicketRequestInterface trs;
        Environment env;
        String realm;
        String ipname;
        long endtime;
        boolean allowProxy;
        String[] readFileAccess;
        String[] writeFileAccess;
        String[] deleteFileAccess;
        int count;
        String delegee;
        boolean isSubscription;
        ByteArrayOutputStream bos;
        Map hosts;
        Iterator it;
        String key;
        URL val;
        Vicinity vicinity;
        byte[] token;
        boolean isUser;
        KeyMaster keymaster;
        X509Certificate cert;
        long duration;
        String durString;
        env = Environment.getEnvironment();
        trs = (TicketRequestInterface) env.lookup(WhatIs.stringValue("TICKET_REQUEST"));
        if (trs == null) {
            System.out.println("Ticket request service not found!");
            return null;
        }
        realm = cbr2.util.CBRConfig.getProperty("cbr.tgs.realm");
        try {
            keymaster = (KeyMaster) env.lookup(WhatIs.stringValue("KEYMASTER"));
            cert = ((X509Certificate) keymaster.getCertificate(KeyMaster.CRYPT_KEY));
            ipname = cert.getSubjectDN().getName();
        } catch (CertificateException ex) {
            System.out.println("Own signing certificate not found");
            ipname = null;
        }
        durString = CBRConfig.getProperty("cbr.subscription.duration");
        durString = durString.trim();
        try {
            duration = Long.parseLong(durString);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid ticket duration string '" + durString + "': " + ex.getMessage() + "\nUsing default 600000.");
            duration = 600000;
        }
        endtime = System.currentTimeMillis() + duration;
        allowProxy = false;
        readFileAccess = new String[] { "abo" };
        deleteFileAccess = null;
        writeFileAccess = null;
        count = -1;
        isUser = true;
        delegee = null;
        isSubscription = false;
        bos = new ByteArrayOutputStream();
        try {
            trs.getServiceTicket(realm, ipname, endtime, allowProxy, readFileAccess, writeFileAccess, deleteFileAccess, count, isUser, delegee, isSubscription, bos);
            token = bos.toByteArray();
            bos.close();
            log(getUserCName() + " kaufte ein Abonnement fuer " + CBRConfig.getProperty("cbr.subscription.price") + " Euro am " + new Date());
            return new Subscription(token, new URL(localURL_), new Date(endtime));
        } catch (TicketRequestException ex1) {
            System.out.println("No subscription received: " + ex1.getMessage());
        } catch (MalformedURLException ex3) {
            System.out.println("Malformed URL: " + localURL_);
        } catch (IOException ex2) {
            System.out.println("IOException: " + ex2.getMessage());
        }
        return null;
    }
