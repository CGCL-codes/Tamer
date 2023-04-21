    private File getStartupCurrentDirectory() {
        File toReturn = new File("data");
        try {
            InputStream inputStream = new FileInputStream(new File(STATIC_FOLDER, STARTUP_DIRECTORY_FILENAME));
            toReturn = new File(new String(IOHelper.inputStreamToByteArray(inputStream)));
            inputStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return toReturn;
    }
