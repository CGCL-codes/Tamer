    public RenderNode(String host, int port, int clock, int threads) throws UnknownHostException, IOException, SceneParserException {
        this.clock = clock;
        this.threads = threads;
        Socket s = new Socket(host, port);
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        pw = new PrintWriter(os, true);
        File ts = new File(TEMP_SCENE_FILENAME);
        if (ts.exists()) if (!ts.delete()) {
            throw new IOException("Couldn't delete file: " + ts.getAbsolutePath());
        }
        FileOutputStream fos = new FileOutputStream(ts);
        int fileSize = 0x0;
        int f;
        while ((f = is.read()) != '\n') {
            fileSize *= 10;
            fileSize += f & 0xF;
        }
        for (int i = 0; i < fileSize; ++i) {
            fos.write(is.read());
        }
        br = new BufferedReader(new InputStreamReader(is));
        fos.close();
        pw.println(threads + "," + clock);
        scene = new SceneParser(ts).parseScene();
        String[] sp = br.readLine().split(",");
        pieces = new int[sp.length];
        for (int i = 0; i < pieces.length; ++i) {
            pieces[i] = Integer.valueOf(sp[i]);
        }
    }
