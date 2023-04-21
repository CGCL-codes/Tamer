    public String importTable(freebaseTable file) {
        int fileSizeInBytes = 0;
        int bytesRecieved = 0;
        int packetSize = 0;
        String row;
        Connection conn = PhraseConnector.getInstance().openConnection();
        try {
            conn.setAutoCommit(false);
            URL url = new URL(file.fileName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int result = connection.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                fileSizeInBytes = connection.getContentLength();
                byte[] data = new byte[fileSizeInBytes];
                InputStream input = connection.getInputStream();
                state = ImportState.Downloading;
                while (bytesRecieved < fileSizeInBytes) {
                    bytesRecieved += input.read(data, bytesRecieved, fileSizeInBytes - bytesRecieved);
                }
                input.close();
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
                bytesRecieved = 0;
                state = ImportState.UpdatingDB;
                Statement statement = conn.createStatement();
                bufferReader.readLine();
                do {
                    row = bufferReader.readLine();
                    if (row == null) break;
                    if (!row.replaceAll(" ", "").equals("")) {
                        String[] rowData = proccessLine(file, row);
                        statement.addBatch(buildCommand(rowData));
                        packetSize++;
                        if (packetSize == 100 && statement != null) {
                            try {
                                statement.executeBatch();
                                conn.commit();
                                statement.clearBatch();
                                packetSize = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    bytesRecieved += row.length();
                } while (row != null);
                bufferReader.close();
                if (statement != null) {
                    statement.executeBatch();
                    conn.commit();
                    statement.clearBatch();
                    packetSize = 0;
                }
            } else {
                return "Import " + file.categoryName + " failed- Error Downloading File " + file.fileName + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Import " + file.categoryName + " failed- Error executing commands :" + e.toString() + "\n";
        } finally {
            PhraseConnector.getInstance().closeConnection(conn);
        }
        return "Import Category successed: " + file.categoryName + "\n";
    }
