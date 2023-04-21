        private final int copyFiles(File[] list) throws InterruptedException {
            if (list == null) return 0;
            int counter = 0;
            try {
                for (int i = 0; i < list.length; i++) {
                    if (stop || isInterrupted()) {
                        error(ctx.getString(R.string.interrupted));
                        break;
                    }
                    File f = list[i];
                    if (f != null && f.exists()) {
                        if (f.isFile()) {
                            progressMessage = ctx.getString(R.string.uploading, f.getName());
                            sendProgress(progressMessage, 0);
                            String fn = f.getAbsolutePath().substring(basePathLen);
                            FileInputStream in = new FileInputStream(f);
                            setCurFileLength(f.length());
                            synchronized (ftp) {
                                ftp.clearLog();
                                if (!ftp.store(fn, in, this)) {
                                    error(ctx.getString(R.string.ftp_upload_failed, f.getName(), ftp.getLog()));
                                    break;
                                }
                            }
                            progressMessage = "";
                        } else if (f.isDirectory()) {
                            synchronized (ftp) {
                                ftp.clearLog();
                                String toCreate = f.getAbsolutePath().substring(basePathLen);
                                if (!ftp.makeDir(toCreate)) {
                                    error(ctx.getString(R.string.ftp_mkdir_failed, toCreate, ftp.getLog()));
                                    break;
                                }
                            }
                            counter += copyFiles(f.listFiles());
                            if (errMsg != null) break;
                        }
                        counter++;
                        if (move && !f.delete()) {
                            error(ctx.getString(R.string.cant_del, f.getCanonicalPath()));
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                error("IOException: " + e.getMessage());
            }
            return counter;
        }
