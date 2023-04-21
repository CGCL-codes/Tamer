    private int writeFileFromInputStream(InputStream in, File file) throws IOException {
        int totalWrote = 0;
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            int bytes_read;
            byte[] buf = new byte[2048];
            while ((bytes_read = in.read(buf)) != -1) {
                out.write(buf, 0, bytes_read);
                totalWrote += bytes_read;
            }
        } catch (FileNotFoundException e) {
            file.delete();
            throw e;
        } catch (IOException e) {
            file.delete();
            throw e;
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (totalWrote);
    }
