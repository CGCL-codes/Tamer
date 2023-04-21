    public void run() {
        byte[] buffer = new byte[4096];
        if (_action == 1) {
            try {
                connected();
                int read = 0;
                while (_size - read > 0) {
                    int r = _is.read(buffer, 0, buffer.length);
                    if (r == -1) throw new Exception(getText(IRCTextProvider.DCC_STREAM_CLOSED));
                    read += r;
                    _file.bytesReceived(buffer, 0, r);
                    Thread.yield();
                    writeConf(_os, read);
                }
                writeConf(_os, _size);
                _file.fileReceived();
            } catch (Exception e) {
                e.printStackTrace();
                _file.fileReceiveFailed();
            }
            disconnected();
            cleanup();
        } else if (_action == 2) {
            _listening = true;
            try {
                _serverSocket.setSoTimeout(30000);
                _socket = _serverSocket.accept();
                _os = new BufferedOutputStream(_socket.getOutputStream());
                _is = new BufferedInputStream(_socket.getInputStream());
                connected();
                int size = _file.getSize();
                int toread = size;
                int rec = 0;
                while (toread > 0) {
                    int r = _file.readBytes(buffer, 0, buffer.length);
                    if (r < 0) throw new Exception(getText(IRCTextProvider.DCC_STREAM_CLOSED));
                    _os.write(buffer, 0, r);
                    toread -= r;
                    if (_is.available() > 0) rec = readConf(_is);
                }
                _os.flush();
                while (rec != size) {
                    rec = readConf(_is);
                }
                _os.close();
                _file.fileSent();
            } catch (Exception e) {
                e.printStackTrace();
                _file.fileSentFailed();
            }
            disconnected();
            cleanup();
        }
    }
