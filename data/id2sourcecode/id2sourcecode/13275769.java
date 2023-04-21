    public static long getSizeEstimate(InputStream is, ZipEntry z) {
        if (is instanceof FileInputStream) {
            try {
                return ((FileInputStream) is).getChannel().size();
            } catch (IOException e) {
            }
        }
        if (is instanceof FileInputStream) {
            if (z != null) return z.getSize();
        }
        try {
            return is.available();
        } catch (IOException e) {
            try {
                return is.available();
            } catch (IOException e1) {
                return -1;
            }
        }
    }
