    private byte[] addTestSchedules(HTTPurl urlData) throws Exception {
        StringBuffer out = new StringBuffer(4096);
        int type = 1;
        int number = 1;
        int duration = 1;
        int gap = 1;
        try {
            type = Integer.parseInt(urlData.getParameter("type"));
        } catch (Exception e) {
        }
        try {
            number = Integer.parseInt(urlData.getParameter("number"));
        } catch (Exception e) {
        }
        try {
            duration = Integer.parseInt(urlData.getParameter("duration"));
        } catch (Exception e) {
        }
        try {
            gap = Integer.parseInt(urlData.getParameter("gap"));
        } catch (Exception e) {
        }
        Calendar start = Calendar.getInstance();
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.add(Calendar.MINUTE, 1);
        HashMap<String, Channel> channels = store.getChannels();
        String[] keys = (String[]) channels.keySet().toArray(new String[0]);
        for (int y = 0; y < number; y++) {
            for (int x = 0; x < keys.length; x++) {
                start.add(Calendar.MINUTE, duration + gap);
                ScheduleItem item = new ScheduleItem(store.rand.nextLong());
                item.setCapType(type);
                item.setType(ScheduleItem.ONCE);
                item.setState(ScheduleItem.WAITING);
                item.setStatus("Waiting");
                item.setStart(start);
                item.setDuration(duration);
                item.setChannel(keys[x]);
                item.setAutoDeletable(false);
                item.setPostTask("");
                String[] namePatterns = store.getNamePatterns();
                item.setFilePattern(namePatterns[0]);
                item.log("New TEST Schedule added/edited");
                store.addScheduleItem(item);
            }
        }
        out.append("HTTP/1.0 302 Moved Temporarily\n");
        out.append("Location: " + "/servlet/" + urlData.getServletClass() + "\n\n");
        return out.toString().getBytes();
    }
