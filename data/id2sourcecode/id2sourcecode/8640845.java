    public static String getContentAndReplaceInputStream(InputStream[] inArray, boolean convertToHex) {
        String content = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            InputStream in = inArray[0];
            byte[] buf = new byte[1024];
            while (true) {
                int read = in.read(buf, 0, buf.length);
                if (read < 0) break;
                out.write(buf, 0, read);
            }
            if (!(in instanceof SeekableInput)) in.close();
            out.flush();
            out.close();
            byte[] data = out.toByteArray();
            out = null;
            inArray[0] = new ByteArrayInputStream(data);
            if (convertToHex) content = Utils.convertByteArrayToHexString(data, true); else content = new String(data);
        } catch (IOException ioe) {
            logger.log(Level.FINE, "Problem getting debug string");
        } catch (Throwable e) {
            logger.log(Level.FINE, "Problem getting content stream, skipping");
        }
        return content;
    }
