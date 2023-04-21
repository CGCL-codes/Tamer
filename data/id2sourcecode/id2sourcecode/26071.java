    private byte[] nownextEPG(HTTPurl urlData) throws Exception {
        GuideStore guide = GuideStore.getInstance();
        StringBuffer buff = new StringBuffer();
        buff.append("<b>Now Showing (" + (new Date()).toString() + ")</b><p>\n");
        buff.append("<a href='/servlet/EpgDataRes?action=21");
        buff.append("'>Refresh This Screen</a><p>\n");
        buff.append("<table>\n");
        buff.append("<tr>");
        buff.append("<td>&nbsp;</td>");
        buff.append("<td>Program Name</td>");
        buff.append("<td>Time</td>");
        buff.append("</tr>");
        Vector chanMap = guide.getChannelMap();
        Set wsChannels = store.getChannels().keySet();
        Calendar startTime = Calendar.getInstance();
        Date Now = new Date();
        for (int y = 0; y < chanMap.size(); y++) {
            String[] map = (String[]) chanMap.get(y);
            boolean bNext = false;
            if (wsChannels.contains(map[0])) {
                GuideItem[] items = guide.getProgramsForChannel(map[1]);
                buff.append("<tr><td colspan='5' bgcolor=#000000>\n");
                buff.append("<span class='areaTitle'>" + map[0] + "</span><br>\n");
                buff.append("</td></tr\n");
                for (int x = 0; x < items.length; x++) {
                    GuideItem item = items[x];
                    startTime.setTime(item.getStart());
                    if ((item.getStart().before(Now) && item.getStop().after(Now)) || bNext) {
                        boolean bNow = !bNext;
                        if (!bNext) bNext = true; else bNext = false;
                        int hour = startTime.get(Calendar.HOUR_OF_DAY);
                        if (hour > 12) hour = hour - 12; else if (hour == 0) hour = 12;
                        String startString = intToStr(hour) + ":" + intToStr(startTime.get(Calendar.MINUTE)) + " " + store.ampm.get(new Integer(startTime.get(Calendar.AM_PM)));
                        buff.append("<tr>\n");
                        buff.append("<td class='epgSearchResults'><b>" + (bNow ? "NOW" : "NEXT") + "</b></td>");
                        buff.append("<td class='epgSearchResults'>");
                        String infoUrl = "/servlet/EpgDataRes?action=06&id=" + item.toString() + "&channel=" + URLEncoder.encode(map[0], "UTF-8");
                        buff.append("<span class='programName' onClick=\"openDetails('" + infoUrl + "')\">");
                        buff.append("<b>" + item.getName() + "</b>");
                        buff.append("</span>");
                        buff.append("</td>");
                        buff.append("<td class='epgSearchResults'>" + startString + " (" + item.getDuration() + ")</td>");
                        buff.append("</tr>\n");
                        if (bNow) buff.append("<tr><td colspan=5><font size=2>" + item.getDescription() + "</font></td></tr>\n");
                        if (!bNow) buff.append("<tr><td colspan=5>&nbsp;</td></tr>\n");
                    }
                }
            }
        }
        buff.append("</table><p>\n");
        PageTemplate template = new PageTemplate(store.getProperty("path.template").replace('\\', File.separatorChar) + File.separator + "epg-nownext.html");
        template.replaceAll("$result", buff.toString());
        template.addCookie("backURL", urlData.getReqString());
        return template.getPageBytes();
    }
