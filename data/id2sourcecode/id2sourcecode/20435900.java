    public void replace(int[] froms, String to, String outputFileName) {
        InputStream in = new EnhancedFile(m_fileName).getInputStream();
        OutputStream out = new EnhancedFile(outputFileName).getOutputStream();
        int fromIndex = 0;
        byte[] readNotCommit = new byte[froms.length];
        int currentRead;
        try {
            while ((currentRead = in.read()) != -1) {
                if (currentRead == froms[fromIndex]) {
                    fromIndex++;
                    if (fromIndex == froms.length) {
                        out.write(new EnhancedString(to).getBytes());
                        fromIndex = 0;
                    } else {
                        readNotCommit[fromIndex - 1] = (byte) currentRead;
                    }
                } else {
                    if (fromIndex > 0) {
                        out.write(readNotCommit, 0, fromIndex);
                        fromIndex = 0;
                    }
                    out.write(currentRead);
                }
            }
            in.close();
            out.close();
        } catch (IOException e) {
            throw new DmException(e, "error copy from " + m_fileName + " to " + outputFileName);
        }
    }
