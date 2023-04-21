    private String saveRes(URL url) throws Exception {
        if (url == null) return "";
        File file = new File(this.getResPath() + url.getFile());
        int i = 0;
        while (file.exists()) {
            file = new File(this.getResPath() + "res" + i + url.getFile());
            i++;
        }
        file.createNewFile();
        BufferedInputStream input = new BufferedInputStream(url.openStream());
        if (input == null) return "";
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
        int read;
        while ((read = input.available()) != -1) {
            output.write(read);
            output.flush();
        }
        if (output != null) output.close();
        if (input != null) input.close();
        return "res" + "/" + file.getName();
    }
