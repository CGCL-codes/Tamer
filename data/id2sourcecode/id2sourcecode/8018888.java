    public void getOpennapServerList() {
        try {
            URL url = new URL("http://www.gotnap.com/servers.wsx");
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(stream);
            FileOutputStream file = new FileOutputStream("servers.wsx");
            BufferedOutputStream out = new BufferedOutputStream(file);
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
            out.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
