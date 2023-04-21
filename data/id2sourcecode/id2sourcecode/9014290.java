    public void service(String conName, final InputStream input, final OutputStream output) {
        try {
            ConnectionInfo info = ConnectionWaiter.getInstance().get(conName);
            if (info == null) {
                output.write("ERROR:SessionNotExist".getBytes());
                output.flush();
                return;
            }
            User user = new User(info.getName(), info.getUserid(), output);
            service(user, info.getChannel(), info.getRoomid(), info.getRoompw(), input);
        } catch (Exception e) {
            System.err.println("Exception : 93");
            e.printStackTrace();
        }
    }
