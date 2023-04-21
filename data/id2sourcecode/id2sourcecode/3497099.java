    public void writeFile(URL url, RecordModel recordModel, boolean writeChildRecords) throws IOException {
        URLConnection urlConnection = url.openConnection();
        OutputStream outputStream = urlConnection.getOutputStream();
        writeFile(outputStream, recordModel, writeChildRecords);
    }
