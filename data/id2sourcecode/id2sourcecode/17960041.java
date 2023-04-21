    private int writeFileFromInputStream(InputStream newIn, File file) throws FileNotFoundException, IOException {
        int totalWrote = 0;
        InputStream in = newIn;
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            int read = 0;
            byte[] buf = new byte[2048];
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
                totalWrote += read;
            }
        } catch (FileNotFoundException e) {
            file.delete();
            throw e;
        } catch (IOException e) {
            file.delete();
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return (totalWrote);
    }
