    public void TranslateCommand(String str) {
        if (setTCPReader == true) {
            this.TcpClient.write("ab000911ffff6161aa000d0a0038\r\n");
            this.setTCPReader = false;
        }
        if (str.contains("ab000e0a")) {
            this.TcpServer.write(str);
            return;
        }
        if (this.ipicoUtil.isValidTag(str)) {
            this.rdrReply = "ValidTag";
            String messages = getTagFormat(str);
            if (this.readerMode == "Write" & messages.contains("DATA")) {
                if (messages.contains(this.writeData[this.PageNum])) {
                    if (PageNum < 14) {
                        this.PageNum++;
                        TcpServer.write(messages);
                        setWriteData(this.writeData[this.PageNum]);
                    } else {
                        this.PageNum = 1;
                    }
                } else {
                    TcpServer.write(messages);
                    setWriteData(this.writeData[this.PageNum]);
                }
            } else {
                TcpServer.write(messages);
            }
        } else if (this.ipicoUtil.isValidReply(str)) {
            if (str.contains("ab00002022")) {
                this.TcpServer.write("Reader mode set to: " + this.readerMode);
            } else if (str.contains("ab00002123")) {
                this.TcpServer.write("Data set Writing data to page: " + this.PageNum);
                writeData(this.PageNum);
            } else {
                this.TcpServer.write(str);
                this.rdrReply = str;
            }
        } else if (str.length() > 2) {
            if (str.contains("stopTolk")) {
                System.exit(0);
            } else if (str.substring(0, 3).contains("ip")) {
                connectReader(str);
            } else if (str.contains("ReadMode")) {
                this.fwdMessageToAllNextNodes("ab000320030f858b\r\n");
                this.readerMode = "Read";
            } else if (str.contains("TTOMode")) {
                this.fwdMessageToAllNextNodes("ab000320040fc5b7\r\n");
                this.readerMode = "TTO";
            } else if (str.contains("WDATA")) {
                while (str.contains("WDATA")) {
                    int index = str.indexOf("WDATA");
                    int pNum = Integer.parseInt(str.substring(index + 5, index + 7));
                    int nextIndex;
                    if (str.substring(index + 5, str.length()).contains("WDATA")) {
                        nextIndex = str.substring(index + 5, str.length()).indexOf("WDATA") + 5;
                    } else {
                        nextIndex = str.length() - 1;
                    }
                    if (nextIndex - index > 20) {
                        this.writeData[pNum] = str.substring(index + 7, nextIndex);
                        this.TcpServer.write(Integer.toString(pNum));
                        this.TcpServer.write(this.writeData[pNum]);
                    }
                    str = str.substring(index + nextIndex, str.length());
                }
                this.PageNum = 1;
                setWriteData(this.writeData[this.PageNum]);
                this.readerMode = "Write";
            } else if (str.contains("ScanNetworkReaders")) {
                try {
                    MoxaDscv.scanForReaders();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.readerType = getRdrType(str);
                TcpServer.write("READER TYPE:" + this.readerType);
                this.fwdMessageToAllNextNodes("ab00000a51\r\n");
                setDate();
            }
        }
    }
