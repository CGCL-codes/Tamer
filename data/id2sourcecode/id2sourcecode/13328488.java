    public void run() {
        byte[] buffer = new byte[this.poolDown.getBandwith()];
        File file;
        FileOutputStream fStream = null;
        while (this.running) {
            this.state = PooledDownload.PENDING;
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
            this.state = PooledDownload.WAIT_SENDER;
            this.di.setState(this.state);
            stopped = false;
            delete = false;
            filename = di.getResource().getPath();
            Date now = new Date(System.currentTimeMillis());
            String str = "";
            str += "ID=" + di.getIDString();
            str += "\nIDResource=" + di.getResource().getIDString();
            str += "\nName=" + di.getResource().getName();
            str += "\nProjectName=" + di.getResource().getNameProject();
            str += "\nUserID=" + di.getUserID();
            str += "\nFileName=" + di.getResource().getPath();
            str += "\nSize=" + di.getResource().getSize();
            str += "\nBytesResidues=" + di.getByteResidue();
            str += "\nSource=" + di.getAddrSource().getCanonicalHostName() + ",TCPPort=" + di.getTCPPortSource() + ",UDPPort=" + di.getUDPPortSource() + ",SSLPort=" + di.getSSLPortSource();
            debug.println("Download: start download {\n " + str + "}");
            try {
                if (filename == null) {
                    debug.println("Download: impossible start download " + di.getIDString() + ", filepath is null");
                    continue;
                }
                file = new File(filename);
                if (this.di.getByteResidue() != this.di.getResource().getSize()) {
                    int pos = this.di.getResource().getSize() - this.di.getByteResidue();
                    if (pos != file.length()) {
                        debug.println("Download: impossible restart download " + di.getIDString() + ", file not found");
                        continue;
                    }
                }
                fStream = new FileOutputStream(file, true);
                int offset = this.di.getResource().getSize() - this.di.getByteResidue();
                if (offset > 0) fStream.getChannel().position(offset);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            } catch (java.io.IOException e) {
                e.printStackTrace();
                continue;
            }
            if (di.isSecureDownload()) {
                try {
                    boolean insideGroup = Group.matching(di.getIDGroup(), this.poolDown.getProfileNode().getGroup().getID());
                    if (insideGroup == true && this.ssfGroup != null) {
                        sslsocket = (SSLSocket) ssfGroup.createSocket(di.getAddrSource(), di.getSSLPortSource());
                        sslsocket.setSoTimeout(TIMEOUT);
                        HandshakeCompletedListener hcl = new SimpleHandshakeListener("client");
                        sslsocket.addHandshakeCompletedListener(hcl);
                        sslsocket.startHandshake();
                        inFromServer = new DataInputStream(new BufferedInputStream(sslsocket.getInputStream()));
                        outToServer = new DataOutputStream(new BufferedOutputStream(sslsocket.getOutputStream()));
                    } else if (insideGroup == false && this.ssfGiewsNetwork != null) {
                        sslsocket = (SSLSocket) ssfGiewsNetwork.createSocket(di.getAddrSource(), di.getSSLPortSource());
                        sslsocket.setSoTimeout(TIMEOUT);
                        HandshakeCompletedListener hcl = new SimpleHandshakeListener("client");
                        sslsocket.addHandshakeCompletedListener(hcl);
                        sslsocket.startHandshake();
                        inFromServer = new DataInputStream(new BufferedInputStream(sslsocket.getInputStream()));
                        outToServer = new DataOutputStream(new BufferedOutputStream(sslsocket.getOutputStream()));
                    } else {
                        this.debug.println("DownloadThread: impossible create SSL socket");
                        continue;
                    }
                } catch (Exception e) {
                    this.debug.println("DownloadThread: Error");
                    this.poolDown.addDownloadStopped(di, true);
                    e.printStackTrace();
                    continue;
                }
            } else {
                try {
                    socket = new Socket(di.getAddrSource(), di.getTCPPortSource());
                    socket.setSoTimeout(TIMEOUT);
                    inFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    outToServer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                } catch (IOException e) {
                    this.debug.println("DownloadThread: Error");
                    this.poolDown.addDownloadStopped(di, true);
                    e.printStackTrace();
                    continue;
                }
            }
            try {
                Message msg = new Message(Message.DOWNLOADREQUEST, Message.newID((byte) 10));
                msg.setIDDownload(di.getID());
                msg.setArea(this.poolDown.getProfileNode().getGroup());
                msg.setIDResource(di.getResource().getID());
                msg.setResourceType(di.getResource().getType());
                msg.setResourceDigest(di.getResource().getDigest());
                msg.setResourceOffset(di.getByteResidue());
                byte[] data = msg.getBytes();
                outToServer.writeInt(data.length);
                outToServer.write(data);
                outToServer.flush();
            } catch (MessageException e) {
                e.printStackTrace();
                this.poolDown.addDownloadStopped(di, true);
                this.closeAllStream(socket, inFromServer, outToServer);
                continue;
            } catch (IOException e) {
                this.poolDown.addDownloadStopped(di, true);
                this.closeAllStream(socket, inFromServer, outToServer);
                e.printStackTrace();
                continue;
            }
            boolean res = false;
            try {
                byte type = inFromServer.readByte();
                if (type == Message.FALSE) {
                    this.closeAllStream(socket, inFromServer, outToServer);
                    this.poolDown.addDownloadStopped(di, true);
                    debug.println("Impossible start Download: " + this.di.getIDString() + ", sender not allow download");
                    continue;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                this.poolDown.addDownloadStopped(di, true);
                this.closeAllStream(socket, inFromServer, outToServer);
                continue;
            } catch (IOException e) {
                this.poolDown.addDownloadStopped(di, true);
                this.closeAllStream(socket, inFromServer, outToServer);
                continue;
            }
            byte type = Message.FALSE;
            while (true) {
                try {
                    debug.println("Download: Wait Sender");
                    type = inFromServer.readByte();
                    break;
                } catch (SocketTimeoutException e) {
                    debug.println("Download: timeout expired");
                    if (stopped) {
                        this.poolDown.addDownloadStopped(di, true);
                        type = Message.FALSE;
                        break;
                    }
                    if (delete) {
                        type = Message.FALSE;
                        break;
                    }
                    if (di.getPingPending() < THRESHOLD) {
                        req.sendDownloadPing(di);
                    } else {
                        debug.println("Download: download " + di.getIDString() + " terminated, source not response");
                        this.poolDown.addDownloadStopped(di, true);
                        this.closeAllStream(socket, inFromServer, outToServer);
                        break;
                    }
                    continue;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            if (type == Message.FALSE) {
                debug.println("Download Interrupted: " + this.di.getIDString());
                this.poolDown.addDownloadStopped(di, true);
                this.closeAllStream(socket, inFromServer, outToServer);
                continue;
            }
            this.state = PooledDownload.RUNNABLE;
            this.di.setState(this.state);
            boolean readAgain = true;
            long a = System.currentTimeMillis();
            long b;
            float bitRate;
            int byteRead = 1, byteByRead, totalByte = 0, byteByReadIn;
            int index, calcSpeed = 0;
            int decrementSpeed = 0;
            int speed;
            try {
                long startTime, endTime;
                speed = this.poolDown.setSpeed(this.id, this.poolDown.getBandwith());
                while (true) {
                    decrementSpeed = 0;
                    byteByReadIn = inFromServer.readInt();
                    if (byteByReadIn <= 0) {
                        break;
                    }
                    byteByRead = byteByReadIn;
                    byteRead = 0;
                    speed = this.poolDown.setSpeed(this.id, byteByReadIn);
                    if (speed == -1) {
                        speed = byteByReadIn;
                        decrementSpeed = 1;
                    } else if (byteByReadIn < speed) {
                        decrementSpeed = 1;
                    } else if (byteByReadIn > speed) {
                        decrementSpeed = speed;
                    }
                    while (true) {
                        startTime = System.currentTimeMillis();
                        byteRead = inFromServer.read(buffer, 0, speed);
                        byteByRead -= byteRead;
                        totalByte += byteRead;
                        fStream.write(buffer, 0, byteRead);
                        this.di.decrementByteResidue(byteRead);
                        calcSpeed++;
                        if (calcSpeed == 5) {
                            b = System.currentTimeMillis();
                            bitRate = ((float) totalByte / (float) (b - a)) * 1000;
                            this.di.setSpeed((8 * bitRate) / 1024);
                            a = System.currentTimeMillis();
                            calcSpeed = 0;
                            totalByte = 0;
                        }
                        if (byteByRead == 0) {
                            break;
                        }
                    }
                    outToServer.writeInt(decrementSpeed);
                    outToServer.flush();
                    endTime = System.currentTimeMillis();
                    int diffTime = (int) (endTime - startTime);
                    try {
                        if (diffTime < 1000) {
                            Thread.sleep((1000 - diffTime));
                        }
                    } catch (InterruptedException e) {
                        this.closeAllStream(socket, inFromServer, outToServer);
                        throw new Exception("");
                    }
                    if (stopped) {
                        this.closeAllStream(socket, inFromServer, outToServer);
                        this.poolDown.addDownloadStopped(di, true);
                        break;
                    }
                    if (delete) {
                        this.closeAllStream(socket, inFromServer, outToServer);
                        break;
                    }
                }
                this.poolDown.setSpeedZero(this.id);
                this.closeAllStream(socket, inFromServer, outToServer);
                fStream.close();
            } catch (java.net.SocketTimeoutException e) {
                if (di.getByteResidue() > 0) {
                    debug.println("Download: download " + this.di.getIDString() + " interrupted: timeout expires");
                    stopped = true;
                    this.poolDown.addDownloadStopped(di, true);
                }
            } catch (Exception e) {
                if (di.getByteResidue() > 0) {
                    debug.println("Download: download " + this.di.getIDString() + " interrupted");
                    stopped = true;
                    this.poolDown.addDownloadStopped(di, true);
                }
            } finally {
                try {
                    fStream.close();
                    this.closeAllStream(socket, inFromServer, outToServer);
                } catch (java.io.IOException ex) {
                }
            }
            now = new Date(System.currentTimeMillis());
            if ((!stopped) && (!delete)) {
                debug.println("Download: end download " + di.getIDString());
                di.setEndDate(System.currentTimeMillis());
                this.poolDown.addResourceDownloaded(di);
            }
            if (delete) {
                java.io.File file2 = new java.io.File(di.getResource().getPath());
                if (file2 != null) {
                    java.io.File dir = new java.io.File(file2.getParent());
                    this.poolDown.deleteDirectory(dir);
                }
            }
        }
    }
