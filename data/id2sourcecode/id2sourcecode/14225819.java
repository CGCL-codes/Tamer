        public void run() {
            String storePath = null;
            String fileName = null;
            try {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                int nameLen = dis.readShort();
                if (nameLen == -1) {
                    return;
                }
                char c;
                StringBuffer sb = new StringBuffer(200);
                for (long i = 0; i < nameLen; i++) {
                    c = dis.readChar();
                    sb.append(c);
                }
                fileName = sb.toString();
                sb.insert(0, FTSImpl.this.basePath);
                storePath = sb.toString();
                DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(storePath)));
                long len = dis.readLong();
                info("[FTS] Incoming file- Storage full path:" + storePath + "| Total Len: " + len + " byte(s)\n");
                byte[] buf = new byte[FTSImpl.this.bufferSize];
                while (true) {
                    int read = 0;
                    if (dis != null) {
                        read = dis.read(buf);
                    }
                    if (read == -1) {
                        break;
                    }
                    fileOut.write(buf, 0, read);
                }
                buf = null;
                dis.close();
                fileOut.close();
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FTSEvent ftsEvent = FTSEvent.createFTSEvent(this, FTSImpl.this.basePath, fileName);
            FTSImpl.this.notifyListeners(ftsEvent);
        }
