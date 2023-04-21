    @Override
    public InputStream getXMLInputStream() {
        if (storage != null) {
            storage.reset();
            return storage;
        }
        try {
            URLConnection urlConn = scriptURL.openConnection();
            InputStream in = new BufferedInputStream(urlConn.getInputStream());
            ArrayList<Byte> buffer = new ArrayList<Byte>();
            int byteRead;
            while ((byteRead = in.read()) != -1) buffer.add((byte) byteRead);
            in.close();
            byteBuffer = new byte[buffer.size()];
            for (int i = 0; i < buffer.size(); i++) byteBuffer[i] = buffer.get(i);
            storage = new ByteArrayInputStream(byteBuffer);
            return storage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
