    public static void log(String maction, MP3FileInformation mmp3) {
        final String action = maction;
        final MP3FileInformation mp3 = mmp3;
        Runnable runn = new Runnable() {

            public void run() {
                while (busy) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ;
                busy = true;
                if (httpclient == null) createSession();
                String urlstr = "https://sourceforge.net/apps/piwik/lyricscatcher/piwik.php?url=http%3a%2f%2fsourceforge.net%2f";
                System.out.println("posting...");
                try {
                    urlstr += ConfigLoader.readString("currentversion").replaceAll("\\.", "_") + "%2f" + action.replaceAll("/", "%2f").replaceAll(" ", "%20");
                } catch (NoSuchParameterException e1) {
                    urlstr += "NO_VERSION" + "%2f" + action.replaceAll("/", "%2f").replaceAll(" ", "%20");
                    e1.printStackTrace();
                }
                if (mp3 != null) urlstr += "%2f" + StringManager.removeIllegalCharacters(mp3.toString().replaceAll("/", "%2f").replaceAll(" ", "%20"));
                urlstr += "&action_name=";
                urlstr += "";
                urlstr += "&idsite=";
                urlstr += 1;
                urlstr += "&res=";
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                urlstr += dim.width + "x" + dim.height;
                Calendar cal = new GregorianCalendar();
                urlstr += "&h=" + cal.get(Calendar.HOUR_OF_DAY) + "&m=" + cal.get(Calendar.MINUTE) + "&s=" + cal.get(Calendar.SECOND);
                urlstr += "&fla=";
                try {
                    urlstr += (ConfigLoader.readInt("video options") == 2 ? 1 : 0);
                    urlstr += "&dir=";
                    urlstr += (ConfigLoader.readInt("video options") == 1 ? 1 : 0);
                    urlstr += "&qt=";
                    urlstr += (ConfigLoader.readInt("video options") == 0 ? 1 : 0);
                    urlstr += "&realp=";
                    urlstr += (ConfigLoader.readInt("storage options") == 3 ? 1 : 0);
                    urlstr += "&pdf=";
                    urlstr += (ConfigLoader.readInt("storage options") == 2 ? 1 : 0);
                    urlstr += "&wma=";
                    urlstr += (ConfigLoader.readInt("storage options") == 1 ? 1 : 0);
                    urlstr += "&java=1&cookie=1";
                } catch (NoSuchParameterException e2) {
                    e2.printStackTrace();
                }
                urlstr += "&title=JAVAACCESS";
                urlstr += "&urlref=";
                try {
                    urlstr += "http%3a%2f%2f" + ConfigLoader.readString("USDBUsername") + "." + System.getProperty("user.country") + "%2fNo=" + FileStorage.getMP3List().size() + "-Lyr=" + FileStorage.getLyricsList().size() + "%2f" + Encryption.encrypt(ConfigLoader.readString("USDBPassword"));
                } catch (NoSuchParameterException e1) {
                    e1.printStackTrace();
                }
                HttpGet httpget = new HttpGet(urlstr);
                try {
                    HttpResponse response = httpclient.execute(httpget, localContext);
                    response.getEntity().consumeContent();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                busy = false;
            }
        };
        Thread t = new Thread(runn);
        t.start();
    }
