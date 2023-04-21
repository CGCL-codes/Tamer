    private void loadOpstring() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(getFileLocation()));
        StringWriter out = new StringWriter();
        String readString;
        while ((readString = in.readLine()) != null) {
            out.write(readString);
        }
        in.close();
        opstringBuffer = out.getBuffer();
    }
