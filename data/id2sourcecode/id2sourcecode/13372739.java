    public boolean transmitFile(File file, String fileDesc, String toJid) {
        boolean transferSuccessful = false;
        OutgoingFileTransfer transfer = _fileTransferManager.createOutgoingFileTransfer(toJid);
        if (file.exists()) {
            _controller.getLog().writeToConsole("Start transmitting file: " + file.getAbsolutePath() + " to: " + toJid);
            try {
                int counter = 0;
                transfer.sendFile(file, fileDesc);
                while (!transfer.isDone()) {
                    if (counter == _filetransferTimeout) {
                        break;
                    }
                    counter++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        _controller.getLog().writeToConsole("ERROR: Thread interrupted while transmitting file: " + file.getName());
                    }
                }
                transferSuccessful = transfer.isDone() && counter < _filetransferTimeout && transfer.getBytesSent() > -1;
            } catch (XMPPException e) {
                _controller.getLog().writeToConsole("FileTransfer throws XMPPException:");
                e.printStackTrace();
            }
        }
        return transferSuccessful;
    }
