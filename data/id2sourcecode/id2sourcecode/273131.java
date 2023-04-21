    private byte[] quicksearchEPG(HTTPurl urlData) throws Exception {
        GuideStore guide = GuideStore.getInstance();
        String doSearch = urlData.getParameter("search");
        StringBuffer buff = new StringBuffer();
        String kwMatch = urlData.getParameter("kwMatch");
        if (kwMatch == null) kwMatch = "";
        kwMatch = kwMatch.trim();
        String catMatch = urlData.getParameter("catMatch");
        if (catMatch == null || catMatch.length() < 2) catMatch = "any";
        String chanMatch = urlData.getParameter("chanMatch");
        if (chanMatch == null || chanMatch.length() == 0) chanMatch = "any";
        int typeMatch = 0;
        try {
            typeMatch = Integer.parseInt(urlData.getParameter("typeMatch"));
        } catch (Exception e) {
        }
        int startHH = 0;
        try {
            startHH = Integer.parseInt(urlData.getParameter("startHH"));
        } catch (Exception e) {
        }
        int startMM = 0;
        try {
            startMM = Integer.parseInt(urlData.getParameter("startMM"));
        } catch (Exception e) {
        }
        int endHH = 23;
        try {
            endHH = Integer.parseInt(urlData.getParameter("endHH"));
        } catch (Exception e) {
        }
        int endMM = 59;
        try {
            endMM = Integer.parseInt(urlData.getParameter("endMM"));
        } catch (Exception e) {
        }
        int ignored = 2;
        try {
            ignored = Integer.parseInt(urlData.getParameter("ignored"));
        } catch (Exception e) {
        }
        HashMap<String, Vector<GuideItem>> results = new HashMap<String, Vector<GuideItem>>();
        int num = 0;
        if (doSearch != null && doSearch.equalsIgnoreCase("yes")) {
            int[] times = new int[4];
            times[0] = startHH;
            times[1] = startMM;
            times[2] = endHH;
            times[3] = endMM;
            num = guide.simpleEpgSearch(kwMatch, typeMatch, catMatch, chanMatch, ignored, times, results);
            buff.append("Your quick search for (" + HTMLEncoder.encode(kwMatch) + ") returned " + num + " results.<p>\n");
            if (results.size() > 0) {
                buff.append("<span class='areaTitle'>Search Results:</span><br><br>\n");
                buff.append("<table class='epgSearchResults'>\n");
                buff.append("<tr>");
                buff.append("<td>Program Name</td>");
                buff.append("<td>Time</td>");
                buff.append("<td>Duration</td>");
                buff.append("<td>Action</td>");
                buff.append("</tr>");
                Calendar startTime = Calendar.getInstance();
                String[] keys = (String[]) results.keySet().toArray(new String[0]);
                for (int y = 0; y < keys.length; y++) {
                    Vector result = (Vector) results.get(keys[y]);
                    if (result.size() > 0) {
                        buff.append("<tr><td colspan='4'>\n");
                        buff.append("<span class='areaTitle'>Channel : " + keys[y] + "</span><br>\n");
                        buff.append("</td></tr\n");
                        for (int x = 0; x < result.size(); x++) {
                            GuideItem item = (GuideItem) result.get(x);
                            startTime.setTime(item.getStart());
                            int hour = startTime.get(Calendar.HOUR);
                            if (hour == 0) hour = 12;
                            String startString = intToStr(hour) + ":" + intToStr(startTime.get(Calendar.MINUTE)) + " " + store.ampm.get(new Integer(startTime.get(Calendar.AM_PM)));
                            String dateString = store.dayName.get(new Integer(startTime.get(Calendar.DAY_OF_WEEK))) + ", " + startTime.get(Calendar.DATE) + " " + store.monthNameShort.get(new Integer(startTime.get(Calendar.MONTH)));
                            buff.append("<tr>\n");
                            buff.append("<td class='epgSearchResults'>" + item.getName());
                            if (item.getSubName() != null && item.getSubName().length() > 0) buff.append(" (" + item.getSubName() + ")");
                            buff.append("</td>");
                            buff.append("<td class='epgSearchResults'>" + dateString + " at " + startString + "</td>");
                            buff.append("<td class='epgSearchResults'>" + item.getDuration() + "</td>");
                            buff.append("<td class='epgSearchResults'>");
                            String infoUrl = "/servlet/EpgDataRes?action=06&id=" + item.toString() + "&channel=" + URLEncoder.encode(keys[y], "UTF-8");
                            buff.append("<span class='programName' onClick=\"openDetails('" + infoUrl + "')\">");
                            buff.append("Details");
                            buff.append("</span> | ");
                            if (startTime.get(Calendar.HOUR_OF_DAY) < 6) startTime.add(Calendar.DATE, -1);
                            String epgUrl = "/servlet/EpgDataRes?action=12" + "&year=" + startTime.get(Calendar.YEAR) + "&month=" + (startTime.get(Calendar.MONTH) + 1) + "&day=" + startTime.get(Calendar.DATE) + "&scrollto=" + startTime.get(Calendar.HOUR_OF_DAY);
                            buff.append("<a class='infoNav' href='" + epgUrl + "'>EPG</a>");
                            buff.append("</td>");
                            buff.append("</tr>\n");
                        }
                    }
                }
                buff.append("</table><p>\n");
            }
        }
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "epg-qsearch.html");
        template.replaceAll("$result", buff.toString());
        if (kwMatch.length() > 0) template.replaceAll("$kwMatch", HTMLEncoder.encode(kwMatch)); else template.replaceAll("$kwMatch", "");
        String[] cats = guide.getCategoryStrings();
        Arrays.sort(cats, String.CASE_INSENSITIVE_ORDER);
        StringBuffer bufc = new StringBuffer();
        if (cats.length > 0) {
            bufc.append("<option value='any'");
            if (catMatch.equalsIgnoreCase("any")) bufc.append(" Selected");
            bufc.append(">Any</option>\n");
            for (int z = 0; z < cats.length; z++) {
                bufc.append("<option value='" + cats[z] + "'");
                if (catMatch.equalsIgnoreCase(cats[z])) {
                    bufc.append(" Selected");
                }
                bufc.append(">" + cats[z] + "</option>\n");
            }
        } else bufc.append("<option value='any'>N/A</option>\n");
        template.replaceAll("$catList", bufc.toString());
        StringBuffer chanBuff = new StringBuffer();
        String[] channels = (String[]) store.getChannels().keySet().toArray(new String[0]);
        Arrays.sort(channels);
        chanBuff.append("<option value='any'");
        if (chanMatch.equalsIgnoreCase("any")) chanBuff.append(" Selected");
        chanBuff.append(">Any</option>\n");
        for (int x = 0; x < channels.length; x++) {
            chanBuff.append("<option value='" + channels[x] + "'");
            if (chanMatch.equalsIgnoreCase(channels[x])) chanBuff.append(" Selected");
            chanBuff.append(">" + channels[x] + "</option>\n");
        }
        template.replaceAll("$chanList", chanBuff.toString());
        StringBuffer typeBuff = new StringBuffer();
        typeBuff.append("<option value='0'");
        if (typeMatch == 0) typeBuff.append(" Selected");
        typeBuff.append(">Name or Description</option>\n");
        typeBuff.append("<option value='1'");
        if (typeMatch == 1) typeBuff.append(" Selected");
        typeBuff.append(">Name</option>\n");
        typeBuff.append("<option value='2'");
        if (typeMatch == 2) typeBuff.append(" Selected");
        typeBuff.append(">Description</option>\n");
        template.replaceAll("$typeList", typeBuff.toString());
        StringBuffer ignoredBuff = new StringBuffer();
        ignoredBuff.append("<option value='0'");
        if (ignored == 0) ignoredBuff.append(" Selected");
        ignoredBuff.append(">No</option>\n");
        ignoredBuff.append("<option value='1'");
        if (ignored == 1) ignoredBuff.append(" Selected");
        ignoredBuff.append(">Yes</option>\n");
        ignoredBuff.append("<option value='2'");
        if (ignored == 2) ignoredBuff.append(" Selected");
        ignoredBuff.append(">Either</option>\n");
        template.replaceAll("$ignoredList", ignoredBuff.toString());
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        nf.setMaximumIntegerDigits(2);
        nf.setMinimumIntegerDigits(2);
        template.replaceAll("$startHH", nf.format(startHH));
        template.replaceAll("$startMM", nf.format(startMM));
        template.replaceAll("$endHH", nf.format(endHH));
        template.replaceAll("$endMM", nf.format(endMM));
        template.addCookie("backURL", urlData.getReqString());
        return template.getPageBytes();
    }
