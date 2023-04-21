    public static void convertObjToJme(URL url) throws IOException {
        if (url == null) {
            System.err.println("url is null");
            return;
        }
        FormatConverter converter = new ObjToJme();
        converter.setProperty("mtllib", url);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        converter.convert(url.openStream(), outStream);
        URL newFile = new URL(url.toString().replaceAll(".obj", ".jme"));
        FileOutputStream fileStream = new FileOutputStream(newFile.toString());
        outStream.writeTo(fileStream);
    }
