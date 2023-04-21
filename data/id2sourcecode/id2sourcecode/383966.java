    private String getScheduleInfo(ScheduleItem item, HTTPurl urlData) {
        StringBuffer data = new StringBuffer(2048);
        data.append("<p style='border: solid 1px #FFFFFF; padding: 3px; width: 100%;'>Schedule Details:</p>\n");
        data.append("Current State : " + item.getStatus() + " (" + item.getState() + ")<p>");
        String type = "? " + item.getType() + " ?";
        if (item.getType() == ScheduleItem.ONCE) type = "Once"; else if (item.getType() == ScheduleItem.DAILY) type = "Daily"; else if (item.getType() == ScheduleItem.WEEKLY) type = "Weekly"; else if (item.getType() == ScheduleItem.MONTHLY) type = "Monthly"; else if (item.getType() == ScheduleItem.WEEKDAY) type = "Week Day"; else if (item.getType() == ScheduleItem.EPG) type = "EPG";
        data.append("This Scheduled Item is set to trigger " + type + " <p>");
        data.append("<table>");
        data.append("<tr><td>Start</td><td>" + dtf.format(item.getStart()) + "</td></tr>");
        data.append("<tr><td>Stop</td><td>" + dtf.format(item.getStop()) + "</td></tr>");
        data.append("<tr><td>Duration</td><td>" + item.getDuration() + "</td></tr>");
        data.append("<tr><td>Channel</td><td>" + item.getChannel() + "</td></tr>");
        data.append("</table><p>");
        data.append("Time to next trigger : " + getTimeLeft(item.getStart()) + "<p>");
        data.append("Name Pattern : " + item.getFilePattern() + "<br>");
        String[] paths = store.getCapturePaths();
        String capName = item.getFileName();
        data.append("File : " + capName + "<br>");
        if (item.getCapturePathIndex() == -1) data.append("Path : AutoSelect<br>"); else if (item.getCapturePathIndex() < 0 || item.getCapturePathIndex() > paths.length - 1) data.append("Path : Out of range!<br>"); else {
            try {
                data.append("Path : " + new File(paths[item.getCapturePathIndex()]).getCanonicalPath() + "<br>");
            } catch (Exception e) {
                data.append("Path : Does not exist!<br>");
            }
        }
        data.append("<p>");
        if (item.getCreatedFrom() != null) {
            data.append("Created From :<pre>");
            data.append("Title      : " + item.getCreatedFrom().getName() + "\n");
            data.append("Sub Title  : " + item.getCreatedFrom().getSubName() + "\n");
            data.append("Start      : " + item.getCreatedFrom().getStart().toString() + "\n");
            data.append("Duration   : " + item.getCreatedFrom().getDuration() + "\n");
            data.append("</pre><p>");
        }
        Vector<CaptureCapability> capabilities = CaptureCapabilities.getInstance().getCapabilities();
        String capType = "ERROR";
        if (item.getCapType() == -1) {
            capType = "AutoSelect";
        } else {
            for (int x = 0; x < capabilities.size(); x++) {
                CaptureCapability capability = capabilities.get(x);
                if (capability.getTypeID() == item.getCapType()) capType = capability.getName();
            }
        }
        data.append("Capture Type : " + capType + "<p>");
        data.append("Is Auto Deletable : " + item.isAutoDeletable() + "<br>");
        data.append("Keep for : " + item.getKeepFor() + " days before auto deleting.<p>");
        data.append("Post Capture Task : " + item.getPostTask() + "<p>");
        data.append("Post Capture Task Enabled : " + item.getPostTaskEnabled() + "<p>");
        data.append("<p style='border: solid 1px #FFFFFF; padding: 3px; width: 100%;'>Signal Statistics:</p>\n");
        data.append("<table cellpadding='2' cellspacing='2'>\n");
        data.append("<tr><td>&nbsp;</td>");
        data.append("<td>Strength</td>");
        data.append("<td>Quality</td></tr>\n");
        HashMap<Date, SignalStatistic> stats = item.getSignalStatistics();
        Date[] keys = stats.keySet().toArray(new Date[0]);
        Arrays.sort(keys);
        NumberFormat nf2Dec = NumberFormat.getInstance();
        nf2Dec.setMaximumFractionDigits(2);
        double strengthMIN = -1;
        double strengthAVG = 0;
        double strengthMAX = -1;
        double qualityMIN = -1;
        double qualityAVG = 0;
        double qualityMAX = -1;
        for (int x = 0; x < keys.length; x++) {
            SignalStatistic value = stats.get(keys[x]);
            if (strengthMIN == -1 || value.getStrength() < strengthMIN) strengthMIN = value.getStrength();
            if (strengthMAX == -1 || value.getStrength() > strengthMAX) strengthMAX = value.getStrength();
            if (qualityMIN == -1 || value.getQuality() < qualityMIN) qualityMIN = value.getQuality();
            if (qualityMAX == -1 || value.getQuality() > qualityMAX) qualityMAX = value.getQuality();
            strengthAVG += value.getStrength();
            qualityAVG += value.getQuality();
        }
        if (keys.length > 0) {
            strengthAVG /= keys.length;
            qualityAVG /= keys.length;
        }
        data.append("<tr><td align='left'>Minimum</td>");
        data.append("<td align='center'>" + nf2Dec.format(strengthMIN) + "</td>");
        data.append("<td align='center'>" + nf2Dec.format(qualityMIN) + "</td></tr>\n");
        data.append("<tr><td align='left'>Average</td>");
        data.append("<td align='center'>" + nf2Dec.format(strengthAVG) + "</td>");
        data.append("<td align='center'>" + nf2Dec.format(qualityAVG) + "</td></tr>\n");
        data.append("<tr><td align='left'>Maximum</td>");
        data.append("<td align='center'>" + nf2Dec.format(strengthMAX) + "</td>");
        data.append("<td align='center'>" + nf2Dec.format(qualityMAX) + "</td></tr>\n");
        data.append("</table>\n");
        if (keys.length > 0) {
            data.append("<ul>\n");
            data.append("<li><a class='nounder' href='/servlet/SignalStatisticsImageDataRes?action=01&id=" + item.toString() + "&data=strength'>Show Signal Strength Graph</a></li>\n");
            data.append("<li><a class='nounder' href='/servlet/SignalStatisticsImageDataRes?action=01&id=" + item.toString() + "&data=quality'>Show Signal Quality Graph</a></li>\n");
            data.append("</ul>\n");
        }
        data.append("<p style='border: solid 1px #FFFFFF; padding: 3px; width: 100%;'>Schedule Log:</p>\n");
        String log = item.getLog();
        data.append("<pre class='log'>" + log + "</pre>");
        return data.toString();
    }
