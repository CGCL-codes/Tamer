    Socket4 createParalellSocket() throws IOException {
        write(Msg.GET_THREAD_ID);
        int serverThreadID = expectedByteResponse(Msg.ID_LIST).readInt();
        Socket4 sock = i_socket.openParalellSocket();
        loginToServer(sock);
        if (switchedToFile != null) {
            MsgD message = Msg.SWITCH_TO_FILE.getWriterForString(systemTransaction(), switchedToFile);
            message.write(sock);
            if (!(Msg.OK.equals(Msg.readMessage(this, systemTransaction(), sock)))) {
                throw new IOException(Messages.get(42));
            }
        }
        Msg.USE_TRANSACTION.getWriterForInt(_transaction, serverThreadID).write(sock);
        return sock;
    }
