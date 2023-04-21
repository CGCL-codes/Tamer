    public String exportScanPost(String jsonString, Ticket credentials) throws TopicSpacesException {
        smp.logDebug("WS_CohereExporter.exportScanPost- " + jsonString);
        String result = "";
        String username = credentials.getOwner();
        String password = credentials.getOldPassword();
        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
        smp.logDebug("WS_CohereExporter.exportScanPost-1 " + jsonObject.toString());
        String title = "", description = "", scanURL = "", resourceURL = "";
        try {
            HttpURLConnection connection;
            String loginurl = COHERE_QUERY + "?format=xml&method=login&username=" + username + "&password=" + password;
            URL url = new URL(loginurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();
            int rc = connection.getResponseCode();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer body = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                body.append(line);
                body.append('\r');
            }
            rd.close();
            smp.logDebug("WS_CohereExporter login " + rc + " " + body.toString());
            String sessionId = null;
            Map<String, List<String>> headers = connection.getHeaderFields();
            smp.logDebug("HEADERS " + headers);
            List<String> cookies = headers.get("Set-Cookie");
            smp.logDebug("CCCC " + cookies);
            String ck = null;
            String user = null;
            if (cookies != null) {
                Iterator<String> itr = cookies.iterator();
                String cky;
                while (itr.hasNext()) {
                    cky = itr.next().split(";", 2)[0];
                    smp.logDebug("COOKIE " + cky);
                    if (cky.startsWith("user") && !(cky.indexOf("del") > 0)) {
                        user = cky;
                    }
                    if (cky.startsWith("Cohere")) {
                        ck = cky;
                        break;
                    }
                }
            }
            if (user != null) user = user.substring("user-".length());
            if (ck != null) {
                ck = ck.substring("Cohere=".length());
            }
            connection.disconnect();
            sessionId = ck;
            smp.logDebug("COOKIES " + sessionId + " user " + user);
            String validate = COHERE_QUERY + "?format=xml&method=validatesession&userid=" + user;
            smp.logDebug("VALIDATE " + validate);
            url = new URL(validate);
            connection = (HttpURLConnection) url.openConnection();
            String myCookie = "userid=" + user;
            connection.setRequestProperty("Cookie", myCookie);
            myCookie = "Cohere=" + sessionId;
            connection.setRequestProperty("Cookie", myCookie);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            rc = connection.getResponseCode();
            is = connection.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is));
            body = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                body.append(line);
                body.append('\r');
            }
            rd.close();
            connection.disconnect();
            smp.logDebug("WS_CohereExporter validate " + rc + " " + body.toString());
            JSONObject node = (JSONObject) jsonObject.get("node");
            title = (String) node.get("title");
            description = (String) node.get("description");
            scanURL = (String) node.get("node_url");
            resourceURL = (String) node.get("field_url");
            smp.logDebug("WS_CohereExporter.exportScanPost-1 " + title + " " + scanURL + " " + resourceURL + " " + description);
            String nodeQuery = COHERE_QUERY + makeNodeQuery(title, description);
            smp.logDebug("WS_CohereExporter.exportScanPost-2 " + nodeQuery);
            String nodeid = _addNode(nodeQuery, user, sessionId);
            smp.logDebug("WS_CohereExporter.exportScanPost-3 " + nodeid);
            if (nodeid != null) {
                nodeQuery = COHERE_QUERY + makeUrlQuery(title, scanURL, "Scan URL");
                String urlid = _addNode(nodeQuery, user, sessionId);
                smp.logDebug("WS_CohereExporter.exportScanPost-4 " + urlid);
                if (urlid != null) {
                    nodeQuery = COHERE_QUERY + makeAddUrlQuery(urlid, nodeid);
                    _addNode(nodeQuery, user, sessionId);
                    nodeQuery = COHERE_QUERY + makeUrlQuery(title, resourceURL, "Node URL");
                    urlid = _addNode(nodeQuery, user, sessionId);
                    smp.logDebug("WS_CohereExporter.exportScanPost-5 " + urlid);
                    if (urlid != null) {
                        nodeQuery = COHERE_QUERY + makeAddUrlQuery(urlid, nodeid);
                        _addNode(nodeQuery, user, sessionId);
                    }
                }
            }
        } catch (Exception e) {
            smp.logError(e.getMessage(), e);
            result = e.getMessage();
        }
        return result;
    }
