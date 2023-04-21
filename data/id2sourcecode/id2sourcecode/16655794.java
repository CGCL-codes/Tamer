    private void getFile(String fileURI, String targetFileName, String sourceFileName) {
        try {
            System.out.println("Getting " + sourceFileName + " from " + fileURI);
            FileOutputStream out = null;
            URL url = new URL(fileURI);
            File targetFile = new File(targetFileName);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            InputStream in = null;
            try {
                in = url.openStream();
                out = new FileOutputStream(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DeploymentException("couldn't open stream due to: " + e.getMessage());
            }
            URLConnection con = url.openConnection();
            long fileLength = con.getContentLength();
            mLog.debug("File Length: " + fileLength);
            mLog.debug("Max Value for LONG: " + Long.MAX_VALUE);
            mLog.debug("Max Value for Integer: " + Integer.MAX_VALUE);
            ReadableByteChannel channelIn = Channels.newChannel(in);
            FileChannel channelOut = out.getChannel();
            channelOut.transferFrom(channelIn, 0, Integer.MAX_VALUE);
            channelIn.close();
            channelOut.close();
            out.flush();
            out.close();
            in.close();
        } catch (Exception ix) {
            ix.printStackTrace();
        }
    }
