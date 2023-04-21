    protected void _start() {
        if (file == null) {
            error("start()", "no file is set up to send");
            return;
        }
        progress = 0;
        long fileSize_ = file.length();
        try {
            FileInputStream in_ = new FileInputStream(file);
            DataOutputStream out_ = new DataOutputStream(getOutputStream());
            if (isDebugEnabled()) debug("Start transmitting a file of size " + fileSize_);
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
            if (isDebugEnabled()) debug("Done with '" + file.getName() + "'");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            error("start()", ioe);
        }
    }
