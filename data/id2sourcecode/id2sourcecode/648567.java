    private boolean execute() {
        if (!isConnected()) return false;
        ChannelMap cmap = new ChannelMap();
        int channelId;
        try {
            channelId = cmap.Add(rbnbChannelName);
        } catch (SAPIException e) {
            writeMessage("Error: Failed to add video channel to channel map; name = " + rbnbChannelName);
            disconnect();
            return false;
        }
        cmap.PutTimeAuto("timeofday");
        cmap.PutMime(channelId, "image/jpeg");
        URLConnection cameraConnection = getCameraConnection();
        if (cameraConnection == null) {
            writeMessage("Failed to get camera connection.");
            disconnect();
            return false;
        }
        String contentType = cameraConnection.getHeaderField("Content-Type");
        if (contentType == null) {
            writeMessage("Failed to find content type in stream.");
            disconnect();
            return false;
        }
        writeMessage("contentType :" + contentType);
        String[] fields = contentType.split(";");
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
            if (fields[i].toLowerCase().startsWith("boundary=")) {
                delimiter = fields[i].substring(9);
                break;
            }
        }
        if (delimiter.length() == 0) {
            writeMessage("Error: Failed to find delimiter.");
            disconnect();
            return false;
        }
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(cameraConnection.getInputStream());
        } catch (IOException e) {
            writeMessage("Error: Failed to get data stream from D-Link host.");
            disconnect();
            return false;
        }
        int imageLength = 0;
        boolean failed = false;
        long previousTimeStamp = -1;
        double averageFPS = -1;
        long images = 0;
        long timeStamp;
        double fps;
        while (true) {
            try {
                imageLength = getContentLength(dis);
            } catch (IOException eio) {
                writeMessage("Error: Failed to read header data: " + eio.getMessage());
                failed = true;
                break;
            }
            if (imageLength > 0) {
                try {
                    loadImage(dis, cmap, channelId, imageLength);
                } catch (IOException io) {
                    writeMessage("Error: Failed to load image: " + io.getMessage());
                    failed = true;
                    break;
                } catch (SAPIException es) {
                    writeMessage("Error: Failed to load data: " + es.getMessage());
                    failed = true;
                    break;
                }
                images++;
                timeStamp = System.currentTimeMillis();
                if (previousTimeStamp != -1 && timeStamp > previousTimeStamp) {
                    fps = 1000d / (timeStamp - previousTimeStamp);
                    if (averageFPS == -1) {
                        averageFPS = fps;
                    } else {
                        averageFPS = 0.95 * averageFPS + 0.05 * fps;
                    }
                    long roundedAverageFPS = Math.round(averageFPS);
                    if (images % roundedAverageFPS == 0) {
                        writeProgressMessage("Average frames per second: " + roundedAverageFPS);
                    }
                }
                previousTimeStamp = timeStamp;
            }
        }
        try {
            dis.close();
        } catch (IOException e) {
            writeMessage("Error: Failed to close connect to D-Link host.");
        }
        return !failed;
    }
