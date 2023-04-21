    private boolean runHTTPCommand(final String theCommand) throws IOException {
        URL url = new URL(theCommand);
        URLConnection seleniumConnection = url.openConnection();
        seleniumConnection.connect();
        InputStream inputStream = seleniumConnection.getInputStream();
        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int streamLength;
        while ((streamLength = inputStream.read(buffer)) != -1) {
            outputSteam.write(buffer, 0, streamLength);
        }
        inputStream.close();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String stringifiedOutput = outputSteam.toString();
        if (stringifiedOutput.startsWith("OK")) return true;
        return false;
    }
