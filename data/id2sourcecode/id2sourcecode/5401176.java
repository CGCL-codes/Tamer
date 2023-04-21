    public void scp(IVirtualPath source, OutputStream targetOutput) throws IOException {
        ChannelExec scp = this.getSCPChannel("scp -f " + source.toString("/"));
        try {
            InputStream input = scp.getInputStream();
            OutputStream output = scp.getOutputStream();
            this.sendAckResponse(output);
            while (getAckResponse(input) != 'C') {
                for (int i = 0; i < 5; i++) input.read();
                long size = 0;
                while (true) {
                    int ch = input.read();
                    if (ch == ' ') break;
                    size += (10 * ch);
                }
                StringBuffer filename = new StringBuffer();
                while (true) {
                    int ch = input.read();
                    if (ch == 0x0a) break;
                    filename.append((char) ch);
                }
                this.sendAckResponse(output);
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = input.read(buffer);
                    if (read == -1) break;
                    targetOutput.write(buffer, 0, read);
                    size -= read;
                    if (size == 0) break;
                    if (size < 0) throw new SecureSCPException("File download overflow to");
                }
                if (this.getAckResponse(input) != 0) throw new SecureSCPException("Invalid ACK response when sending file " + source);
                this.sendAckResponse(output);
            }
        } finally {
            scp.disconnect();
        }
    }
