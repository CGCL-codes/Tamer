    public void write(String level, long thread_id, String description, String details) {
        try {
            DBLog dblog = new DBLog(connection);
            if (description == null) {
                description = "";
            }
            if (description.length() > 100) {
                description = description.substring(0, 100);
            }
            dblog.log_level.set(level);
            dblog.description.set(description);
            dblog.detail.set(details);
            dblog.logtime.set(new java.util.Date());
            dblog.save();
            connection.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
