    private void queryEDGIApplications(final String repositoryURLPar, List<Item.Edgi.Job> jobList) throws MalformedURLException, IOException {
        final boolean POST = true;
        boolean warning;
        final String GET_APPS_SERVLET = "mce/getapps";
        final String GET_APP_RESP_REGEXP = "^\\d+ .+";
        final String GET_IMPS_SERVLET = "mce/getimps";
        final String GET_IMPS_RESP_REGEXP = "^\\d+ \\d+";
        final String GET_IMP_ATTRS_SERVLET = "mce/getimpattr";
        final String GET_IMP_ATTRS_RESP_REGEXP = "^\\d+ .+=.*";
        final String GET_IMP_FILE_URL_SERVLET = "mce/getfileurls";
        final String GET_IMP_FILE_URL_RESP_REGEXP = "^\\d+ .+";
        URL url;
        URLConnection conn;
        BufferedReader rd;
        String line;
        Hashtable<String, String> apps = new Hashtable<String, String>();
        Hashtable<String, String> imps = new Hashtable<String, String>();
        Hashtable<String, Vector<String>> fileUrls = new Hashtable<String, Vector<String>>();
        Hashtable<String, Vector<String>> voIds = new Hashtable<String, Vector<String>>();
        Hashtable<String, String> voNames = new Hashtable<String, String>();
        Hashtable<String, Vector<String>> ceUrls = new Hashtable<String, Vector<String>>();
        Hashtable<String, Vector<String>> appFileUrls = new Hashtable<String, Vector<String>>();
        String repositoryURL = repositoryURLPar.endsWith("/") ? repositoryURLPar : repositoryURLPar + "/";
        if (DEBUG) System.out.println("Getting APPLICATIONS...");
        warning = false;
        url = new java.net.URL(repositoryURL + GET_APPS_SERVLET);
        conn = url.openConnection();
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = rd.readLine()) != null) {
            if (!line.matches(GET_APP_RESP_REGEXP)) {
                if (!warning) {
                    Base.writeLogg(THIS_MIDDLEWARE, new LB(THIS_MIDDLEWARE + " plugin: application repository line has invalid syntax: " + line));
                    warning = true;
                }
                continue;
            }
            String appId = line.substring(0, line.indexOf(' '));
            String appName = line.substring(line.indexOf(' ') + 1).trim();
            apps.put(appId, appName);
            if (DEBUG) System.out.println("\t" + appName + " " + appId + " ");
        }
        rd.close();
        if (apps.size() == 0) {
            Base.writeLogg(THIS_MIDDLEWARE, new LB(THIS_MIDDLEWARE + " plugin: no application found in the repository"));
            return;
        }
        StringBuffer appIdListBuffer = new StringBuffer();
        for (String appId : apps.keySet()) appIdListBuffer.append("+" + appId);
        String appIdList = appIdListBuffer.toString();
        appIdList = appIdList.substring(1);
        if (DEBUG) System.out.println("\nGetting application IMPLEMENTATIONS...");
        warning = false;
        if (POST) {
            if (DEBUG) System.out.println("Querying application implementations using POST method");
            url = new java.net.URL(repositoryURL + GET_IMPS_SERVLET + "");
            conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            String pars = "appids=" + appIdList;
            wr.write(pars);
            wr.flush();
        } else {
            url = new java.net.URL(repositoryURL + GET_IMPS_SERVLET + "?appids=" + appIdList);
            conn = url.openConnection();
        }
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = rd.readLine()) != null) {
            if (!line.matches(GET_IMPS_RESP_REGEXP)) {
                if (!warning) {
                    Base.writeLogg(THIS_MIDDLEWARE, new LB(THIS_MIDDLEWARE + " plugin: application implementation repository line has invalid syntax: " + line));
                    warning = true;
                }
                continue;
            }
            String appId = line.substring(0, line.indexOf(' '));
            String impId = line.substring(line.indexOf(' ') + 1).trim();
            if (DEBUG) if (imps.containsKey(impId) && !appId.equals(imps.get(impId))) if (DEBUG) System.out.println("WARNING: same implemenation but different apps");
            imps.put(impId, appId);
            if (DEBUG) System.out.println("\t" + apps.get(appId) + " " + impId + "");
        }
        rd.close();
        if (imps.size() == 0) {
            Base.writeLogg(THIS_MIDDLEWARE, new LB(THIS_MIDDLEWARE + " plugin: no implementation found in the repository"));
            return;
        }
        StringBuffer impIdListBuffer = new StringBuffer();
        for (String impId : imps.keySet()) impIdListBuffer.append("+" + impId);
        String impIdList = impIdListBuffer.toString();
        impIdList = impIdList.substring(1);
        if (DEBUG) System.out.println("\nGetting implementation URLS...");
        warning = false;
        if (POST) {
            url = new java.net.URL(repositoryURL + GET_IMP_FILE_URL_SERVLET + "");
            conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            String pars = "impids=" + impIdList;
            wr.write(pars);
            wr.flush();
        } else {
            url = new java.net.URL(repositoryURL + GET_IMP_FILE_URL_SERVLET + "?impids=" + impIdList);
            conn = url.openConnection();
        }
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = rd.readLine()) != null) {
            if (!line.matches(GET_IMP_FILE_URL_RESP_REGEXP)) {
                if (!warning) {
                    Base.writeLogg(THIS_MIDDLEWARE, new LB(THIS_MIDDLEWARE + " plugin: application executable repository line has invalid syntax: " + line));
                    warning = true;
                }
                continue;
            }
            String impId = line.substring(0, line.indexOf(' '));
            String fileUrl = line.substring(line.indexOf(' ') + 1).trim();
            if (!fileUrls.containsKey(impId)) {
                Vector<String> tempFileUrls = new Vector<String>();
                tempFileUrls.add(fileUrl);
                fileUrls.put(impId, tempFileUrls);
            } else {
                fileUrls.get(impId).add(fileUrl);
            }
            if (DEBUG) System.out.println("\t" + impId + " " + fileUrl + "");
        }
        rd.close();
        if (DEBUG) System.out.println("\nGetting implementation ATTRIBUTES...");
        warning = false;
        if (POST) {
            url = new java.net.URL(repositoryURL + GET_IMP_ATTRS_SERVLET + "");
            conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            String pars = "impids=" + impIdList;
            wr.write(pars);
            wr.flush();
        } else {
            url = new java.net.URL(repositoryURL + GET_IMP_ATTRS_SERVLET + "?impids=" + impIdList);
            conn = url.openConnection();
        }
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = rd.readLine()) != null) {
            if (!line.matches(GET_IMP_ATTRS_RESP_REGEXP)) continue;
            String impId = line.substring(0, line.indexOf(' '));
            line = line.substring(line.indexOf(' ') + 1);
            String attrName = line.substring(0, line.indexOf('='));
            String attrValue = line.substring(line.indexOf('=') + 1).trim();
            if (attrName.matches("^VOs[.]vo\\d+[.]name") && attrValue.matches("^gLite:.+")) {
                String voId = attrName.substring(attrName.indexOf('.') + 1, attrName.lastIndexOf('.'));
                String voName = attrValue.substring(attrValue.indexOf("gLite:") + 6);
                if (DEBUG) System.out.println("\t" + impId + " " + voName + " (" + voId + ")");
                if (voIds.containsKey(impId)) {
                    voIds.get(impId).add(voId);
                } else {
                    Vector<String> tempVoIds = new Vector<String>();
                    tempVoIds.add(voId);
                    voIds.put(impId, tempVoIds);
                }
                String key = impId + "_" + voId;
                voNames.put(key, voName);
            } else if (attrName.matches("^VOs[.]vo\\d+[.]site\\d+") && attrValue.matches(".+")) {
                String voId = attrName.substring(attrName.indexOf('.') + 1, attrName.lastIndexOf('.'));
                String ceUrl = attrValue;
                if (DEBUG) System.out.println("\t" + impId + " " + ceUrl + " (" + voId + ")");
                String key = impId + "_" + voId;
                if (ceUrls.containsKey(key)) {
                    ceUrls.get(key).add(ceUrl);
                } else {
                    Vector<String> tempCeUrls = new Vector<String>();
                    tempCeUrls.add(ceUrl);
                    ceUrls.put(key, tempCeUrls);
                }
            }
        }
        rd.close();
        Hashtable<String, Hashtable<String, Vector<String>>> finalApps = new Hashtable<String, Hashtable<String, Vector<String>>>();
        for (String impId : imps.keySet()) {
            if (voIds.containsKey(impId)) {
                for (String voId : voIds.get(impId)) {
                    String key = impId + "_" + voId;
                    final String appName = apps.get(imps.get(impId));
                    final String voName = voNames.get(key);
                    Hashtable<String, Vector<String>> tempVoHash = null;
                    Vector<String> tempCeList = null;
                    if (ceUrls.containsKey(key)) {
                        for (String ceUrl : ceUrls.get(key)) {
                            if (!finalApps.containsKey(appName)) {
                                tempCeList = new Vector<String>();
                                tempCeList.add(ceUrl);
                                tempVoHash = new Hashtable<String, Vector<String>>();
                                tempVoHash.put(voName, tempCeList);
                                finalApps.put(appName, tempVoHash);
                            } else {
                                tempVoHash = finalApps.get(appName);
                                if (!tempVoHash.containsKey(voName)) {
                                    tempCeList = new Vector<String>();
                                    tempCeList.add(ceUrl);
                                    tempVoHash.put(voName, tempCeList);
                                } else {
                                    tempCeList = tempVoHash.get(voName);
                                    if (!tempCeList.contains(ceUrl)) tempCeList.add(ceUrl); else if (DEBUG) System.out.println("WARNING: Duplicate CE URL: " + ceUrl + " " + appName + "(" + impId + ") VO:" + voName + "(" + voId + ") ");
                                }
                            }
                            if (fileUrls.containsKey(impId)) {
                                Vector<String> fs = fileUrls.get(impId);
                                if (DEBUG) for (String file : fs) {
                                    System.out.println("-----impId:" + impId + "appName:" + appName + "--file:" + file);
                                }
                                appFileUrls.put(appName, fs);
                            }
                        }
                    }
                }
            }
        }
        if (finalApps.size() == 0) {
            Base.writeLogg(THIS_MIDDLEWARE, new LB(THIS_MIDDLEWARE + " plugin: no application found with VO.name (gLite) and CE.site attributes"));
            return;
        }
        if (DEBUG) {
            System.out.println("Listing APPs, VOs, and CEs...");
            for (String app : finalApps.keySet()) {
                System.out.println("\n\"" + app + "\"");
                Hashtable<String, Vector<String>> vos = finalApps.get(app);
                for (String vo : vos.keySet()) {
                    System.out.println("\tVO: " + vo);
                    Vector<String> ces = vos.get(vo);
                    for (String ce : ces) {
                        System.out.println("\t\tCE: " + ce);
                    }
                }
            }
        }
        Vector<String> sortedApps = new Vector<String>();
        for (String appName : finalApps.keySet()) sortedApps.add(appName);
        Collections.sort(sortedApps, new AppNameComparator());
        for (String appName : sortedApps) {
            Item.Edgi.Job job = new Item.Edgi.Job();
            job.setName(appName);
            try {
                Vector<String> exeurls = appFileUrls.get(appName);
                for (String eurl : exeurls) {
                    Edgi.Job.Exeurl exeurl = new Edgi.Job.Exeurl();
                    exeurl.setUrl(eurl);
                    job.getExeurl().add(exeurl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            jobList.add(job);
            Hashtable<String, Vector<String>> vos = finalApps.get(appName);
            for (String voName : vos.keySet()) {
                Item.Edgi.Job.Vo vo = new Item.Edgi.Job.Vo();
                vo.setName(voName);
                job.getVo().add(vo);
                Vector<String> ces = vos.get(voName);
                for (String ceName : ces) {
                    vo.getCe().add(ceName);
                }
            }
        }
    }
