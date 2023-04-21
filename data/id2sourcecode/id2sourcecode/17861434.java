    protected String subscriptionContentsAsString(SubscriptionType sub) {
        String contentsDir = Props.instance().getProperty("subscriber.subscription_contents_dir");
        String subId = sub.getSubscriptionId().toString();
        String contentsDirPath = contentsDir + File.separator + subId;
        File f = new File(contentsDirPath + File.separator + subId);
        if (f.exists() && !f.isDirectory()) {
            StringWriter out = new StringWriter();
            FileReader in = null;
            try {
                in = new FileReader(f);
                char[] buf = new char[BUFSIZ];
                int len;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            } catch (IOException ioe) {
                Logger.instance().log(Logger.ERROR, loggerPrefix, "TwICESubscriber.subscriptionContentsAsString", ioe);
            } finally {
                try {
                    if (in != null) in.close();
                } catch (IOException ioe2) {
                    Logger.instance().log(Logger.ERROR, loggerPrefix, "TwICESubscriber.subscriptionContentsAsString", ioe2);
                }
            }
            return out.toString();
        } else {
            f = new File(contentsDirPath);
            if (f.exists() && f.isDirectory()) {
                StringBuffer buf = new StringBuffer();
                buildDirectoryHierarchy(buf, "", f);
                return buf.toString();
            } else {
                return "No subscription contents found.";
            }
        }
    }
