    private int getSchTblData(ScheduleItem[] itemsArray, StringBuffer content, boolean showOverlapStatus) throws Exception {
        Calendar dateFormater = Calendar.getInstance();
        String rowHi = "class='rowHi'";
        String rowLo = "class='rowLow'";
        for (int x = 0; x < itemsArray.length; x++) {
            ScheduleItem item = itemsArray[x];
            dateFormater.setTime(item.getStart());
            String type = "";
            if (item.getType() == ScheduleItem.ONCE) type = "Once"; else if (item.getType() == ScheduleItem.DAILY) type = "Daily"; else if (item.getType() == ScheduleItem.WEEKLY) type = "Weekly"; else if (item.getType() == ScheduleItem.MONTHLY) type = "Monthly"; else if (item.getType() == ScheduleItem.WEEKDAY) type = "Week Day"; else if (item.getType() == ScheduleItem.EPG) type = "EPG"; else type = "?" + item.getType() + "?";
            content.append("<tr ");
            if (x % 2 == 0) content.append(rowHi + " >"); else content.append(rowLo + " >");
            content.append("<td class='itemdata'>");
            Vector<String> warnings = item.getWarnings();
            if (warnings.size() > 0) {
                String waringText = "";
                for (int warnIndex = 0; warnIndex < warnings.size(); warnIndex++) {
                    waringText += " - " + warnings.get(warnIndex) + "\\n";
                }
                content.append("<img alt='Warning' title='This Schedule has Warnings' onClick=\"warningBox('" + waringText + "', '" + item.toString() + "');\" " + "src='/images/exclaim24.png' border='0' width='22' height='24' style='cursor: pointer; cursor: hand;''> ");
            }
            CaptureDeviceList devList = CaptureDeviceList.getInstance();
            if (showOverlapStatus) {
                int depth = overlapDepth(item);
                if (depth > devList.getDeviceCount()) content.append("<img alt='To many overlapping items (" + depth + ")' title='To many overlapping items (" + depth + ")' " + "src='/images/exclaim24.png' border='0' width='22' height='24'> "); else content.append("<img alt='(" + depth + ")' title='(" + depth + ")' " + "src='/images/tick.png' border='0' alt='Ok' width='24' height='24'> ");
            }
            content.append("<a href='/servlet/ScheduleDataRes?action=07&id=" + URLEncoder.encode(item.toString(), "UTF-8") + "'>");
            content.append("<img src='/images/log.png' border='0' alt='Schedule Log' width='24' height='24'></a> ");
            Calendar viewDate = Calendar.getInstance();
            viewDate.setTime(dateFormater.getTime());
            if (viewDate.get(Calendar.HOUR_OF_DAY) <= 6) viewDate.add(Calendar.DATE, -1);
            String egpUrl = "/servlet/EpgDataRes?action=12&year=" + viewDate.get(Calendar.YEAR) + "&month=" + (viewDate.get(Calendar.MONTH) + 1) + "&day=" + viewDate.get(Calendar.DATE) + "&scrollto=" + viewDate.get(Calendar.HOUR_OF_DAY);
            content.append("<a href='" + egpUrl + "'>");
            content.append("<img src='/images/epglink.png' border='0' alt='EPG Link' width='24' height='24'></a>\n");
            content.append("</td>\n");
            content.append("<td class='itemdata'>" + item.getName() + "</td>\n");
            int hour = dateFormater.get(Calendar.HOUR);
            if (hour == 0) hour = 12;
            String timeString = store.intToStr(hour) + ":" + store.intToStr(dateFormater.get(Calendar.MINUTE)) + " " + store.ampm.get(new Integer(dateFormater.get(Calendar.AM_PM)));
            content.append("<td class='itemdata'><b>" + timeString + "</b></td>\n");
            String dateString = store.dayName.get(new Integer(dateFormater.get(Calendar.DAY_OF_WEEK))) + ", " + dateFormater.get(Calendar.DATE) + " " + store.monthNameShort.get(new Integer(dateFormater.get(Calendar.MONTH)));
            content.append("<td class='itemdata'>" + dateString + "</td>\n");
            content.append("<td class='itemdata'>" + item.getDuration() + "min</td>\n");
            content.append("<td class='itemdata'>");
            content.append(item.getChannel());
            content.append("</td>\n");
            content.append("<td class='itemdata'>" + type + "</td>\n");
            content.append("<td class='itemdata'>" + item.getStatus() + "</td>\n");
            content.append("<td class='itemdata'>");
            if (item.getState() == ScheduleItem.WAITING || item.getState() == ScheduleItem.SKIPPED || item.getState() == ScheduleItem.FINISHED || item.getState() == ScheduleItem.ERROR) {
                content.append("<a onClick='return confirmAction(\"Delete\");' href='/servlet/ScheduleDataRes?action=04&id=" + URLEncoder.encode(item.toString(), "UTF-8") + "'>");
                content.append("<img src='/images/delete.png' border='0' alt='Delete' width='24' height='24'></a>\n");
                if (item.getType() != ScheduleItem.EPG) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(item.getStart());
                    content.append(" <a href='/servlet/ScheduleDataRes?action=01&id=" + URLEncoder.encode(item.toString(), "UTF-8") + "&month=" + cal.get(Calendar.MONTH) + "&year=" + cal.get(Calendar.YEAR) + "'>");
                    content.append("<img src='/images/edit.png' border='0' alt='Edit' width='24' height='24'></a>\n");
                    content.append("<a href='/servlet/ScheduleDataRes?action=05&id=" + URLEncoder.encode(item.toString(), "UTF-8") + "'>");
                    content.append("<img src='/images/+5.png' border='0' alt='Add Time' width='24' height='24'></a>\n");
                }
                if (item.getType() == ScheduleItem.DAILY || item.getType() == ScheduleItem.WEEKLY || item.getType() == ScheduleItem.MONTHLY || item.getType() == ScheduleItem.WEEKDAY) {
                    content.append("<a href='/servlet/ScheduleDataRes?action=06&id=" + URLEncoder.encode(item.toString(), "UTF-8") + "'>");
                    content.append("<img src='/images/skip.png' border='0' alt='Skip' width='24' height='24'></a>\n");
                }
            }
            if (item.getState() == ScheduleItem.RUNNING) {
                content.append("<a onClick='return confirmAction(\"Stop\");' href='/servlet/ScheduleDataRes?action=09&id=" + URLEncoder.encode(item.toString(), "UTF-8") + "'>");
                content.append("<img src='/images/stop.png' border='0' alt='Stop' width='24' height='24'></a>\n");
                content.append("<a href='/servlet/ScheduleDataRes?action=05&id=" + URLEncoder.encode(item.toString(), "UTF-8") + "'>");
                content.append("<img src='/images/+5.png' border='0' alt='Add Time' width='24' height='24'></a>\n");
            }
            content.append("</td>\n");
            content.append("</tr>\n");
        }
        return 0;
    }
