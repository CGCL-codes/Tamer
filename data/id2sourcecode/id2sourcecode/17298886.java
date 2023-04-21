    public void run() {
        end = false;
        if (verbose) show();
        try {
            URL url = new URL(urlString);
            URLConnection uc = url.openConnection();
            int contentLength = uc.getContentLength();
            if ((contentLength == -1) && verbose) saveProgressBar.setEnabled(false);
            InputStream raw = uc.getInputStream();
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[4096];
            int bytesRead = 0;
            int total = 0;
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
            if (contentLength != -1) {
                while (total < contentLength) {
                    if (end) {
                        out.close();
                        deleteFile();
                        break;
                    }
                    bytesRead = in.read(data, 0, 4096);
                    if (bytesRead == -1) break;
                    total += bytesRead;
                    out.write(data, 0, bytesRead);
                    if (verbose) saveProgressBar.setValue((int) (100 * ((float) total / (float) contentLength)));
                }
            } else {
                while ((bytesRead = in.read(data, 0, 4096)) != -1) {
                    if (end) {
                        out.close();
                        deleteFile();
                        break;
                    }
                    out.write(data, 0, bytesRead);
                }
            }
            in.close();
            if (!end) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Can't download file\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (verbose) dispose();
    }
