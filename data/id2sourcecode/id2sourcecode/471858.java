    private void uploadImage(URL[] files, SpitfireBase sfBase, URL endpoint, String parameter, Properties prop) throws RemoteException, IOException {
        for (int i = 0; i < files.length; i++) {
            String filename = FileUtils.getFilename(files[i]);
            String outputFile = System.currentTimeMillis() + "_" + filename;
            FileUtils.copyFile(files[i], new File(_api.getStorage() + "/" + outputFile));
            String outputURL = "file:" + _api.getStorage() + "/" + outputFile;
            int rows = sfBase.insert(database, table, new String[] { "name", "owner", "uploaded", "location", "keywords", "description" }, new String[] { filename, _api.getDescription().getInfo().getOwner(), new Timestamp(System.currentTimeMillis()).toString(), URLEncoder.encode(outputURL.toString(), "iso-8859-1"), prop.getProperty("KEYWORD"), parameter });
            if (rows > 0) _results.insertResult("succeeded", endpoint.getHost(), "inserted " + rows + " rows to database on " + endpoint.toString()); else _results.insertResult("failed", endpoint.getHost(), "no rows inserted to database on " + endpoint.toString());
        }
    }
