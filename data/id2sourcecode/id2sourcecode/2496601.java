    private void storeOpstring() throws IOException {
        StringReader in = new StringReader(opstringBuffer.toString());
        BufferedWriter out = new BufferedWriter(new FileWriter(getFileLocation()));
        int readInt;
        while ((readInt = in.read()) != -1) {
            out.write(readInt);
        }
        in.close();
        out.flush();
        out.close();
    }
