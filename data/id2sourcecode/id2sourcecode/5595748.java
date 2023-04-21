    void ok() {
        Object[] files = list.getSelectedValues();
        for (int i = 0; i < files.length; i++) {
            String filename = (String) files[i];
            File localfile = new File(dir, filename);
            if (localfile.exists()) {
                int val = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "File exists", JOptionPane.YES_NO_OPTION);
                if (val == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            try {
                FileOutputStream localStream = new FileOutputStream(localfile);
                BufferedOutputStream localBuffer = new BufferedOutputStream(localStream, 1024);
                ftp.dataPort();
                FtpInputStream ftpInput = ftp.retrieveStream(filename);
                BufferedInputStream remoteBuffer = new BufferedInputStream(ftpInput, 1024);
                byte[] b = new byte[1024];
                int read;
                read = remoteBuffer.read(b, 0, 1024);
                while (read != -1) {
                    localBuffer.write(b, 0, read);
                    read = remoteBuffer.read(b, 0, 1024);
                }
                ftpInput.close();
                localBuffer.flush();
            } catch (IOException e) {
                transferError(e.getMessage());
            }
        }
        close();
    }
