    public static boolean copyStreams(InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) > 0) out.write(buffer, 0, count);
            return true;
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                if (out != null) out.flush();
            } catch (IOException ex) {
            }
            try {
                if (out != null) out.close();
            } catch (IOException ex) {
            }
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
            }
        }
    }
