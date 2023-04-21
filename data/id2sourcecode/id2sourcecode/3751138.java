    public MusicInputStream(URL url, int offset, String mimeType, String srcMimeType) throws IOException {
        myurl = url;
        conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "JReceiver/@version@");
        conn.setRequestProperty("icy-metadata", "1");
        if (offset > 0) {
            conn.setRequestProperty("Range", "bytes=" + offset + "-");
        }
        conn.connect();
        locked = false;
        contentType = conn.getContentType();
        if ((contentType == null || contentType.equals("content/unknown")) && srcMimeType != null) contentType = srcMimeType;
        if (contentType != null && contentType.equals("video/x-ms-asf")) {
            conn = handleMSRedirect(conn);
            contentType = "audio/x-ms-wma";
        }
        ReadAheadInputStream originalStream = new ReadAheadInputStream(conn.getInputStream());
        s = originalStream;
        if (!s.markSupported()) {
            s = new BufferedInputStream(s);
        }
        metaInt = -1;
        metaData = new MusicMetaData();
        metaData.set("name", conn.getHeaderField("x-audiocast-name"));
        metaData.set("genre", conn.getHeaderField("x-audiocast-genre"));
        metaData.setIf("name", conn.getHeaderField("ice-name"));
        metaData.setIf("genre", conn.getHeaderField("ice-genre"));
        streamHasAnEnd = false;
        if (url.getProtocol().equals("file")) {
            streamHasAnEnd = true;
        }
        if (conn.getContentLength() > 0) {
            streamHasAnEnd = true;
        }
        if (conn instanceof HttpURLConnection) {
            HttpURLConnection hconn = (HttpURLConnection) conn;
            int responseCode = hconn.getResponseCode();
            s.mark(32);
            byte[] lbyte = new byte[16];
            int bytes_read = s.read(lbyte);
            String line;
            line = new String(lbyte, 0, bytes_read);
            s.reset();
            if (line != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreTokens() && st.nextToken().equals("ICY")) {
                    DataInputStream in = new DataInputStream(s);
                    try {
                        responseCode = Integer.decode(st.nextToken()).intValue();
                    } catch (Exception e) {
                    }
                    HashMap headers = new HashMap();
                    while ((line = in.readLine()) != null && line.length() > 0) {
                        int i = line.indexOf(':');
                        if (i > 0) {
                            headers.put(line.substring(0, i).trim().toLowerCase(), line.substring(i + 1).trim());
                        }
                    }
                    contentType = "audio/mpeg";
                    metaData.setIf("name", (String) headers.get("icy-name"));
                    metaData.setIf("genre", (String) headers.get("icy-genre"));
                    try {
                        metaInt = Integer.decode((String) headers.get("icy-metaint")).intValue();
                    } catch (Exception e) {
                        metaInt = -1;
                    }
                }
            }
            if (responseCode < 200 || responseCode >= 300) {
                throw new IOException("Failed to open HTTP connection");
            }
            if (metaInt > 0) {
                s = new BufferedInputStream(new MetaDataInputStream(s, metaInt, metaData));
            }
        }
        pos = 0;
        posBase = 0;
        posAtTick = 0;
        marked_pos = 0;
        posAtEnd = -1;
        setOffset(offset);
        if (mimeType != null && !mimeType.equals(contentType)) {
            InputStream ts = TranscoderInputStreamFactory.getTranscodedInputStream(s, contentType, mimeType);
            if (ts != s) {
                originalStream = new ReadAheadInputStream(ts);
                s = originalStream;
            }
        }
        if (!s.markSupported()) {
            s = new BufferedInputStream(s);
        }
        s.mark(MARK_BUFFER_SIZE);
        class MusicTimer extends TimerTask {

            private MusicInputStream stream;

            MusicTimer(MusicInputStream s) {
                stream = s;
            }

            public void run() {
                stream.tick();
            }
        }
        t = new Timer();
        t.schedule(new MusicTimer(this), 20000, 20000);
        createMPEGAudioFrameHeader();
        metaData.notifyObservers(this);
        originalStream.setBufferSize(200000);
        needsPrebuffer = true;
    }
