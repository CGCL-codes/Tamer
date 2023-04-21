    private byte[] checkGuideData(HTTPurl urlData) throws Exception {
        GuideStore guide = GuideStore.getInstance();
        StringBuffer buff = new StringBuffer();
        String[] epgChannels = guide.getChannelList();
        DecimalFormat oneDec = new DecimalFormat("0.0");
        buff.append("<table width='600'>");
        buff.append("<tr>");
        buff.append("<td class='itemheading'><strong>Channel</strong></td>");
        buff.append("<td class='itemheading' align='center'><strong>Count</strong></td>");
        buff.append("<td class='itemheading' align='center'><strong>Start</strong></td>");
        buff.append("<td class='itemheading' align='center'><strong>Span</strong></td>");
        buff.append("</tr>\n");
        for (int x = 0; x < epgChannels.length; x++) {
            GuideItem[] items = guide.getProgramsForChannel(epgChannels[x]);
            buff.append("<tr><td align='left'>" + epgChannels[x] + "</td><td align='center'>" + items.length + "</td>");
            long span = items[items.length - 1].getStart().getTime() - items[0].getStart().getTime();
            double spanDays = (double) span / (double) (1000 * 60 * 60 * 24);
            long start = items[0].getStart().getTime() - new Date().getTime();
            double startDays = (double) start / (double) (1000 * 60 * 60 * 24);
            buff.append("<td align='center'>" + oneDec.format(startDays) + "</td><td align='center'>" + oneDec.format(spanDays) + "</td>");
            buff.append("</tr>\n");
        }
        buff.append("</table>\n");
        buff.append("<br>\n");
        buff.append("<table border='0' cellspacing='0' cellpadding='0' width='600'>");
        buff.append("<tr><td class='itemheading'><strong>Continuity Report</strong></td></tr><tr><td>");
        for (int x = 0; x < epgChannels.length; x++) {
            buff.append("<table>");
            buff.append("<tr><td>" + epgChannels[x] + "</td></tr>");
            buff.append("<tr><td><img src='/servlet/ContinuityImageDataRes?action=1&channel=" + URLEncoder.encode(epgChannels[x], "UTF-8") + "'></td></tr>\n");
            buff.append("</table>");
        }
        buff.append("</td></tr></table>\n");
        StringBuffer overaps = new StringBuffer();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MMM/yy");
        for (int x = 0; x < epgChannels.length; x++) {
            GuideItem[] items = guide.getProgramsForChannel(epgChannels[x]);
            for (int y = 0; y < items.length - 1; y++) {
                GuideItem item1 = items[y];
                GuideItem item2 = items[y + 1];
                if (item2.getStart().getTime() < item1.getStop().getTime()) {
                    overaps.append("<tr>\n");
                    overaps.append("<td valign='top'>" + epgChannels[x] + "</td>\n");
                    overaps.append("<td>");
                    overaps.append("<table>");
                    overaps.append("<tr><td>" + item1.getName() + "</td><td>" + dateFormat.format(item1.getStart()) + "</td><td>(" + item1.getDuration() + ")</td></tr>\n");
                    overaps.append("<tr><td>" + item2.getName() + "</td><td>" + dateFormat.format(item2.getStart()) + "</td><td>(" + item2.getDuration() + ")</td></tr>\n");
                    overaps.append("</table>");
                    overaps.append("</td>");
                    long diff = item1.getStop().getTime() - item2.getStart().getTime();
                    diff = diff / (1000 * 60);
                    overaps.append("<td valign='top'>" + diff + "</td>\n");
                    overaps.append("</tr>\n");
                }
            }
        }
        if (overaps.length() > 0) {
            buff.append("<br><table border='0' cellspacing='5' cellpadding='5' width='600'>");
            buff.append("<tr><td nowrap class='itemheading' colspan='3'><strong>Overlapping Item Report</strong></td></tr>\n");
            buff.append("<tr>\n");
            buff.append("<td nowrap><strong>Channel</strong></td>\n");
            buff.append("<td nowrap><strong>Items</strong></td>\n");
            buff.append("<td nowrap><strong>Duration</strong></td>\n");
            buff.append(overaps);
            buff.append("</tr></table>\n");
        }
        StringBuffer gaps = new StringBuffer();
        for (int x = 0; x < epgChannels.length; x++) {
            GuideItem[] items = guide.getProgramsForChannel(epgChannels[x]);
            for (int y = 0; y < items.length - 1; y++) {
                GuideItem item1 = items[y];
                GuideItem item2 = items[y + 1];
                if (item2.getStart().getTime() > item1.getStop().getTime()) {
                    gaps.append("<tr>\n");
                    gaps.append("<td valign='top'>" + epgChannels[x] + "</td>\n");
                    gaps.append("<td>" + dateFormat.format(item1.getStop()) + "</td>\n");
                    gaps.append("<td>" + dateFormat.format(item2.getStart()) + "</td>\n");
                    long diff = item2.getStart().getTime() - item1.getStop().getTime();
                    diff = diff / (1000 * 60);
                    gaps.append("<td valign='top'>" + diff + "</td>\n");
                    gaps.append("</tr>\n");
                }
            }
        }
        if (gaps.length() > 0) {
            buff.append("<br><table border='0' cellspacing='5' cellpadding='5' width='600'>");
            buff.append("<tr><td nowrap class='itemheading' colspan='4'><strong>Gap Report</strong></td></tr>\n");
            buff.append("<tr>\n");
            buff.append("<td nowrap><strong>Channel</strong></td>\n");
            buff.append("<td nowrap><strong>From</strong></td>\n");
            buff.append("<td nowrap><strong>To</strong></td>\n");
            buff.append("<td nowrap><strong>Duration</strong></td>\n");
            buff.append(gaps);
            buff.append("</tr></table>\n");
        }
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "epg-conflicts.html");
        template.replaceAll("$title", "EPG Data Report");
        template.replaceAll("$list", buff.toString());
        return template.getPageBytes();
    }
