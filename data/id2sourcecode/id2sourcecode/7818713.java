    private File copyResourceFromTo(String from, String to) throws FileNotFoundException, IOException {
        InputStream streamToModiaf = this.getClass().getClassLoader().getResourceAsStream(from);
        if (streamToModiaf == null) throw new FileNotFoundException(from + " resource not found");
        File destination = new File(to);
        if (destination.isDirectory() && !destination.exists()) destination.mkdirs();
        FileOutputStream target = new FileOutputStream(destination);
        byte[] bytes = new byte[8000];
        int read = 0;
        do {
            read = streamToModiaf.read(bytes);
            if (read > 0) {
                target.write(bytes, 0, read);
            }
        } while (read != -1);
        target.close();
        streamToModiaf.close();
        return destination;
    }
