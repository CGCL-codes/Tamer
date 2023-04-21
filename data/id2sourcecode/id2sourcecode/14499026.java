    public static final Image loadImage(final String innerFileName, final boolean isInner) {
        if (innerFileName == null) {
            return null;
        }
        String tmp_file = innerFileName, innerName = StringUtils.replaceIgnoreCase(innerFileName, "\\", "/");
        String keyName = innerName.toLowerCase();
        Object imageReference = cacheImages.get(keyName);
        if (imageReference == null) {
            int read;
            boolean flag;
            byte[] bytes = null;
            File file_tmp = null;
            Image img_tmp = null;
            InputStream in = null;
            ByteArrayOutputStream os = null;
            try {
                os = new ByteArrayOutputStream(8192);
                if (isInner) {
                    in = new DataInputStream(new BufferedInputStream(Resources.openResource(innerName)));
                    flag = true;
                } else {
                    file_tmp = new File(tmp_file);
                    flag = file_tmp.exists();
                    if (flag) {
                        in = new DataInputStream(new BufferedInputStream(new FileInputStream(file_tmp)));
                    }
                }
                if (flag) {
                    bytes = new byte[8192];
                    while ((read = in.read(bytes)) >= 0) {
                        os.write(bytes, 0, read);
                    }
                    bytes = os.toByteArray();
                    img_tmp = toolKit.createImage(bytes);
                }
                cacheImages.put(keyName, imageReference = img_tmp);
                waitImage(img_tmp);
            } catch (Exception e) {
                if (!isInner) {
                    imageReference = null;
                } else {
                    return loadImage(innerFileName, false);
                }
            } finally {
                try {
                    if (os != null) {
                        os.flush();
                        os = null;
                    }
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                    img_tmp = null;
                    bytes = null;
                    tmp_file = null;
                    file_tmp = null;
                } catch (IOException e) {
                }
            }
        }
        if (imageReference == null) {
            throw new RuntimeException(("File not found. ( " + innerName + " )").intern());
        }
        return (Image) imageReference;
    }
