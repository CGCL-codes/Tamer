    private void init(FileInputStream fileInputStream) {
        this.in = fileInputStream;
        fileChannel = fileInputStream.getChannel();
        try {
            fileSize = fileChannel.size();
        } catch (IOException e) {
            e.printStackTrace();
            fileSize = 0;
        }
        mostRecentTimestamp = 0;
        currentStartTimestamp = 0;
        boolean openok = false;
        System.gc();
        try {
            mapChunk(0);
        } catch (IOException e) {
            log.warning("couldn't map chunk 0 of file");
            e.printStackTrace();
        }
        reader = new InputStreamReader(fileInputStream);
        try {
            readHeader();
        } catch (IOException e) {
            log.warning("couldn't read header");
        }
        int type = readType();
        if (type == Event3D.DIRECT3D) pure3D = true; else pure3D = false;
        packet = new AEPacket3D(MAX_BUFFER_SIZE_EVENTS, type);
        try {
            Event3D ev = readEventForwards();
            firstTimestamp = ev.timestamp;
            position((int) (size() - 1));
            ev = readEventForwards();
            lastTimestamp = ev.timestamp;
            position(0);
            currentStartTimestamp = firstTimestamp;
            mostRecentTimestamp = firstTimestamp;
        } catch (IOException e) {
            System.err.println("couldn't read first event to set starting timestamp");
        } catch (NonMonotonicTimeException e2) {
            log.warning("On AEInputStream.init() caught " + e2.toString());
        }
        log.info(this.toString());
    }
