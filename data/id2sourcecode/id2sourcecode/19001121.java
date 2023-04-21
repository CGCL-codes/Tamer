    public void Message(ClientConnector cc, Map<String, String> attributes) throws Exception {
        log.debug("Starting HTTP GET Message");
        String DESTNODE = attributes.get("dest_name");
        int DESTPORT;
        int DESTPORTDEFAULT = 80;
        String destPortString = attributes.get("dest_port");
        if (destPortString != null) {
            try {
                DESTPORT = Integer.parseInt(destPortString);
            } catch (Exception e) {
                DESTPORT = DESTPORTDEFAULT;
                log.warn("Destination Port \"" + destPortString + "\" was not valid. Using default value");
            }
        } else {
            List DESTPORTLIST = axt.db.GeneralDAO.getNodeValue(DESTNODE, "msg_get_port");
            if ((DESTPORTLIST.size() > 0)) {
                try {
                    DESTPORT = Integer.parseInt(DESTPORTLIST.get(0).toString());
                } catch (Exception e) {
                    DESTPORT = DESTPORTDEFAULT;
                    log.warn("DB Destination Port \"" + destPortString + "\" was not valid. Using default value");
                }
            } else {
                DESTPORT = DESTPORTDEFAULT;
            }
        }
        String URLPATH = attributes.get("dest_get_form");
        if ((URLPATH == null) || (URLPATH.equals(""))) {
            List DESTURLPATH = axt.db.GeneralDAO.getNodeValue(DESTNODE, "msg_get_form");
            if ((DESTURLPATH.size() > 0)) {
                URLPATH = DESTURLPATH.get(0).toString();
            } else {
                URLPATH = "message.cgi";
            }
        }
        String getURL = "msg_node=" + URLEncoder.encode(attributes.get("src_name"), "UTF-8");
        Iterator attrIterator = attributes.keySet().iterator();
        while (attrIterator.hasNext()) {
            String keyName = (String) attrIterator.next();
            if (keyName.toLowerCase().startsWith("msg_")) {
                if (!getURL.equals("")) {
                    getURL += "&";
                }
                getURL += URLEncoder.encode(keyName, "UTF-8") + "=" + URLEncoder.encode(attributes.get(keyName), "UTF-8");
            }
        }
        String fullURL = "http://" + DESTNODE + ":" + DESTPORT + "/" + URLPATH + "?" + getURL;
        URL url = new URL(fullURL);
        log.debug("Full GET URL is: " + fullURL);
        log.debug("Connecting");
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String logResponse = "-----------------\nHTTP Get Response:\n-----------------\n";
        while ((line = rd.readLine()) != null) {
            log.debug("Received: " + line);
            logResponse += line + "\n";
        }
        log.debug("Closing");
        rd.close();
        logResponse += "-----------------\n";
        cc.log(logResponse);
    }
