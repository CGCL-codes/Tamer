    public AppletAudioClip(URL url) {
        InputStream in = null;
        try {
            try {
                URLConnection uc = url.openConnection();
                uc.setAllowUserInteraction(true);
                in = uc.getInputStream();
                data = new AudioStream(in).getData();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            data = null;
        }
    }
