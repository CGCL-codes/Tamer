    public void run() {
        try {
            URL url = new URL(this.addr);
            URLConnection connection = url.openConnection();
            this.fileSize = connection.getContentLength();
            CaptureSystem.filterSize = this.fileSize;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                Commun.logError(e1);
            }
            this.pbar.setMaximum(this.fileSize);
            if (this.fileSize == -1) {
                this.lblStatus.setText(MainForm.lang.lang_table[50]);
                this.isFinished = true;
                return;
            }
            String dir = this.fileDir + File.separator + this.fileTitle;
            try {
                InputStream input = connection.getInputStream();
                File fic = new File("temp");
                if (!fic.exists()) fic.mkdir();
                FileOutputStream writeFile = new FileOutputStream(dir);
                byte[] buffer = new byte[2048];
                int read;
                while (((read = input.read(buffer)) > 0) && (!this.stop)) {
                    writeFile.write(buffer, 0, read);
                    this.downSize += read;
                    this.pbar.setValue(this.downSize);
                    this.lblStatus.setText(MainForm.lang.lang_table[51] + " ... (" + Commun.sizeConvert(this.downSize) + " / " + Commun.sizeConvert(this.fileSize) + ") at " + this.speed);
                }
                writeFile.flush();
                writeFile.close();
                input.close();
            } catch (Exception e) {
                this.lblStatus.setText(MainForm.lang.lang_table[50]);
                this.isFinished = true;
                return;
            }
            this.isFinished = true;
            if (this.stop) {
                File f = new File(dir);
                f.delete();
            }
        } catch (IOException e) {
            this.isFinished = true;
            this.lblStatus.setText(MainForm.lang.lang_table[50]);
        }
    }
