    private STATUS downloadFile(Deviation da, String downloadUrl, String filename, boolean reportError, YesNoAllDialog matureMoveDialog, YesNoAllDialog overwriteDialog, YesNoAllDialog overwriteNewerDialog, YesNoAllDialog deleteEmptyDialog) {
        AtomicBoolean download = new AtomicBoolean(true);
        File art = getFile(da, downloadUrl, filename, download, matureMoveDialog, overwriteDialog, overwriteNewerDialog, deleteEmptyDialog);
        if (art == null) {
            return null;
        }
        if (download.get()) {
            File parent = art.getParentFile();
            if (!parent.exists()) {
                if (!parent.mkdirs()) {
                    showMessageDialog(owner, "Unable to create '" + parent.getPath() + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
            GetMethod method = new GetMethod(downloadUrl);
            try {
                int sc = -1;
                do {
                    sc = client.executeMethod(method);
                    requestCount++;
                    if (sc != 200) {
                        if (sc == 404 || sc == 403) {
                            method.releaseConnection();
                            return STATUS.NOTFOUND;
                        } else {
                            LoggableException ex = new LoggableException(method.getResponseBodyAsString());
                            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
                            int res = showConfirmDialog(owner, "An error has occured when contacting deviantART : error " + sc + ". Try again?", "Continue?", JOptionPane.YES_NO_CANCEL_OPTION);
                            if (res == JOptionPane.NO_OPTION) {
                                String text = "<br/><a style=\"color:red;\" href=\"" + da.getUrl() + "\">" + downloadUrl + " has an error" + "</a>";
                                setPaneText(text);
                                method.releaseConnection();
                                progress.incremTotal();
                                return STATUS.SKIP;
                            }
                            if (res == JOptionPane.CANCEL_OPTION) {
                                return null;
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                } while (sc != 200);
                int length = (int) method.getResponseContentLength();
                int copied = 0;
                progress.setUnitMax(length);
                InputStream is = method.getResponseBodyAsStream();
                File tmpFile = new File(art.getParentFile(), art.getName() + ".tmp");
                FileOutputStream fos = new FileOutputStream(tmpFile, false);
                byte[] buffer = new byte[16184];
                int read = -1;
                while ((read = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, read);
                    copied += read;
                    progress.setUnitValue(copied);
                    if (progress.isCancelled()) {
                        is.close();
                        method.releaseConnection();
                        tmpFile.delete();
                        return null;
                    }
                }
                fos.close();
                method.releaseConnection();
                if (art.exists()) {
                    if (!art.delete()) {
                        showMessageDialog(owner, "Unable to delete '" + art.getPath() + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
                if (!tmpFile.renameTo(art)) {
                    showMessageDialog(owner, "Unable to rename '" + tmpFile.getPath() + "' to '" + art.getPath() + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                art.setLastModified(da.getTimestamp().getTime());
                return STATUS.DOWNLOADED;
            } catch (HttpException e) {
                showMessageDialog(owner, "Error contacting deviantART: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            } catch (IOException e) {
                showMessageDialog(owner, "Error contacting deviantART: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            progress.setText("Skipping file '" + filename + "' from " + da.getArtist());
            return STATUS.SKIP;
        }
    }
