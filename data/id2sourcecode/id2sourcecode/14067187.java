    public void initActivities() throws StoreException {
        map = new HashMap<String, IEventInfo>();
        URLConnection connection;
        try {
            connection = url.openConnection();
            Iterator<IEventInfo> it = new CalendarImporter().importCalendar(id, connection.getInputStream());
            while (it.hasNext()) {
                IEventInfo eventinfo = it.next();
                map.put(eventinfo.getId(), eventinfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.initActivities();
    }
