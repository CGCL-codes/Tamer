    public void run() {
        do {
            try {
                this.upd_progress.setJobStatus("Started");
                URL url = new URL(this.upd_settings.update_server + this.upd_entry.getFileCommand());
                InputStream is = url.openStream();
                if (!(new File(this.upd_settings.output_path)).exists()) {
                    (new File(this.upd_settings.output_path)).mkdirs();
                }
                RandomAccessFile raf = new RandomAccessFile(this.upd_settings.output_path + "tempFile.bin", "rw");
                float size = 671200;
                long current_size = 0;
                System.out.println("File size: " + size);
                this.upd_progress.setStatus("Download file");
                byte[] array = new byte[BLOCK_SIZE];
                while (is.available() > 0) {
                    if (is.available() < BLOCK_SIZE) {
                        array = new byte[is.available()];
                    } else {
                        if (array.length < BLOCK_SIZE) {
                            array = new byte[BLOCK_SIZE];
                        }
                    }
                    is.read(array);
                    raf.write(array);
                    current_size += array.length;
                    System.out.println("Av: " + is.available());
                    int prg = (int) ((current_size / size) * 100);
                    System.out.println("Progress: " + prg);
                    this.upd_progress.setProgress(prg);
                }
                this.upd_progress.setStatus("Checking file");
                CheckSumUtility csu = new CheckSumUtility();
                long check = csu.getChecksumValue(this.upd_settings.output_path + "tempFile.bin");
                if (check != this.upd_entry.estimated_crc) {
                    this.upd_progress.setStatus("Checksum failed.");
                    this.upd_progress.setJobStatus("Failed");
                } else {
                    this.upd_progress.setStatus("File downloaded successfully.");
                    this.upd_progress.setJobStatus("Finished");
                    retry = max_retry + 1;
                }
                File f = new File(this.upd_settings.output_path + "tempFile.bin");
                f.renameTo(new File(this.upd_settings.output_path + this.upd_entry.output_file));
                is.close();
                raf.close();
            } catch (Exception ex) {
                this.upd_progress.setJobStatus("Error");
                ex.printStackTrace();
            }
            retry++;
            System.out.println("Retry: " + retry);
        } while (retry <= max_retry);
    }
