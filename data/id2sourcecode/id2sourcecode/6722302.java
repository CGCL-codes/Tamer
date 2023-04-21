    public static void parse(Message message, String outputDir) {
        try {
            String subject = message.getSubject();
            if (log.isDebugEnabled()) {
                log.debug("subject = " + subject);
            }
            if (subject != null && subject.indexOf("AEL") >= 0) {
                if (log.isDebugEnabled()) {
                    log.debug("found a data message");
                }
                String contentType = message.getContentType();
                if (log.isDebugEnabled()) {
                    log.debug("contentType = " + contentType);
                }
                if (contentType.startsWith("text/plain") || contentType.startsWith("text/html") || contentType.startsWith("multipart/alternative")) {
                    InputStream is = message.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String thisLine = reader.readLine();
                    while (thisLine != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("line = " + thisLine);
                        }
                        if (thisLine.indexOf("http") > 0) {
                            String urlName = thisLine.substring(thisLine.indexOf("http"), thisLine.length());
                            log.debug("filename = " + urlName);
                            URL url = new URL(urlName.trim());
                            String dataname = subject.substring(subject.indexOf("AEL"), subject.indexOf("_AEL"));
                            File file = new File(outputDir + dataname + ".zip");
                            InputStream downloadInputStream = url.openStream();
                            if (log.isDebugEnabled()) {
                                log.debug("downloading " + url.getPath());
                            }
                            toFile(downloadInputStream, file);
                        }
                        thisLine = reader.readLine();
                    }
                } else {
                    log.error("I don't recognise the contentType '" + contentType + "'");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.error("this isn't a data message");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
