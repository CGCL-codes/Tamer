    public void actionPerformed(ActionEvent e) {
        SocketClient sc = SocketClient.getInstance();
        Classroom c = Application.getWindow().getView(Classroom.class);
        int[] array = c.getSelectedRows();
        if (array == null || array.length <= 0) {
            return;
        }
        int index = array[0];
        Classmate cm = c.getModel().get(index);
        if (cm.getState().getRole() == PeterHi.ROLE_STUDENT) {
            LeaveChannelMessage message = new LeaveChannelMessage();
            message.channel = Application.getChannel();
            message.id = cm.getId();
            try {
                sc.send(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
