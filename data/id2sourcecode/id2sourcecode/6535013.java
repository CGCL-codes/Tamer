    public static synchronized String uploadPicture(InputStream in, String fileName, long length, final ProgressListener listener) throws IOException {
        if (!fileNameHasExtension(fileName)) {
            fileName = fileName + ".png";
        }
        String nameWithoutExt, extension;
        int num = 0;
        int dotIndex = fileName.lastIndexOf('.');
        nameWithoutExt = fileName.substring(0, dotIndex);
        extension = fileName.substring(dotIndex);
        String completeName = nameWithoutExt + extension;
        while (existsFile(client, completeName)) {
            completeName = nameWithoutExt + (++num) + extension;
        }
        if (!connect()) {
            return null;
        }
        client.cd("Bilder");
        if (listener != null) {
            listener.progressStarted(100);
        }
        final double incrementStep = (double) length / 409600;
        OutputStream out = client.put(completeName);
        byte c[] = new byte[4096];
        int read;
        long loopCounter = 0;
        double progress = incrementStep;
        while ((read = in.read(c)) != -1) {
            out.write(c, 0, read);
            if (++loopCounter > progress) {
                progress += incrementStep;
                if (listener != null) {
                    listener.progressIncrease();
                }
            }
        }
        in.close();
        out.close();
        if (listener != null) {
            listener.progressEnded();
        }
        return "file:///var/InternerSpeicher/Bilder/" + completeName;
    }
