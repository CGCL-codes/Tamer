    public static void copyFile(String filename, InputStream iStream, File destFolder) {
        File destFile = new File(destFolder, filename);
        if (destFile.exists()) {
            throw new BuildException("Could not copy the stream for " + filename + " to " + destFolder + " as " + destFile + " already exists");
        }
        WritableByteChannel channel = null;
        try {
            channel = new FileOutputStream(destFile).getChannel();
            ByteBuffer buf = ByteBuffer.allocateDirect(10);
            byte[] bytes = new byte[1024];
            int count = 0;
            int index = 0;
            while (count >= 0) {
                if (index == count) {
                    count = iStream.read(bytes);
                    index = 0;
                }
                while (index < count && buf.hasRemaining()) {
                    buf.put(bytes[index++]);
                }
                buf.flip();
                int numWritten = channel.write(buf);
                if (buf.hasRemaining()) {
                    buf.compact();
                } else {
                    buf.clear();
                }
            }
            channel.close();
        } catch (IOException ioe) {
            throw new BuildException("Could not copy " + filename + " to " + destFolder + ": " + ioe, ioe);
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException e) {
                }
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                }
            }
        }
    }
