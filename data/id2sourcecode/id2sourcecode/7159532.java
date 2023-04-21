    public void loadImages() throws Exception {
        ZipEntry entry = _zis.getNextEntry();
        String name;
        BMPLoader bmp = new BMPLoader();
        int pos;
        while (entry != null) {
            name = entry.getName().toLowerCase();
            pos = name.lastIndexOf("/");
            if (pos != -1) name = name.substring(pos + 1);
            if (name.endsWith("bmp")) {
                _images.put(name, bmp.getBMPImage(_zis));
            } else if (name.endsWith("txt")) {
                InputStreamReader reader = new InputStreamReader(_zis, "US-ASCII");
                StringWriter writer = new StringWriter();
                char buffer[] = new char[256];
                int charsRead;
                while ((charsRead = reader.read(buffer)) != -1) writer.write(buffer, 0, charsRead);
                _images.put(name, writer.toString());
            }
            entry = _zis.getNextEntry();
        }
        _zis.close();
    }
