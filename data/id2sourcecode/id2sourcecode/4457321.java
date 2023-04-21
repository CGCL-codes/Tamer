    public String transferStreamData(java.io.InputStream in, java.io.OutputStream out) throws java.io.IOException {
        try {
            long inputOffset = 0;
            long inputLen = -1;
            int bytesRead;
            int transferred = 0;
            byte[] buf = new byte[4096];
            if (inputOffset > 0) if (in.skip(inputOffset) != inputOffset) throw new IOException("Input skip failed (offset " + inputOffset + ")");
            while (true) {
                if (inputLen >= 0) bytesRead = in.read(buf, 0, (int) Math.min(buf.length, inputLen - transferred)); else bytesRead = in.read(buf);
                if (bytesRead <= 0) break;
                if (md != null) {
                    if (bytesRead == buf.length) md.update(buf); else md.update(Arrays.copyOf(buf, bytesRead));
                }
                out.write(buf, 0, bytesRead);
                transferred += bytesRead;
                if (inputLen >= 0 && transferred >= inputLen) break;
            }
            out.flush();
            return md != null ? Util.encodeHexStr(md.digest()) : null;
        } finally {
            if (md != null) md.reset();
        }
    }
