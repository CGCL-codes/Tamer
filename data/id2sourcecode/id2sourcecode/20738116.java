    private static void merge(String url, String destDir) throws Exception {
        if ("none".equals(url)) return;
        Properties propsIn = new Properties();
        propsIn.load(new URL(url).openStream());
        if (propsIn.isEmpty()) return;
        File destFile = new File(destDir, getSettingsFilename());
        Properties orig = new Properties();
        try {
            FileInputStream origIn = new FileInputStream(destFile);
            orig.load(origIn);
            origIn.close();
        } catch (Exception e) {
        }
        Properties propsOut = new Properties();
        for (Iterator i = propsIn.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            String key = (String) e.getKey();
            if (!key.startsWith(PROP_PREFIX)) continue;
            String settingName = key.substring(PROP_PREFIX.length());
            String value = (String) e.getValue();
            if (!orig.containsKey(settingName)) propsOut.put(settingName, value);
        }
        if (propsOut.isEmpty()) return;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile, true)));
        out.newLine();
        out.newLine();
        for (Iterator i = propsOut.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            out.write((String) e.getKey());
            out.write("=");
            out.write((String) e.getValue());
            out.newLine();
        }
        out.close();
    }
