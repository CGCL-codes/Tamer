    public IRemoteMessage readObject(Object obj) {
        IRemoteMessage remoteMessage = null;
        GZIPInputStream gzis = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) obj);
            gzis = new GZIPInputStream(bais);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b;
            while ((b = gzis.read()) != -1) baos.write(b);
            gzis.close();
            byte[] extractedObj = baos.toByteArray();
            bais = new ByteArrayInputStream(extractedObj);
            ois = new ObjectInputStream(bais);
            remoteMessage = (IRemoteMessage) ois.readUnshared();
            ois.close();
        } catch (Exception e) {
            throw new RuntimeException("Can't read message", e);
        } finally {
            if (gzis != null) try {
                gzis.close();
            } catch (IOException e) {
            }
            if (ois != null) try {
                ois.close();
            } catch (IOException e) {
            }
        }
        return remoteMessage;
    }
