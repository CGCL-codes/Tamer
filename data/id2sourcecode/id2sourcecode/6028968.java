        private final int copyFiles(LsItem[] list, String path) throws InterruptedException {
            int counter = 0;
            try {
                for (int i = 0; i < list.length; i++) {
                    if (stop || isInterrupted()) {
                        error(ctx.getString(R.string.interrupted));
                        break;
                    }
                    LsItem f = list[i];
                    if (f != null) {
                        String pathName = path + f.getName();
                        File dest = new File(dest_folder, pathName);
                        if (f.isDirectory()) {
                            if (!dest.mkdir()) {
                                if (!dest.exists() || !dest.isDirectory()) {
                                    errMsg = "Can't create folder \"" + dest.getCanonicalPath() + "\"";
                                    break;
                                }
                            }
                            LsItem[] subItems = ftp.getDirList(pathName);
                            if (subItems == null) {
                                errMsg = "Failed to get the file list of the subfolder '" + pathName + "'.\n FTP log:\n\n" + ftp.getLog();
                                break;
                            }
                            counter += copyFiles(subItems, pathName + SLS);
                            if (errMsg != null) break;
                            if (move && !ftp.rmDir(pathName)) {
                                errMsg = "Failed to remove folder '" + pathName + "'.\n FTP log:\n\n" + ftp.getLog();
                                break;
                            }
                        } else {
                            if (dest.exists()) {
                                int res = askOnFileExist(ctx.getString(R.string.file_exist, dest.getAbsolutePath()), commander);
                                if (res == Commander.ABORT) break;
                                if (res == Commander.SKIP) continue;
                                if (res == Commander.REPLACE) {
                                    if (!dest.delete()) {
                                        error(ctx.getString(R.string.cant_del, dest.getAbsoluteFile()));
                                        break;
                                    }
                                }
                            }
                            progressMessage = ctx.getString(R.string.retrieving, pathName);
                            sendProgress(progressMessage, 0);
                            setCurFileLength(f.length());
                            FileOutputStream out = new FileOutputStream(dest);
                            synchronized (ftp) {
                                ftp.clearLog();
                                if (!ftp.retrieve(pathName, out, this)) {
                                    error("Can't download file '" + pathName + "'.\n FTP log:\n\n" + ftp.getLog());
                                    dest.delete();
                                    break;
                                } else if (move) {
                                    if (!ftp.delete(pathName)) {
                                        error("Can't delete file '" + pathName + "'.\n FTP log:\n\n" + ftp.getLog());
                                        break;
                                    }
                                }
                            }
                            progressMessage = "";
                        }
                        Date ftp_file_date = f.getDate();
                        if (ftp_file_date != null) dest.setLastModified(ftp_file_date.getTime());
                        final int GINGERBREAD = 9;
                        if (android.os.Build.VERSION.SDK_INT >= GINGERBREAD) ForwardCompat.setFullPermissions(dest);
                        counter++;
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                error("Runtime Exception: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                error("Input-Output Exception: " + e.getMessage());
            }
            return counter;
        }
