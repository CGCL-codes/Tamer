    private byte[] showChannelMapping(HTTPurl urlData) throws Exception {
        GuideStore guide = GuideStore.getInstance();
        StringBuffer buff = new StringBuffer();
        StringBuffer warnings = new StringBuffer();
        Vector chanMap = guide.getChannelMap();
        PageTemplate template = new PageTemplate(store.getProperty("path.template").replace('\\', File.separatorChar) + File.separator + "epg-mapping.html");
        Set<String> wsChanSet = store.getChannels().keySet();
        String[] wsChannels = (String[]) wsChanSet.toArray(new String[0]);
        for (int x = 0; x < chanMap.size(); x++) {
            int problem = 0;
            String[] map = (String[]) chanMap.get(x);
            GuideItem[] items = guide.getProgramsForChannel(map[1]);
            if (!wsChanSet.contains(map[0])) {
                warnings.append("TV Scheduler Pro Channel (" + map[0] + ") does not exist!<br>\n");
                problem = 1;
            }
            if (items.length == 0) {
                warnings.append("There is currently no data for EPG channel (" + map[1] + ")<br>\n");
                problem += 2;
            }
            buff.append("<tr>");
            buff.append("<td>");
            if (problem == 1 || problem == 3) {
                buff.append("<img align='absmiddle' src='/images/exclaim24.png' border='0' alt='Error' width='22' height='24'>");
            }
            buff.append(map[0] + "</td><td>");
            if (problem == 2 || problem == 3) {
                buff.append("<img align='absmiddle' src='/images/exclaim24.png' border='0' alt='Error' width='22' height='24'>");
            }
            buff.append(map[1] + "</td>\n");
            buff.append("<td>");
            buff.append("<a href='/servlet/EpgDataRes?action=07&id=" + x + "'><img align='absmiddle' border='0' alt='Delete' src='/images/delete.png' width='24' height='24'></a>\n");
            buff.append("<a href='/servlet/EpgDataRes?action=24&id=" + x + "&dir=0'><img align='absmiddle' border='0' alt='Up' src='/images/up01.png' width='7' height='7'></a>\n");
            buff.append("<a href='/servlet/EpgDataRes?action=24&id=" + x + "&dir=1'><img align='absmiddle' border='0' alt='Down' src='/images/down01.png' width='7' height='7'></a>\n");
            buff.append("</td>\n");
            buff.append("</tr>\n");
        }
        template.replaceAll("$channelmap", buff.toString());
        Arrays.sort(wsChannels, String.CASE_INSENSITIVE_ORDER);
        buff = new StringBuffer();
        for (int x = 0; x < wsChannels.length; x++) {
            buff.append("<OPTION VALUE=\"" + wsChannels[x] + "\"> " + wsChannels[x] + "\n");
        }
        template.replaceAll("$wsChannels", buff.toString());
        String[] epgChannels = (String[]) guide.getChannelList();
        Arrays.sort(epgChannels, String.CASE_INSENSITIVE_ORDER);
        buff = new StringBuffer();
        for (int x = 0; x < epgChannels.length; x++) {
            buff.append("<OPTION VALUE=\"" + epgChannels[x] + "\"> " + epgChannels[x] + "\n");
        }
        template.replaceAll("$epgChannels", buff.toString());
        if (warnings.length() > 0) {
            String warningText = "<table><tr><td style='border: 1px solid #FFFFFF;'>" + "<img align='absmiddle' src='/images/exclaim24.png' border='0' alt='Error' width='22' height='24'><b>Warnings</b>" + "</td></tr><tr><td>";
            warningText += warnings.toString() + "</td></tr></table>";
            template.replaceAll("$warning", warningText);
        } else template.replaceAll("$warning", "");
        return template.getPageBytes();
    }
