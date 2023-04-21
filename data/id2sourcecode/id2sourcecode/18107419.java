    public void sendImage(final BufferedImage image, final ChatRoom room) {
        File tmpDirectory = new File(Spark.getSparkUserHome(), "/tempImages");
        tmpDirectory.mkdirs();
        String imageName = "image_" + StringUtils.randomString(2) + ".png";
        final File imageFile = new File(tmpDirectory, imageName);
        room.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        SwingWorker writeImageThread = new SwingWorker() {

            public Object construct() {
                try {
                    ImageIO.write(image, "png", imageFile);
                } catch (IOException e) {
                    Log.error(e);
                }
                return true;
            }

            public void finished() {
                ChatRoomImpl roomImpl = (ChatRoomImpl) room;
                sendFile(imageFile, roomImpl.getParticipantJID());
                SparkManager.getChatManager().getChatContainer().activateChatRoom(room);
                room.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        writeImageThread.start();
    }
