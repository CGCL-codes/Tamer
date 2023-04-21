    public boolean retrieveCSVList() throws IOException, MalformedURLException {
        synchronized (this) {
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            boolean newEntries = false;
            Debug.info("Opening HTML Callerlist page");
            String postdata = firmware.getAccessMethod() + POSTDATA_LIST.replaceAll("\\$LANG", firmware.getLanguage()) + URLEncoder.encode(box_password, "ISO-8859-1");
            String urlstr = "http://" + box_address + ":" + box_port + "/cgi-bin/webcm";
            Debug.debug("Urlstr: " + urlstr);
            Debug.debug("Postdata: " + postdata);
            try {
                url = new URL(urlstr);
            } catch (MalformedURLException e) {
                Debug.error("URL invalid: " + urlstr);
                throw new MalformedURLException("URL invalid: " + urlstr);
            }
            if (url != null) {
                urlConn = url.openConnection();
                urlConn.setConnectTimeout(5000);
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                if (postdata != null) {
                    urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    printout = new DataOutputStream(urlConn.getOutputStream());
                    printout.writeBytes(postdata);
                    printout.flush();
                    printout.close();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while (reader.skip(100000) > 0) {
                    }
                    reader.close();
                    urlConn.getInputStream().close();
                } catch (IOException e1) {
                    throw new IOException("Network unavailable");
                }
            }
            Debug.info("Retrieving the CSV list from the box");
            urlstr = "http://" + box_address + ":" + box_port + "/cgi-bin/webcm";
            try {
                url = new URL(urlstr);
            } catch (MalformedURLException e) {
                Debug.error("URL invalid: " + urlstr);
                throw new MalformedURLException("URL invalid: " + urlstr);
            }
            if (!JFritz.isShutdownInvoked() && url != null) {
                urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                printout = new DataOutputStream(urlConn.getOutputStream());
                if (firmware.getLanguage().equals("de")) {
                    printout.writeBytes(POSTDATA_FETCH_CALLERLIST.replaceAll("\\$LANG", firmware.getLanguage()).replaceAll("\\$CSV_FILE", CSV_FILE_DE));
                } else if (firmware.getLanguage().equals("en")) {
                    printout.writeBytes(POSTDATA_FETCH_CALLERLIST.replaceAll("\\$LANG", firmware.getLanguage()).replaceAll("\\$CSV_FILE", CSV_FILE_EN));
                }
                printout.flush();
                printout.close();
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    Debug.info("Received response, begin processing call list");
                    newEntries = JFritz.getCallerList().importFromCSVFile(reader);
                    Debug.info("Finished processing response");
                    reader.close();
                    urlConn.getInputStream().close();
                } catch (IOException e1) {
                    throw new IOException("Network unavailable");
                }
            }
            return newEntries;
        }
    }
