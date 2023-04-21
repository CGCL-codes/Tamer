    private void getParameter() {
        app = parent.getChoosenApp();
        taskname = parent.getTaskname();
        channel = parent.getChannel();
        winuserpassword = parent.getWinUserpassword();
        start = parent.getStartDateTime();
        stop = parent.getStopDateTime();
        start.add(Calendar.MONTH, 1);
        stop.add(Calendar.MONTH, 1);
    }
