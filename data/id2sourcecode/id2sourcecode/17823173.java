    public void run() {
        splitter.setRun(true);
        InputStream input = getInputStream();
        if (input == null) {
            task.writeMessage(splitter.getName(), Messages.DownloadThread_ERR_Can_Not_Get_Input_Stream);
            splitter.setRun(false);
            return;
        }
        try {
            task.writeMessage(splitter.getName(), Messages.DownloadThread_MSG_Reading);
            int size = 0;
            byte[] buf = new byte[2048];
            while ((size = input.read(buf, 0, buf.length)) > 0 && splitter.isRun() && !this.isInterrupted() && (((splitter.getFinished() + splitter.getStartPos()) < splitter.getEndPos()) || splitter.getEndPos() == 0)) {
                int pos = Integer.parseInt((splitter.getStartPos() + splitter.getFinished()) + "");
                synchronized (file) {
                    file.seek(pos);
                    file.write(buf, 0, size);
                }
                splitter.setFinished(splitter.getFinished() + size);
                Arrays.fill(buf, (byte) 0);
                sleep(10);
            }
            if (splitter.isFinish()) {
                logger.info(splitter.getName() + "线程任务完成!");
                task.writeMessage(splitter.getName(), Messages.DownloadThread_MSG_Thread_Task_Finished);
            } else {
                logger.info(splitter.getName() + "线程停止!");
                task.writeMessage(splitter.getName(), Messages.DownloadThread_MSG_Thread_Stopped);
            }
        } catch (IOException e) {
            task.writeMessage(splitter.getName(), Messages.DownloadThread_ERR_Stream_Exception + e.getLocalizedMessage());
            return;
        } catch (InterruptedException e) {
            task.writeMessage(splitter.getName(), Messages.DownloadThread_ERR_Thread_Interrupted);
            return;
        } finally {
            splitter.setRun(false);
            try {
                if (input != null) input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
