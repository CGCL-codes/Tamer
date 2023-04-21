    public void uploadFile() throws IOException {
        if (selectedDataSource != null) {
            int index = uploadFileName.lastIndexOf("\\");
            if (index < 0) {
                index = uploadFileName.lastIndexOf("/");
            }
            String name = uploadFileName.substring(index + 1);
            String path = selectedDataSource.getRootPath() + "/" + name;
            File file = new File(path);
            FileOutputStream out = null;
            try {
                byte[] buffer = new byte[1024];
                int read;
                out = new FileOutputStream(file);
                while ((read = uploadStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }
                out.flush();
            } catch (IOException e) {
                throw e;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            loadFiles();
        }
    }
