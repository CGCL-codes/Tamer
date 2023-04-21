    public void run() {
        File file;
        FileInputStream fileStream = null;
        byte[] buffer = new byte[this.bandwidth];
        while (this.running) {
            this.state = PooledUpload.WAITING;
            synchronized (pool) {
                while (pool.isEmpty()) {
                    try {
                        if (this.running == false) {
                            break;
                        }
                        pool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (pool.size() <= 0) {
                    continue;
                }
                di = (DownloadInfo) pool.remove(0);
            }
            this.state = PooledUpload.RUNNABLE;
            Date now = new Date(System.currentTimeMillis());
            String str = "";
            str += "ID=" + di.getIDString();
            str += "\nIDResource=" + di.getResource().getIDString();
            str += "\nName=" + di.getResource().getName();
            str += "\nSize=" + di.getResource().getSize();
            str += "\nBytesResidues=" + di.getByteResidue();
            str += "\nSource=" + di.getAddrSource().getCanonicalHostName() + ":" + di.getTCPPortSource();
            debug.println("Upload: start upload {\n " + str + "}");
            try {
                inFromClient = di.getInputStream();
                outToClient = di.getOutputStream();
                file = new File(di.getResource().getPath());
                if (!file.exists()) {
                    debug.println("Upload: upload " + di.getIDString() + " aborted, file not found " + file.getPath());
                    outToClient.writeByte(Message.FALSE);
                    outToClient.flush();
                    this.closeStream();
                    continue;
                }
                outToClient.writeByte(Message.TRUE);
                outToClient.flush();
                fileStream = new FileInputStream(file);
                int offset = this.di.getResource().getSize() - this.di.getByteResidue();
                if (offset > 0) {
                    fileStream.getChannel().position(offset);
                }
                boolean readAgain = true;
                int byteToRead, byteRead, totalByteRead = 0, calcSpeed = 0;
                long startTime, sTimeCalcSpeed, endTime, diffTime;
                float bitRate = 0;
                sTimeCalcSpeed = System.currentTimeMillis();
                while (readAgain) {
                    startTime = System.currentTimeMillis();
                    byteToRead = this.poolUp.getSpeed(this.id);
                    if (byteToRead == 0) {
                        byteToRead = 1;
                    }
                    byteRead = fileStream.read(buffer, 0, byteToRead);
                    totalByteRead += byteRead;
                    if (byteRead < 0) {
                        outToClient.writeInt(byteRead);
                        readAgain = false;
                        fileStream.close();
                        continue;
                    }
                    outToClient.writeInt(byteRead);
                    outToClient.flush();
                    outToClient.write(buffer, 0, byteRead);
                    outToClient.flush();
                    int b = inFromClient.readInt();
                    this.di.decrementByteResidue(byteRead);
                    endTime = System.currentTimeMillis();
                    diffTime = endTime - startTime;
                    try {
                        if (b == 1) {
                            this.poolUp.incrementSpeed(this.id);
                        } else if (b > 1) {
                            this.poolUp.decrementSpeed(this.id, this.poolUp.getSpeed(id) - b);
                        }
                        if (diffTime < 1000) {
                            Thread.sleep(1000 - diffTime);
                        }
                    } catch (InterruptedException e) {
                        throw new java.io.IOException("");
                    }
                    calcSpeed++;
                    if (calcSpeed == 5) {
                        endTime = System.currentTimeMillis();
                        bitRate = ((totalByteRead) / (float) (endTime - sTimeCalcSpeed)) * 1000;
                        this.di.setSpeed((8 * bitRate) / 1024);
                        sTimeCalcSpeed = System.currentTimeMillis();
                        totalByteRead = 0;
                        calcSpeed = 0;
                    }
                }
                this.closeStream();
                fileStream.close();
            } catch (java.net.SocketTimeoutException e) {
                debug.println("Upload: upload " + di.getIDString() + " aborted, timeout expires");
                this.closeStream();
                continue;
            } catch (java.io.IOException e) {
                debug.println("Upload: upload " + di.getIDString() + " aborted," + e.getMessage());
                this.closeStream();
                continue;
            }
            now = new Date(System.currentTimeMillis());
            debug.println("Upload: end upload " + di.getIDString());
        }
    }
