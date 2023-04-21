    private int returnFileContent(String fileName) throws Exception {
        File thisFile = new File(fileName);
        String requestedFilePath = thisFile.getCanonicalPath();
        File root = new File(store.getProperty("path.httproot"));
        String rootFilePath = root.getCanonicalPath();
        if (requestedFilePath.indexOf(rootFilePath) < 0) {
            throw new Exception("File out of bounds!");
        }
        FileInputStream fi = null;
        fi = new FileInputStream(thisFile);
        long fileLength = thisFile.length();
        int read = 0;
        byte[] bytes = new byte[4096];
        read = fi.read(bytes);
        try {
            String header = "";
            header += "HTTP/1.0 200 OK\n";
            header += "Content-Length: " + fileLength + "\n";
            if (fileName.indexOf(".htc") > -1) header += "Content-Type: text/plain\n"; else if (fileName.indexOf(".html") > -1) header += "Content-Type: text/html\n";
            header += "\n";
            outStream.write(header.getBytes());
            while (read > -1) {
                outStream.write(bytes, 0, read);
                read = fi.read(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
            } catch (Exception e2) {
            }
        }
        return 1;
    }
