    private static String[] doVersionCheck() {
        URL url = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            url = new URL("http://mysqlmt.svn.sourceforge.net/viewvc/mysqlmt/MysqlMonitorTool/version");
            isr = new InputStreamReader(url.openStream());
            br = new BufferedReader(isr);
            String newVersion = br.readLine();
            String downloadUrl = br.readLine();
            if (compareVersions(getVersion(), newVersion)) {
                return new String[] { newVersion, downloadUrl };
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
