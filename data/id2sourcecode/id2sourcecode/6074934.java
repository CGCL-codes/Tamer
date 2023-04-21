    public static File wGet(String url) {
        try {
            File ff = File.getTemp("jpg");
            InputStream tmp = (new URL(url)).openStream();
            Writer wr = ff.getWriter();
            BLK.System.Threads.Thread.sleepThread(5000);
            while (true) {
                if (tmp.available() > 0) {
                    wr.write(tmp.read());
                    wr.flush();
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Url.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Url.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
