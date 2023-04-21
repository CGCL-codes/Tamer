    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Syntax: java -jar Feed2ereader.jar " + "opmlFile eReaderPath html2mobiExecutable [proxy] [proxyPort]");
            System.err.println("Examples:\n" + "java -jar Feed2ereader.jar " + "opml.xml F:/eBooks .\\mobiperl\\html2mobi.exe\n" + "java -jar Feed2ereader.jar " + "google-reader-subscriptions.xml /media/Cybook/eBooks /usr/local/bin/html2mobi myproxy.com 81");
            System.exit(0);
        }
        String opmlFile = args[0];
        String path = args[1];
        html2mobi = args[2];
        if (args.length >= 5) {
            String proxy = args[3];
            String proxyPort = args[4];
            System.getProperties().put("proxySet", "true");
            System.getProperties().put("proxyHost", proxy);
            System.getProperties().put("proxyPort", proxyPort);
        }
        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance();
        String today = df.format(now);
        OpmlReader opml = new OpmlReader(opmlFile);
        for (String[] feed : opml.getFeeds()) {
            HttpFeed feedChannel = new HttpFeed(feed[0], feed[1]);
            String file = feedChannel.getTitle();
            HtmlWriter htmlWriter = new HtmlWriter(feedChannel.getChannel(), file, Feed2ereader.tempFolder, feed[2], today);
            if (toMobi(htmlWriter.getFile(), path + "/" + file + ".mobi")) {
                try {
                    File thumbnail = new File(path + "/" + file + "_6090.t2b");
                    if (thumbnail.exists()) {
                        thumbnail.delete();
                    }
                } catch (SecurityException e) {
                    System.out.println("Thumbnail could not be deleted");
                }
                System.out.println("=> " + file + " conversion succeeded!");
            } else {
                System.out.println("=> " + file + " conversion failed!");
            }
        }
        System.out.println("Synchronization finished!");
    }
