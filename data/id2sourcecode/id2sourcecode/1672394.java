    private Night readNight(String file) throws IOException {
        URL url = getClass().getResource("device/" + file);
        BufferedInputStream stream = new BufferedInputStream(url.openStream());
        Night night = Device.readNight(stream, 2009);
        stream.close();
        return night;
    }
