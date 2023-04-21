    public void sendShape(String s) {
        try {
            URLConnection uc = new URL(url + "&add=" + s).openConnection();
            InputStream in = uc.getInputStream();
            while (in.read() != -1) {
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
