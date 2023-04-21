    private void setupSocket(String digestString) {
        socketAdv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        try {
            byte[] bid = MessageDigest.getInstance("MD5").digest(digestString.getBytes("ISO-8859-1"));
            PipeID pipeID = IDFactory.newPipeID(peerGroup.getPeerGroupID(), bid);
            socketAdv.setPipeID(pipeID);
            socketAdv.setType(PipeService.UnicastType);
            socketAdv.setName("Socket Pipe");
            socketAdv.setDescription("verbose description");
            System.out.println("Creating JxtaServerSocket");
            serverSocket = new JxtaServerSocket(peerGroup, socketAdv);
            serverSocket.setSoTimeout(0);
            System.out.println("LocalAddress :" + serverSocket.getLocalSocketAddress());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
