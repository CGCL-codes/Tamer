    private String addSchedule(HTTPurl urlData, ScheduleItem item) throws Exception {
        int startBuff = 0;
        int endBuff = 0;
        try {
            startBuff = Integer.parseInt(store.getProperty("Schedule.buffer.start"));
            endBuff = Integer.parseInt(store.getProperty("Schedule.buffer.end"));
        } catch (Exception e) {
        }
        String addBuff = urlData.getParameter("buffer");
        String y = urlData.getParameter("year");
        String m = urlData.getParameter("month");
        String d = urlData.getParameter("day");
        String h = urlData.getParameter("hour");
        String mi = urlData.getParameter("min");
        String dur = urlData.getParameter("duration");
        String channel = urlData.getParameter("channel");
        String name = urlData.getParameter("name");
        String autoDel = urlData.getParameter("autoDel");
        String namePattern = urlData.getParameter("namePattern");
        String keepFor = urlData.getParameter("keepfor");
        String task = urlData.getParameter("task");
        HashMap channels = store.getChannels();
        if (!channels.containsKey(channel)) {
            throw new Exception("Channel Not Found!");
        }
        String[] namePatterns = store.getNamePatterns();
        if (namePattern == null || namePattern.length() == 0) namePattern = namePatterns[0];
        GuideStore guide = GuideStore.getInstance();
        boolean found = false;
        for (int x = 0; x < namePatterns.length; x++) {
            if (namePatterns[x].equals(namePattern)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Name Pattern Not Found!");
        }
        if (item != null) {
            store.removeScheduleItem(item.toString());
        }
        int duration = Integer.parseInt(dur);
        int type = 0;
        try {
            type = Integer.parseInt(urlData.getParameter("type"));
        } catch (Exception e01) {
        }
        int captype = 2;
        try {
            captype = Integer.parseInt(store.getProperty("Capture.deftype"));
        } catch (Exception e01) {
        }
        try {
            captype = Integer.parseInt(urlData.getParameter("captype"));
        } catch (Exception e01) {
        }
        Calendar newDate = Calendar.getInstance();
        newDate.set(Calendar.MILLISECOND, 0);
        newDate.set(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d), Integer.parseInt(h), Integer.parseInt(mi), 0);
        if ("yes".equals(addBuff)) {
            newDate.add(Calendar.MINUTE, (startBuff * -1));
            duration = duration + startBuff + endBuff;
        }
        if (item == null) item = new ScheduleItem(store.rand.nextLong());
        item.setCreatedFrom(null);
        item.setCapType(captype);
        item.setType(type);
        item.setName(name);
        item.setState(ScheduleItem.WAITING);
        item.setStatus("Waiting");
        item.resetAbort();
        item.setStart(newDate);
        item.setDuration(duration);
        int pathIndex = -1;
        try {
            pathIndex = Integer.parseInt(urlData.getParameter("path"));
        } catch (Exception e01) {
        }
        item.setCapturePathIndex(pathIndex);
        item.setChannel(channel);
        if ("true".equalsIgnoreCase(autoDel)) item.setAutoDeletable(true); else item.setAutoDeletable(false);
        item.setFilePattern(namePattern);
        if (keepFor != null) {
            int keepInt = 30;
            try {
                keepInt = Integer.parseInt(keepFor);
            } catch (Exception e) {
            }
            item.setKeepFor(keepInt);
        } else {
            keepFor = store.getProperty("AutoDel.KeepFor");
            int keepInt = 30;
            try {
                keepInt = Integer.parseInt(keepFor);
            } catch (Exception e) {
            }
            item.setKeepFor(keepInt);
        }
        if (task != null && !task.equalsIgnoreCase("none") && task.length() > 0) {
            HashMap tasks = store.getTaskList();
            if (tasks.containsKey(task)) item.setPostTask(task);
        } else if (task != null && task.equalsIgnoreCase("none")) {
            item.setPostTask("");
        }
        item.log("New Schedule added/edited");
        boolean isAlreadyInLIst = guide.isAlreadyInList(item, 1);
        if (isAlreadyInLIst) {
            return "Already In List";
        } else {
            store.addScheduleItem(item);
        }
        return null;
    }
