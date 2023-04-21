        public void run() {
            keepGoing = true;
            try {
                BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp));
                byte[] readBuffer = new byte[1024 * 10];
                int bytesRead = 0;
                while ((bytesRead = bis.read(readBuffer)) != -1 && keepGoing) {
                    downloadedBytes += bytesRead;
                    bos.write(readBuffer, 0, bytesRead);
                    System.out.println(bytesRead);
                    sendNotification(DownloadState.DOWNLOADED_CHUNK);
                }
                bos.flush();
                bos.close();
                bis.close();
                connection.disconnect();
                if (downloadedBytes >= getContentLength()) {
                    File pFile = new File(plugin_folder_path + File.separator + pluginShortName + ".jar");
                    if (pFile.exists()) {
                        pFile.delete();
                        pFile = new File(plugin_folder_path + File.separator + pluginShortName + ".jar");
                    }
                    pFile.createNewFile();
                    temp.renameTo(pFile);
                }
            } catch (java.io.EOFException eof) {
                System.out.println("PluginDownloader.startDownload()::downloadWorker: We reached the end!");
                sendNotification(DownloadState.FINISHED_DOWNLOADING);
            } catch (java.io.IOException ioe) {
                ioe.printStackTrace();
                sendNotification(DownloadState.CANCELLED_DOWNLOADING);
            } catch (Exception e) {
                System.out.println("Unexpected Exception downloading " + pluginShortName);
                e.printStackTrace();
            } finally {
                if (!keepGoing) {
                    System.out.println("PluginDownloader.run() -> somebody told me to stop!");
                    sendNotification(DownloadState.CANCELLED_DOWNLOADING);
                } else {
                    sendNotification(DownloadState.FINISHED_DOWNLOADING);
                }
            }
        }
