    public void formatProgsAsTVPI(ProgramList progs, Writer out, boolean gmt) throws IOException {
        PrintWriter outPW = new PrintWriter(out);
        outPW.println("<tv-program-info version=\"1.0\">");
        progs.sortAndRemoveDups();
        Iterator it = progs.iterator();
        ProgItem elProgram;
        if (gmt) {
            TimeZone tz = TimeZone.getTimeZone("GMT");
            m_UTCTimeFormat.setTimeZone(tz);
        }
        while (it.hasNext()) {
            elProgram = (ProgItem) it.next();
            if (elProgram != null) {
                String station = getChannel(elProgram);
                station = getChannelDesc(station);
                int chPos;
                String channel = null;
                if ((chPos = station.indexOf(' ')) != -1) {
                    channel = station.substring(0, chPos);
                    station = station.substring(chPos + 1);
                }
                outPW.println("<program>");
                outPW.println("<station>" + station + "</station>");
                outPW.println("<tv-mode>cable</tv-mode>");
                outPW.print("<program-title>" + getData(elProgram, ProgramData.TITLE));
                String subtitle = getData(elProgram, ProgramData.SUBTITLE);
                if (subtitle != null) {
                    outPW.print(": " + subtitle);
                }
                outPW.println("</program-title>");
                String desc = getData(elProgram, ProgramData.DESC);
                if (desc != null) {
                    outPW.println("<program-description>" + desc + "</program-description>");
                }
                String start = getStartTime(elProgram);
                String end = getStopTime(elProgram);
                Calendar startTime = Utilities.makeCal(start);
                Calendar endTime = Utilities.makeCal(end);
                String startUTC = m_UTCTimeFormat.format(startTime.getTime());
                String endUTC = m_UTCTimeFormat.format(endTime.getTime());
                outPW.println("<start-date>" + startUTC.substring(0, 8) + "</start-date>");
                outPW.println("<start-time>" + startUTC.substring(8, 10) + ":" + startUTC.substring(10, 12) + "</start-time>");
                outPW.println("<end-date>" + endUTC.substring(0, 8) + "</end-date>");
                outPW.println("<end-time>" + endUTC.substring(8, 10) + ":" + endUTC.substring(10, 12) + "</end-time>");
                long diff = endTime.getTime().getTime() - startTime.getTime().getTime();
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumIntegerDigits(2);
                nf.setMaximumFractionDigits(0);
                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);
                long minutes = diff / (60 * 1000);
                outPW.println("<duration>" + nf.format(hours) + ":" + nf.format(minutes) + "</duration>");
                if (channel != null) {
                    outPW.println("<rf-channel>" + channel + "</rf-channel>");
                }
                outPW.println("</program>");
            }
        }
        outPW.println("</tv-program-info>");
    }
