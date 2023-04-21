    @Override
    public AudioClip getAudioClip(URL url) {
        if ("file".equalsIgnoreCase(url.getProtocol())) {
            this.checkPermission(new FilePermission(url.getFile(), "read"));
        } else {
            String hostport = url.getHost() + ':' + url.getPort();
            this.checkPermission(new SocketPermission(hostport, "connect"));
        }
        AudioClip c = (AudioClip) this.clips.get(url);
        if (c == null) {
            InputStream in = null;
            try {
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("user-agent", "Tahiti/Alpha5x");
                conn.setRequestProperty("agent-system", "aglets");
                conn.setAllowUserInteraction(true);
                conn.connect();
                in = conn.getInputStream();
                AudioData data = new AudioStream(in).getData();
                c = new AgletAudioClip(url, data);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                    }
                }
            }
            this.clips.put(url, c);
        }
        return c;
    }
