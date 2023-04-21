    public synchronized Object put(Object key, Object value) {
        File file = null;
        BufferedOutputStream out = null;
        InputStream in = null;
        try {
            String filename = (String) key;
            file = new File(rootDirectory, filename);
            out = new BufferedOutputStream(new FileOutputStream(file));
            in = (InputStream) value;
            int i;
            while ((i = in.read()) > -1) out.write(i);
        } catch (IOException ioex) {
            try {
                out.close();
            } catch (Exception ex) {
            }
            try {
                file.delete();
            } catch (Exception ex) {
            }
            throw new RuntimeException(ioex.getMessage());
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
            try {
                out.close();
            } catch (Exception ex) {
            }
        }
        return null;
    }
