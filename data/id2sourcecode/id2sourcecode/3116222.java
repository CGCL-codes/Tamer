    private int scan(Bitstream bitstream, InputStream inputstream, String itemHandle) {
        try {
            dataOutputStream.write(INSTREAM);
        } catch (IOException e) {
            log.error("Error writing INSTREAM command . . .");
            return Curator.CURATE_ERROR;
        }
        int read = DEFAULT_CHUNK_SIZE;
        while (read == DEFAULT_CHUNK_SIZE) {
            try {
                read = inputstream.read(buffer);
            } catch (IOException e) {
                log.error("Failed attempting to read the InputStream . . . ");
                return Curator.CURATE_ERROR;
            }
            if (read == -1) {
                break;
            }
            try {
                dataOutputStream.writeInt(read);
                dataOutputStream.write(buffer, 0, read);
            } catch (IOException e) {
                log.error("Could not write to the socket . . . ");
                return Curator.CURATE_ERROR;
            }
        }
        try {
            dataOutputStream.writeInt(0);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error("Error writing zero-length chunk to socket");
            return Curator.CURATE_ERROR;
        }
        try {
            read = socket.getInputStream().read(buffer);
        } catch (IOException e) {
            log.error("Error reading result from socket");
            return Curator.CURATE_ERROR;
        }
        if (read > 0) {
            String response = new String(buffer, 0, read);
            logDebugMessage("Response: " + response);
            if (response.indexOf("FOUND") != -1) {
                String itemMsg = "item - " + itemHandle + ": ";
                String bsMsg = "bitstream - " + bitstream.getName() + ": SequenceId - " + bitstream.getSequenceID() + ": infected";
                report(itemMsg + bsMsg);
                results.add(bsMsg);
                return Curator.CURATE_FAIL;
            } else {
                return Curator.CURATE_SUCCESS;
            }
        }
        return Curator.CURATE_ERROR;
    }
