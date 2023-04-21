    void extractDll(String name) {
        InputStream inputStream = OpenSongJ.class.getResourceAsStream(name);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(name);
            byte[] array = new byte[8192];
            for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) outputStream.write(array, 0, i);
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("Failed to export dll " + name);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to export dll " + name);
            e.printStackTrace();
        }
    }
