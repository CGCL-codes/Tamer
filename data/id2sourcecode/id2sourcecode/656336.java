    protected void _start() {
        if (file == null) {
            error("start()", "no file is set up to send");
            return;
        }
        progress = 0;
        long fileSize_ = file.length();
        try {
            InetSocket socket_ = socketMaster.newSocket();
            socketMaster.bind(socket_, localAddress, 0);
            socketMaster.connect(socket_, remoteAddress, remotePort);
            FileInputStream in_ = new FileInputStream(file);
            DataOutputStream out_ = new DataOutputStream(socket_.getOutputStream());
            if (isDebugEnabled()) debug("Start transmitting a file of size " + fileSize_);
            byte[] remoteFileName_ = remoteFile.getBytes();
            out_.writeInt(remoteFileName_.length);
            out_.write(remoteFileName_);
            out_.writeLong(fileSize_);
            byte[] buf_ = new byte[bufferSize];
            while (progress <= fileSize_) {
                int nread_ = in_.read(buf_, 0, buf_.length);
                if (nread_ <= 0) break;
                progress += nread_;
                out_.write(buf_, 0, nread_);
            }
            in_.close();
            out_.close();
            socketMaster.close(socket_);
            if (isDebugEnabled()) debug("Done with '" + file.getName() + "'");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            error("start()", ioe);
        }
    }
