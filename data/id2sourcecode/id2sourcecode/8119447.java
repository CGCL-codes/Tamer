    public void formatProg(ProgItem prog, Calendar start, StringBuffer buf, boolean html) {
        if (html) buf.append("<blockquote><p><font size=\"2\">");
        buf.append("  ");
        String channel = getChannel(prog);
        buf.append(getChannelDesc(channel));
        buf.append(' ');
        if (html) buf.append("<b><i>");
        buf.append(getData(prog, ProgramData.TITLE));
        String subtitle = getData(prog, ProgramData.SUBTITLE);
        if (subtitle != null) {
            if (html) buf.append(" &quot;"); else buf.append(" \"");
            buf.append(subtitle);
            if (html) buf.append("&quot;"); else buf.append("\"");
        }
        if (html) buf.append("</i></b>");
        String desc = getData(prog, ProgramData.DESC);
        if (desc != null) {
            buf.append(" - ");
            buf.append(desc);
        }
        String date = getData(prog, ProgramData.DATE);
        if (date != null) {
            buf.append(" (");
            buf.append(date);
            buf.append(')');
        }
        StringBuffer categories = new StringBuffer();
        Vector vCat = getCategories(prog);
        int vCatSize = vCat.size();
        for (int i = 0; i < vCatSize; i++) {
            if (categories.length() > 0) categories.append(", ");
            categories.append(vCat.get(i));
        }
        if (categories.length() > 0) {
            buf.append(" (");
            buf.append(categories.toString());
            buf.append(')');
        }
        StringBuffer qualifiers = new StringBuffer();
        if (getClosedCaption(prog)) {
            if (qualifiers.length() > 0) qualifiers.append(", ");
            qualifiers.append("CC");
        }
        String stop = getStopTime(prog);
        if (stop != null) {
            if (qualifiers.length() > 0) qualifiers.append(", ");
            Calendar end = Utilities.makeCal(getStopTime(prog));
            qualifiers.append("ends at " + formatTime(end));
        }
        if (qualifiers.length() > 0) {
            buf.append(" (");
            buf.append(qualifiers.toString());
            buf.append(')');
        }
        if (html) buf.append("</font></p></blockquote>");
        buf.append("\r\n");
    }
