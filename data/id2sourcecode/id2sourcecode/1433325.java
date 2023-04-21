    private byte[] retrieveToken(String image, String url) {
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
        long duration;
        String durString;
        KeyMaster keymaster;
        X509Certificate cert;
        env = Environment.getEnvironment();
        trs = (TicketRequestInterface) env.lookup(WhatIs.stringValue("TICKET_REQUEST"));
        if (trs == null) {
            System.out.println("Ticket request service not found!");
            return null;
        }
        realm = cbr2.util.CBRConfig.getProperty("cbr.tgs.realm");
        try {
            vicinity = (Vicinity) Environment.getEnvironment().lookup(WhatIs.stringValue("VICINITY"));
        } catch (Exception e) {
            System.err.println("Fatal error: Could not access vicinity at " + WhatIs.stringValue("VICINITY") + ":");
            return null;
        }
        hosts = vicinity.getContactTable();
        it = hosts.keySet().iterator();
        ipname = null;
        while (it.hasNext()) {
            key = (String) it.next();
            val = (URL) hosts.get(key);
            if (val.equals(url)) {
                ipname = key;
                break;
            }
        }
        if (ipname == null) {
            try {
                keymaster = (KeyMaster) env.lookup(WhatIs.stringValue("KEYMASTER"));
                cert = ((X509Certificate) keymaster.getCertificate(KeyMaster.CRYPT_KEY));
                ipname = cert.getSubjectDN().getName();
            } catch (CertificateException ex) {
                System.out.println("Own signing certificate not found");
                ipname = null;
            }
        }
        durString = CBRConfig.getProperty("cbr.subscription.duration");
        durString = durString.trim();
        try {
            duration = Long.parseLong(durString);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid ticket duration string '" + durString + "': " + ex.getMessage() + "\nUsing default 300000.");
            duration = 300000;
        }
        endtime = System.currentTimeMillis() + duration;
        allowProxy = false;
        readFileAccess = new String[] { image };
        deleteFileAccess = null;
        writeFileAccess = null;
        count = 1;
        delegee = null;
        isSubscription = false;
        bos = new ByteArrayOutputStream();
        try {
            trs.getProxyTicketDirect(realm, ipname, endtime, allowProxy, readFileAccess, writeFileAccess, deleteFileAccess, count, delegee, isSubscription, bos);
            token = bos.toByteArray();
        } catch (TicketRequestException ex1) {
            System.out.println("No ticket received: " + ex1.getMessage());
            return null;
        }
        return token;
    }
