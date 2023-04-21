    public static String download(final String sURL, final String sFilename) throws IOException {
        final URL u = new URL(sURL);
        final URLConnection conn = u.openConnection();
        final InputStream is = conn.getInputStream();
        final byte[] buff = new byte[10240];
        int read;
        final OutputStream os;
        String actualFileName = sFilename;
        if (sFilename != null) {
            File f = new File(sFilename);
            if (f.exists() && f.isDirectory()) {
                String sLastPart = sURL;
                int idx = sLastPart.indexOf('?');
                if (idx >= 0) sLastPart = sLastPart.substring(0, idx);
                idx = sLastPart.lastIndexOf('/');
                if (idx >= 0) sLastPart = sLastPart.substring(idx + 1);
                f = new File(f, sLastPart);
                actualFileName = f.getCanonicalPath();
            }
            os = new FileOutputStream(f);
        } else {
            os = new ByteArrayOutputStream();
        }
        try {
            while ((read = is.read(buff)) > 0) {
                os.write(buff, 0, read);
            }
        } finally {
            os.close();
        }
        is.close();
        if (os instanceof ByteArrayOutputStream) {
            String encoding = conn.getContentEncoding();
            if (encoding == null) encoding = "UTF-8";
            final byte[] content = ((ByteArrayOutputStream) os).toByteArray();
            try {
                return new String(content, encoding);
            } catch (final UnsupportedEncodingException uee) {
                return new String(content);
            }
        }
        return actualFileName;
    }
