    private String saveRes(String path, String url1) throws Exception {
        URL url = null;
        try {
            url = new URL(url1);
        } catch (MalformedURLException ex) {
            logger.debug("ץȡ��Դʧ��:" + url1);
        }
        if (url == null) return "";
        BufferedInputStream input = null;
        try {
            input = new BufferedInputStream(url.openStream());
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        if (input == null) return "";
        File file = new File(this.getResPath() + this.getSingleFileName(url));
        if (file.exists()) return file.getName();
        try {
            file.createNewFile();
        } catch (IOException ex2) {
            System.out.println(file.getPath() + "   " + file.getName());
            ex2.printStackTrace();
        }
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
        int read;
        while ((read = input.read()) != -1) {
            output.write(read);
            output.flush();
        }
        if (output != null) output.close();
        if (input != null) input.close();
        return file.getName();
    }
