    public void run() {
        changeStatus(Task.STATUS_RUNNING);
        TaskModel.getInstance().updateTask(task);
        task.setFileSize(getFileSize());
        long refreshTime = System.currentTimeMillis();
        long cycleTime = System.currentTimeMillis();
        byte[] buf = new byte[1024];
        int readSize;
        FileUtils.createDirectory(task.getFilePath());
        String fileName = task.getFilePath() + File.separator + task.getFileName();
        RandomAccessFile savedFile = null;
        HttpURLConnection httpConnection = null;
        InputStream input = null;
        try {
            savedFile = new RandomAccessFile(fileName, "rw");
            long pos = task.getFinishedSize();
            savedFile.seek(pos);
            URL url = new URL(task.getFileUrl());
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "RCP Get");
            httpConnection.setRequestProperty("RANGE", "bytes=" + pos + "-");
            input = httpConnection.getInputStream();
            while ((readSize = input.read(buf, 0, buf.length)) > 0 && task.getStatus() == Task.STATUS_RUNNING && !this.isInterrupted()) {
                savedFile.write(buf, 0, readSize);
                Arrays.fill(buf, (byte) 0);
                task.setFinishedSize(task.getFinishedSize() + readSize);
                long current = System.currentTimeMillis();
                long costTime = current - cycleTime;
                if (costTime > 0) {
                    task.setTotalTime(task.getTotalTime() + costTime);
                    cycleTime = current;
                }
                if (task.getTotalTime() != 0) task.setSpeed(task.getFinishedSize() / task.getTotalTime());
                refreshTime = current;
                TaskModel.getInstance().updateTask(task);
                Thread.yield();
            }
            if (task.getStatus() == Task.STATUS_STOP) {
                logger.info("下载停止");
                return;
            }
            if (this.isInterrupted()) {
                logger.info("下载中断");
                changeStatus(Task.STATUS_STOP);
                return;
            }
            changeStatus(Task.STATUS_FINISHED);
            logger.info("下载完成");
        } catch (Exception e) {
            e.printStackTrace();
            changeStatus(Task.STATUS_ERROR);
        } finally {
            if (input != null) try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (savedFile != null) {
                try {
                    savedFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (httpConnection != null) httpConnection.disconnect();
            TaskModel.getInstance().updateTask(task);
        }
    }
