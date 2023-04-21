        private boolean downloadFile(String pMirrorSite, Updateable pUpdateable, String pDestinationDir, IProgressMonitor monitor) throws InterruptedException, MalformedURLException, FileNotFoundException, IOException {
            pMirrorSite = pMirrorSite.endsWith("/") ? pMirrorSite : pMirrorSite + "/";
            URL url = new URL(pMirrorSite + pUpdateable.getJarLocation());
            URLConnection urlConnection = url.openConnection();
            InputStream istream = urlConnection.getInputStream();
            String destinationFile = Settings.getUpdatesTempDirectory() + File.separator + pDestinationDir + File.separator + pUpdateable.getInstallName();
            BufferedOutputStream fos;
            try {
                fos = new BufferedOutputStream(new FileOutputStream(destinationFile));
            } catch (FileNotFoundException e) {
                MessageSystem.logException("", getClass().getName(), "run", null, e);
                MessageSystem.showErrorMessage(Messages.getString("CopyAction.file.is.not.found") + "\n" + e.getMessage());
                return false;
            }
            monitor.subTask(Messages.getString("ScythaUpdater.downloading", new Object[] { pUpdateable.getJarLocation(), "" + (Integer.valueOf(pUpdateable.getSize()).intValue() / 1024) }));
            byte[] buf = new byte[1024];
            int j = 0;
            while ((j = istream.read(buf)) != -1) {
                fos.write(buf, 0, j);
                if (monitor.isCanceled()) {
                    fos.flush();
                    fos.close();
                    istream.close();
                    downloadWasOk = false;
                    throw new InterruptedException();
                }
                monitor.worked(j);
            }
            fos.flush();
            fos.close();
            istream.close();
            switch(pUpdateable.getType()) {
                case IUpdateable.HELP:
                    break;
                case IUpdateable.SWT:
                    fSwtLocation = destinationFile;
                    break;
                case IUpdateable.UPDATER:
                    fUpdaterLocation = destinationFile;
                    break;
                case IUpdateable.C_LIBRARY:
                    fUpdaterLocation = destinationFile;
                    break;
            }
            downloadedFiles.add(destinationFile);
            return true;
        }
