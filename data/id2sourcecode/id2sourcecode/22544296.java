    private File downloadIntoTempFile(String filePathName) {
        try {
            File tempFile = File.createTempFile("refactorit", "cache");
            tempFile.deleteOnExit();
            URL url = new URL(filePathName);
            InputStream in = null;
            OutputStream out = null;
            tuneTimeouts();
            try {
                in = url.openStream();
                out = new FileOutputStream(tempFile);
                copy(in, out);
            } catch (Exception e) {
                System.err.println("missing doc: " + filePathName);
                tempFile = null;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                restoreTimeouts();
            }
            return tempFile;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
