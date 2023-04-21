    public static String getContentFromSeekableInput(SeekableInput in, boolean convertToHex) {
        String content = null;
        try {
            long position = in.getAbsolutePosition();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (true) {
                int read = in.getInputStream().read();
                if (read < 0) break;
                out.write(read);
            }
            in.seekAbsolute(position);
            out.flush();
            out.close();
            byte[] data = out.toByteArray();
            if (convertToHex) content = Utils.convertByteArrayToHexString(data, true); else content = new String(data);
        } catch (IOException ioe) {
            logger.log(Level.FINE, "Problem getting debug string");
        }
        return content;
    }
