    public int prepareTable(Date day) {
        Date start = new Date(day.getTime());
        Date end = new Date(day.getTime() + 24 * 60 * 60 * 1000);
        Date now = new Date();
        boolean isToday = now.getYear() == day.getYear() && now.getMonth() == day.getMonth() && now.getDate() == day.getDate();
        containerPanel.clear();
        containerPanel.add(scheduleTable);
        DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.HOUR24_MINUTE);
        scheduleTable.removeAllRows();
        scheduleTable.setCornerWidget(new Label("Ora"));
        int i = 0;
        for (String channelCode : channelTree.getSelectedChannels()) {
            Channel channel = channelService.getChannelByCode(channelCode);
            scheduleTable.setRowHeaderWidget(i, new Label(channel.getName()));
            i++;
        }
        int j = 0;
        boolean selectionDone = false;
        int selectedRow = -1;
        Date currentDate = new Date(start.getTime());
        int module = 30 * 60 * 1000;
        while (currentDate.compareTo(end) < 0) {
            scheduleTable.setColumnHeaderWidget(j, new Label(format.format(currentDate)));
            String styleName;
            if (isToday && !selectionDone && (((now.getHours() - currentDate.getHours()) * 60) + (now.getMinutes() - currentDate.getMinutes())) <= 30) {
                selectionDone = true;
                styleName = style.nowrow();
                selectedRow = j;
            } else {
                styleName = j % 2 == 0 ? style.evenrow() : style.oddrow();
            }
            scheduleTable.getContentRowFormatter().addStyleName(j, styleName);
            scheduleTable.getHeaderColumnRowFormatter().addStyleName(j, styleName);
            currentDate = new Date(currentDate.getTime() + module);
            j++;
        }
        scheduleTable.setHeaderColumnWidth("50px");
        scheduleTable.layout();
        return selectedRow;
    }
