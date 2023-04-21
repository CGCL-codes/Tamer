    public void setResourceContent(String uri, InputStream is, String contentType, String characterEncoding) throws IOException {
        if (debug == 1) System.out.println("LocalFileSystemStore.setResourceContent(" + uri + ")");
        File file = new File(root, uri);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        try {
            int read = -1;
            byte[] copyBuffer = new byte[BUF_SIZE];
            while ((read = is.read(copyBuffer, 0, copyBuffer.length)) != -1) {
                os.write(copyBuffer, 0, read);
            }
        } finally {
            try {
                is.close();
            } finally {
                os.close();
            }
        }
    }
