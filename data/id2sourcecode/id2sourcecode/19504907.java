    private void copyFile(JFMFile fin, JFMFile fout) throws ActionCancelledException {
        if (fout.exists() && !overwriteAll && !skipAll) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEE, MMM d, yyyy 'at' hh:mm:ss");
            String message = "Target file " + fout.getPath() + "  already exists." + System.getProperty("line.separator") + System.getProperty("line.separator") + "Source last modified date: " + format.format(new java.util.Date(fin.lastModified())) + System.getProperty("line.separator") + "Target last modified date: " + format.format(new java.util.Date(fout.lastModified())) + System.getProperty("line.separator") + System.getProperty("line.separator") + "What should I do?";
            String[] buttons = new String[] { "Overwrite", "Overwrite all", "Skip", "Skip all", "Cancel" };
            int result = JOptionPane.showOptionDialog(progress, message, "File exists", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);
            switch(result) {
                case 0:
                    break;
                case 1:
                    overwriteAll = true;
                    break;
                case 2:
                    totalBytesWritten += fin.length();
                    int t_percent = totalFilesSizes != 0 ? (int) ((totalBytesWritten * 100) / totalFilesSizes) : 0;
                    progress.setTotalProgresssValue(t_percent);
                    return;
                case 3:
                    skipAll = true;
                    break;
                case 4:
                    throw new ActionCancelledException();
            }
        }
        if (fout.exists() && skipAll) {
            totalBytesWritten += fin.length();
            int t_percent = (int) ((totalBytesWritten * 100) / totalFilesSizes);
            progress.setTotalProgresssValue(t_percent);
            progress.setFileProgresssValue(100);
            return;
        }
        java.io.InputStream in = null;
        java.io.OutputStream out = null;
        try {
            in = fin.getInputStream();
            out = fout.getOutputStream();
            progress.setFileProgresssValue(0);
            curentlyCopiedFile = fout;
            byte[] data = new byte[1024];
            int read = 0;
            long bytesWrote = 0;
            long f_length = fin.length();
            while ((read = in.read(data)) >= 0) {
                if (cancel) {
                    throw new ActionCancelledException();
                }
                out.write(data, 0, read);
                bytesWrote += read;
                totalBytesWritten += read;
                int f_percent = (int) ((bytesWrote * 100) / f_length);
                int t_percent = (int) ((totalBytesWritten * 100) / totalFilesSizes);
                progress.setFileProgresssValue(f_percent);
                progress.setTotalProgresssValue(t_percent);
            }
            progress.setFileProgresssValue(100);
        } catch (ActionCancelledException ex) {
            ex.printStackTrace();
            curentlyCopiedFile.delete();
            throw ex;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(progress, "Error while writing " + fout.getPath(), "Error", JOptionPane.ERROR_MESSAGE);
            curentlyCopiedFile.delete();
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {
            }
            try {
                out.close();
            } catch (Exception ignored) {
            }
        }
    }
