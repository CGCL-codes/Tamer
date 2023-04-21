    public void write(String level, long thread_id, String description, String details) {
        if ("Message".equals(level)) {
            Utils.createDialog(parent, description, details, false).setVisible(true);
        }
        System.out.println(level + " [" + thread_id + "] " + date_format.format(new Date()) + "\n" + "TCE " + date_format.format(new Date()) + "\n" + "Description: " + description + "\n" + details + "\n" + "-------------------------------------");
        if (Logging.CRITICAL.toString().equals(level)) {
            new Exception("CURRENT STACK TRACE").printStackTrace();
        }
    }
