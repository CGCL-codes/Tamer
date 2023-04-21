    public void downloadFile(String destinationPath) throws FileNotFoundException {
        InputStream reader = null;
        FileOutputStream writeFile = null;
        try {
            File fpath = new File(destinationPath).getCanonicalFile();
            destinationPath = fpath.getPath();
            File dpath = new File(fpath.getParent());
            dpath.mkdirs();
            reader = performRequest();
            writeFile = new FileOutputStream(destinationPath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = reader.read(buffer)) > 0) {
                writeFile.write(buffer, 0, read);
                Broadcaster.fireDownloadedBytes(new DownloadedBytesEvent(read));
            }
            writeFile.flush();
        } catch (FileNotFoundException e1) {
            throw e1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
            try {
                writeFile.close();
            } catch (Exception e) {
            }
        }
    }
