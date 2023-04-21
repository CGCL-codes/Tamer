        public void run() {
            try {
                URL url = new URL(info.getUrl());
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Range", "bytes=" + start + "-");
                if (info.getTotalSize() == 0) {
                    info.setTotalSize(conn.getContentLength());
                }
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                RandomAccessFile raf = new RandomAccessFile(info.getLocalPath(), "rw");
                raf.seek(start);
                int c = 0;
                byte[] b = new byte[1024 * 10];
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        int percent = (Double.valueOf((count * 1.0 / info.getTotalSize() * 100))).intValue();
                        Intent intent = new Intent(ACTION_NOTIFY_PROGRESS);
                        intent.putExtra("id", info.getId());
                        intent.putExtra("percent", percent);
                        DownloadService.this.sendBroadcast(intent);
                    }
                }, new Date(), 2000);
                while ((c = bis.read(b)) != -1 && !Thread.currentThread().isInterrupted()) {
                    raf.write(b, 0, c);
                    count += c;
                }
                timer.cancel();
                downLoadingMap.remove(info.getId());
                if (!Thread.currentThread().isInterrupted()) {
                    mdb.updateDowloadApp(info.getId(), AppInfo.WAIT_INSTALL);
                    Intent intent = new Intent(ACTION_NOTIFY_FINISH);
                    intent.putExtra("id", info.getId());
                    intent.putExtra("path", info.getLocalPath());
                    DownloadService.this.sendBroadcast(intent);
                } else {
                    Intent intent = new Intent(ACTION_NOTIFY_INTERRUPT);
                    intent.putExtra("id", info.getId());
                    DownloadService.this.sendBroadcast(intent);
                }
            } catch (Exception e) {
                timer.cancel();
                downLoadingMap.remove(info.getId());
                Intent intent = new Intent(ACTION_NOTIFY_INTERRUPT);
                intent.putExtra("id", info.getId());
                DownloadService.this.sendBroadcast(intent);
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
