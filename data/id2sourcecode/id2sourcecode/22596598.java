    public boolean download() {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(location);
            int chunks = (getSize() / chunkSize) + 1;
            fos = new FileOutputStream(new File(target));
            for (int chunk = 0; chunk < chunks; chunk++) {
                int downloaded = 0;
                URLConnection connection = url.openConnection();
                if (chunk > 0) {
                    connection.addRequestProperty("Range", "bytes=" + (chunk * chunkSize) + "-");
                }
                bis = new BufferedInputStream(connection.getInputStream());
                StringBuffer sb = new StringBuffer();
                byte[] buffer = new byte[bufferSize];
                int r = bis.read(buffer);
                int total = r;
                while (r != -1) {
                    boolean shouldBreak = false;
                    if ((downloaded + r) > chunkSize) {
                        r = chunkSize - downloaded;
                        shouldBreak = true;
                    }
                    if (r == bufferSize) {
                        fos.write(buffer);
                    } else {
                        for (int i = 0; i < r; i++) {
                            fos.write(buffer[i]);
                        }
                    }
                    if (isCanceled()) {
                        return true;
                    } else if (r != -1) {
                        downloaded += r;
                        total += r;
                        downloaded(total);
                    }
                    if (shouldBreak) {
                        break;
                    }
                    r = bis.read(buffer);
                }
                bis.close();
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
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
