    private void load(Map map, String property, WebApp app) {
        Resource[] resources = app.getResources(property);
        try {
            for (int i = 0; i < resources.length; i++) {
                if (resources[i].exists()) {
                    URL url = resources[i].getURL();
                    if (url == null) {
                        log.info("Desklet properties file " + property + " not found.");
                        return;
                    }
                    log.info("Load property from " + url);
                    Properties pp = new Properties();
                    pp.load(url.openStream());
                    Iterator iter = pp.entrySet().iterator();
                    while (iter.hasNext()) {
                        Entry entry = (Entry) iter.next();
                        String name = (String) entry.getValue();
                        String path = (String) entry.getKey();
                        DeskletDef def = loadDef(path, name);
                        if (map.get(path) != null) {
                            throw new SysException("Load desklet file " + def + " error. same path desklet " + map.get(path) + " already exist.");
                        }
                        map.put(path, def);
                    }
                    log.info("Load property from " + url + " finished.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new SysException("Load property file " + property + " error.");
        }
    }
