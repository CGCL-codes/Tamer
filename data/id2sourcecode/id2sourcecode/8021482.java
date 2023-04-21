        static void pipe(final InputStream in, final OutputStream out, final boolean isBlocking) throws IOException {
            final byte[] buf = new byte[65500];
            int nread;
            int navailable;
            int total = 0;
            synchronized (in) {
                while (((navailable = isBlocking ? Integer.MAX_VALUE : in.available()) > 0) && ((nread = in.read(buf, 0, Math.min(buf.length, navailable))) >= 0)) {
                    out.write(buf, 0, nread);
                    total += nread;
                }
            }
            out.flush();
        }
