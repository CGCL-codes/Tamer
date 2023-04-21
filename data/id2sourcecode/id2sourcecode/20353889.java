    public boolean download() {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(location);
            bis = new BufferedInputStream(url.openStream());
            fos = new FileOutputStream(new File(target));
            StringBuffer sb = new StringBuffer();
            byte[] buffer = new byte[bufferSize];
            int r = bis.read(buffer);
            int total = r;
            while (r != -1) {
                if (r == bufferSize) {
                    fos.write(buffer);
                } else {
                    for (int i = 0; i < r; i++) {
                        fos.write(buffer[i]);
                    }
                }
                r = bis.read(buffer);
                if (isCanceled()) {
                    return true;
                } else if (r != -1) {
                    total += r;
                    downloaded(total);
                }
            }
            done();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                fos.flush();
                fos.close();
                bis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
