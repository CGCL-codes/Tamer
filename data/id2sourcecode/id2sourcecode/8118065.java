    public InputStream retriveData(String fileName) {
        InputStream isDown = null;
        try {
            URL urlDown = new URL(SysPath + "/" + fileName);
            isDown = urlDown.openStream();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isDown;
    }
