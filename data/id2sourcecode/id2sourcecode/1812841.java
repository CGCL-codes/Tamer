    private void init(FileInputStream is) {
        fileInputStream = is;
        fileChannel = is.getChannel();
        try {
            fileSize = fileChannel.size();
        } catch (IOException e) {
            e.printStackTrace();
            fileSize = 0;
        }
        boolean openok = false;
        size = fileSize / motionData.getLoggedObjectSize();
        dataInputStream = new DataInputStream(Channels.newInputStream(fileChannel));
        getSupport().firePropertyChange("position", 0, position());
        position(1);
    }
