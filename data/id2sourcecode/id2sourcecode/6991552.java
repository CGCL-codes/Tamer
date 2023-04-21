    private ScheduleItem getSchFromXML(String data) throws Exception {
        if (data == null || data.length() == 0) throw new Exception("Post data is missing");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(data.getBytes()));
        NodeList schedule = document.getElementsByTagName("schedule");
        if (schedule.getLength() == 0) throw new Exception("No schedule entry in the xml");
        String id = null;
        String name = null;
        String duration = null;
        String channel = null;
        String type = null;
        String path = null;
        String filename_pattern = null;
        String capture_type = null;
        String post_task = null;
        String auto_delete = null;
        String keep_for = null;
        String startYear = null;
        String startMonth = null;
        String startDay = null;
        String startHour = null;
        String startMinute = null;
        try {
            id = schedule.item(0).getAttributes().getNamedItem("id").getNodeValue();
            name = getNodeString(schedule.item(0), "name");
            if (name == null) name = "";
            duration = getNodeString(schedule.item(0), "duration");
            channel = getNodeString(schedule.item(0), "channel");
            type = getNodeString(schedule.item(0), "type");
            path = getNodeString(schedule.item(0), "path");
            filename_pattern = getNodeString(schedule.item(0), "filename_pattern");
            capture_type = getNodeString(schedule.item(0), "capture_type");
            post_task = getNodeString(schedule.item(0), "post_task");
            if (post_task == null) post_task = "";
            auto_delete = getNodeString(schedule.item(0), "auto_delete");
            keep_for = getNodeString(schedule.item(0), "keep_for");
            Node times = getChildByTag(schedule.item(0), "times");
            Node start = getChildByTag(times, "start");
            startYear = start.getAttributes().getNamedItem("year").getNodeValue();
            startMonth = start.getAttributes().getNamedItem("month").getNodeValue();
            startDay = start.getAttributes().getNamedItem("day").getNodeValue();
            startHour = start.getAttributes().getNamedItem("hour").getNodeValue();
            startMinute = start.getAttributes().getNamedItem("minute").getNodeValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing XML (" + e.toString() + ")");
        }
        HashMap<String, Channel> channels = store.getChannels();
        if (!channels.containsKey(channel)) {
            throw new Exception("Channel Not Found");
        }
        String[] namePatterns = store.getNamePatterns();
        boolean patternFound = false;
        for (int x = 0; x < namePatterns.length; x++) {
            if (namePatterns[x].equals(filename_pattern)) {
                patternFound = true;
                break;
            }
        }
        if (patternFound == false) {
            throw new Exception("Filename pattern not found!");
        }
        int iDuration = -1;
        try {
            iDuration = Integer.parseInt(duration);
        } catch (Exception e) {
            throw new Exception("Duration not valid!");
        }
        int iType = -1;
        try {
            iType = Integer.parseInt(type);
        } catch (Exception e) {
            throw new Exception("Schedule Type not valid!");
        }
        int iCapType = -1;
        try {
            iCapType = Integer.parseInt(capture_type);
        } catch (Exception e) {
            throw new Exception("Capture Type not valid!");
        }
        int iPathIndex = -1;
        try {
            iPathIndex = Integer.parseInt(path);
        } catch (Exception e01) {
            throw new Exception("Path not valid!");
        }
        int iKeepFor = 30;
        try {
            iKeepFor = Integer.parseInt(keep_for);
        } catch (Exception e) {
            throw new Exception("Keep for not valid!");
        }
        if (post_task.length() > 0) {
            HashMap<String, TaskCommand> tasks = store.getTaskList();
            if (tasks.containsKey(post_task) == false) {
                throw new Exception("Post Task not valid!");
            }
        }
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.MILLISECOND, 0);
        try {
            startTime.set(Integer.parseInt(startYear), Integer.parseInt(startMonth) - 1, Integer.parseInt(startDay), Integer.parseInt(startHour), Integer.parseInt(startMinute), 0);
        } catch (Exception e) {
            throw new Exception("Start Time/Date not valid!");
        }
        boolean replace = false;
        ScheduleItem item = null;
        if (id.length() > 0) {
            item = store.getScheduleItem(id);
            replace = true;
            if (item == null) {
                throw new Exception("Schedule ID (" + id + ") not found");
            }
            if (item.getState() != ScheduleItem.WAITING && item.getState() != ScheduleItem.FINISHED && item.getState() != ScheduleItem.SKIPPED && item.getState() != ScheduleItem.ERROR) {
                throw new Exception("Can only edit schedules in the the (WAITING|FINISHED|SKIPPED|ERROR) states");
            }
        } else {
            item = new ScheduleItem(store.rand.nextLong());
        }
        item.setCreatedFrom(null);
        item.setCapType(iCapType);
        item.setType(iType);
        item.setName(name);
        item.setState(ScheduleItem.WAITING);
        item.setStatus("Waiting");
        item.resetAbort();
        item.setStart(startTime);
        item.setDuration(iDuration);
        item.setCapturePathIndex(iPathIndex);
        item.setChannel(channel);
        item.setFilePattern(filename_pattern);
        if ("1".equals(auto_delete)) item.setAutoDeletable(true); else item.setAutoDeletable(false);
        item.setKeepFor(iKeepFor);
        item.setPostTask(post_task);
        item.log("New Schedule added/edited");
        if (replace == true) {
            store.removeScheduleItem(item.toString());
        }
        GuideStore guide = GuideStore.getInstance();
        boolean isAlreadyInLIst = guide.isAlreadyInList(item, 1);
        if (isAlreadyInLIst) {
            throw new Exception("A schedule with matching details already exists");
        } else {
            store.addScheduleItem(item);
        }
        return item;
    }
