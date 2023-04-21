    byte[] download(URL url) {
        byte[] data;
        try {
            URLConnection uc = url.openConnection();
            int len = uc.getContentLength();
            IJ.showStatus("Downloading " + url.getFile());
            InputStream in = uc.getInputStream();
            data = new byte[len];
            int n = 0;
            while (n < len) {
                int count = in.read(data, n, len - n);
                if (count < 0) throw new EOFException();
                n += count;
                IJ.showProgress(n, len);
            }
            in.close();
        } catch (IOException e) {
            return null;
        }
        return data;
    }
