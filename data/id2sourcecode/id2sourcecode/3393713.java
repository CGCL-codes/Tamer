    void readXML() throws FileNotFoundException, UnsupportedEncodingException {
        String encoding = System.getProperty("file.encoding");
        String version = FileMagic.getjGnashXMLVersion(file);
        if (Float.parseFloat(version) >= 2.01f) {
            encoding = "UTF-8";
        }
        ObjectInputStream in = null;
        FileLock readLock = null;
        FileInputStream fis = new FileInputStream(file);
        Reader reader = new BufferedReader(new InputStreamReader(fis, encoding));
        readWriteLock.writeLock().lock();
        try {
            XStream xstream = configureXStream(new XStream(new StoredObjectReflectionProvider(objects), new KXml2Driver()));
            readLock = fis.getChannel().tryLock(0, Long.MAX_VALUE, true);
            if (readLock != null) {
                in = xstream.createObjectInputStream(reader);
                in.readObject();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    if (readLock != null) {
                        readLock.release();
                    }
                    in.close();
                } catch (IOException e) {
                    Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            try {
                reader.close();
            } catch (IOException e) {
                Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                fis.close();
            } catch (IOException e) {
                Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, e);
            }
            acquireFileLock();
            readWriteLock.writeLock().unlock();
        }
    }
