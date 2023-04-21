    public void writeFile(URL url) throws IOException, FileNotFoundException {
        BufferedReader readBuffer = new BufferedReader(new InputStreamReader(url.openStream()));
        char buffer[] = new char[512 * 1024];
        int readNumber;
        while ((readNumber = readBuffer.read(buffer)) != -1) {
            writer.write(buffer, 0, readNumber);
        }
        readBuffer.close();
    }
