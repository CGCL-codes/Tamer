    private String exportImage(PropertyFile prop, File dir, String name) throws IOException {
        String suffix = null;
        if (prop.getValue().toLowerCase().indexOf("jpg") > 0) {
            suffix = ".jpg";
        }
        if (prop.getValue().toLowerCase().indexOf("gif") > 0) {
            suffix = ".gif";
        }
        if (prop.getValue().toLowerCase().indexOf("png") > 0) {
            suffix = ".png";
        }
        if (suffix == null) {
            return "";
        }
        OutputStream imgOut = null;
        InputStream imgIn = null;
        try {
            imgOut = new FileOutputStream(new File(dir, name + suffix));
            imgIn = prop.getInputStream();
            while (true) {
                int read = imgIn.read(imgBuffer);
                if (read <= 0) {
                    break;
                }
                imgOut.write(imgBuffer, 0, read);
            }
        } finally {
            try {
                imgOut.close();
            } catch (Exception e) {
            }
            ;
            try {
                imgIn.close();
            } catch (Exception e) {
            }
            ;
        }
        return name + suffix;
    }
