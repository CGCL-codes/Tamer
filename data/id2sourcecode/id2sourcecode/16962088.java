    private byte[] showAddForm(HTTPurl urlData) throws Exception {
        int day = -1;
        int month = -1;
        int year = -1;
        try {
            day = Integer.parseInt(urlData.getParameter("day"));
            month = Integer.parseInt(urlData.getParameter("month"));
            year = Integer.parseInt(urlData.getParameter("year"));
        } catch (Exception e) {
        }
        ScheduleItem item = null;
        String id = urlData.getParameter("id");
        if (id != null && id.length() > 0) {
            item = store.getScheduleItem(id);
        }
        if (item != null && (item.getState() != ScheduleItem.FINISHED && item.getState() != ScheduleItem.WAITING && item.getState() != ScheduleItem.SKIPPED && item.getState() != ScheduleItem.ERROR)) {
            StringBuffer out = new StringBuffer();
            out.append("HTTP/1.0 302 Moved Temporarily\n");
            out.append("Location: /servlet/ScheduleDataRes\n\n");
            return out.toString().getBytes();
        }
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "itemdetails.html");
        if (item != null) template.replaceAll("$duration", new Integer(item.getDuration()).toString()); else template.replaceAll("$duration", "1");
        if (item != null) template.replaceAll("$name", item.getName()); else template.replaceAll("$name", "");
        Calendar cal = Calendar.getInstance();
        if (item != null) cal.setTime(item.getStart());
        template.replaceAll("$hour", store.intToStr(cal.get(Calendar.HOUR_OF_DAY)));
        template.replaceAll("$min", store.intToStr(cal.get(Calendar.MINUTE)));
        template.replaceAll("$channels", getChannelList(item));
        template.replaceAll("$item_type", getTypeList(item));
        template.replaceAll("$item_captype", getCapTypeList(item));
        String fields = "";
        fields += "<input type='hidden' name='day' value='" + day + "'>\n";
        fields += "<input type='hidden' name='month' value='" + month + "'>\n";
        fields += "<input type='hidden' name='year' value='" + year + "'>\n";
        if (item != null) {
            fields += "<input name='id' type='hidden' id='id' value='" + id + "'>\n";
        }
        template.replaceAll("$fields", fields);
        if (item != null && item.isAutoDeletable()) template.replaceAll("$adtrue", "checked"); else template.replaceAll("$adtrue", "");
        template.replaceAll("$pattern", getNamePatternList(item));
        if (item == null) {
            String defKeepFor = store.getProperty("autodel.keepfor");
            if (defKeepFor == null) defKeepFor = "30";
            template.replaceAll("$keepfor", defKeepFor);
        } else {
            template.replaceAll("$keepfor", new Integer(item.getKeepFor()).toString());
        }
        template.replaceAll("$tasks", getTaskList(item));
        template.replaceAll("$CapturePaths", getCapturePathList(item));
        return template.getPageBytes();
    }
