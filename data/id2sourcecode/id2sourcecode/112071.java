    private byte[] showAvailableThemes(HTTPurl urlData, HashMap<String, String> headers) throws Exception {
        StringBuffer out = new StringBuffer();
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "ShowThemes.html");
        String httpDir = store.getProperty("path.httproot");
        String themeDir = store.getProperty("path.theme");
        File themeDirs = new File(httpDir + File.separator + "themes");
        int count = 0;
        if (themeDirs.exists()) {
            File[] dirs = themeDirs.listFiles();
            for (int x = 0; x < dirs.length; x++) {
                if (dirs[x].isDirectory() && dirs[x].isHidden() == false) {
                    count++;
                    out.append("<option value=\"" + dirs[x].getName() + "\"");
                    if (dirs[x].getName().equalsIgnoreCase(themeDir)) out.append(" SELECTED ");
                    out.append(">" + dirs[x].getName() + "</option>\n");
                }
            }
        }
        if (count == 0) {
            out.append("<option value=\"none\">none available</option>\n");
        }
        template.replaceAll("$themeList", out.toString());
        String currentEPGTheme = store.getProperty("path.theme.epg");
        out = new StringBuffer();
        String xslDir = store.getProperty("path.xsl");
        count = 0;
        File xslDirs = new File(xslDir);
        if (xslDirs.exists()) {
            File[] xslFiles = xslDirs.listFiles();
            for (int x = 0; x < xslFiles.length; x++) {
                if (xslFiles[x].isDirectory() == false) {
                    if (xslFiles[x].getName().matches("epg-.*.xsl")) {
                        count++;
                        out.append("<option value=\"" + xslFiles[x].getName() + "\"");
                        if (xslFiles[x].getName().equalsIgnoreCase(currentEPGTheme)) out.append(" SELECTED ");
                        String name = xslFiles[x].getName().substring(4, xslFiles[x].getName().length() - 4);
                        out.append(">" + name + "</option>\n");
                    }
                }
            }
        }
        if (count == 0) {
            out.append("<option value=\"none\">none available</option>\n");
        }
        template.replaceAll("$epg_themeList", out.toString());
        out = new StringBuffer();
        String[] agentList = store.getAgentMappingList();
        for (int x = 0; x < agentList.length; x++) {
            String themeForAgent = store.getThemeForAgent(agentList[x]);
            out.append("<tr>");
            out.append("<td>" + agentList[x] + "</td>");
            out.append("<td>" + themeForAgent + "</td>");
            out.append("<td><a href='/servlet/SystemDataRes?action=37&agent=" + URLEncoder.encode(agentList[x], "UTF-8") + "'><img src='/images/delete.png' alt='Delete Mapping' align='absmiddle' border='0' height='24' width='24'></a></td>");
            out.append("</tr>\n");
        }
        template.replaceAll("$themeMappings", out.toString());
        template.replaceAll("$agentString", headers.get("User-Agent"));
        return template.getPageBytes();
    }
