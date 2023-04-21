    public byte[] getFile(String file, boolean try2) {
        URLConnection urlConn;
        URL url;
        try {
            url = new URL(file);
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            int contentLength = urlConn.getContentLength();
            InputStream raw = urlConn.getInputStream();
            BufferedInputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int bytesRead = 0;
            int offset = 0;
            while (offset < contentLength) {
                bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) break;
                offset += bytesRead;
            }
            in.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            if (try2) {
                JOptionPane.showMessageDialog(null, "Unable to get file from server " + file + "!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Try fetching again!");
                byte[] trie2 = getFile(file, true);
                return trie2;
            }
        }
        return null;
    }
