    private void copyFile(JFMFile fin, JFMFile fout) throws CopyCancelledException {
        if (fout.exists() && !overwriteAll && !skipAll) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEE, MMM d, yyyy 'at' hh:mm:ss");
            String message = "Target file " + fout.getPath() + "  already exists." + System.getProperty("line.separator") + System.getProperty("line.separator") + "Source last modified date: " + format.format(new java.util.Date(fin.lastModified())) + System.getProperty("line.separator") + "Target last modified date: " + format.format(new java.util.Date(fout.lastModified())) + System.getProperty("line.separator") + System.getProperty("line.separator") + "What should I do?";
            String[] buttons = new String[] { "Overwrite", "Overwrite all", "Skip", "Skip all", "Append" };
            int result = JOptionPane.showOptionDialog(this, message, "File exists", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);
            switch(result) {
                case 0:
                    break;
                case 1:
                    overwriteAll = true;
                    break;
                case 2:
                    totalBytesWritten += fin.length();
                    int t_percent = (int) ((totalBytesWritten * 100) / totalFilesSizes);
                    totalCopyProgressBar.setValue(t_percent);
                    return;
                case 3:
                    skipAll = true;
                    break;
                case 4:
                    break;
            }
        }
        if (fout.exists() && skipAll) {
            totalBytesWritten += fin.length();
            int t_percent = (int) ((totalBytesWritten * 100) / totalFilesSizes);
            totalCopyProgressBar.setValue(t_percent);
            fileCopyProgressBar.setValue(100);
            return;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = fin.getInputStream();
            out = fout.getOutputStream();
            fileCopyProgressBar.setValue(0);
            byte[] data = new byte[1024];
            int read = 0;
            long bytesWrote = 0;
            long f_length = fin.length();
            while ((read = in.read(data)) >= 0) {
                if (cancel) {
                    throw new CopyCancelledException();
                }
                out.write(data, 0, read);
                bytesWrote += read;
                totalBytesWritten += read;
                int f_percent = (int) ((bytesWrote * 100) / f_length);
                int t_percent = (int) ((totalBytesWritten * 100) / totalFilesSizes);
                fileCopyProgressBar.setValue(f_percent);
                totalCopyProgressBar.setValue(t_percent);
            }
            fileCopyProgressBar.setValue(100);
        } catch (Exception ex) {
            if (ex instanceof CopyCancelledException) throw (CopyCancelledException) ex;
            JOptionPane.showMessageDialog(this, "Error while writing " + fout.getPath(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception ignored) {
            }
        }
    }
