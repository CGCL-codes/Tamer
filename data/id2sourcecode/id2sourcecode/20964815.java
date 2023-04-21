        private void processFile(File file) throws IOException {
            String uri = file.getPath().substring(this.parentFilepath.length());
            if (escapeBackslash) uri = uri.replace('\\', '/');
            if (file.isDirectory()) {
                System.out.println(name() + "skipping directory entry " + uri);
                return;
            }
            System.out.println(name() + uri);
            FileChannel input = new FileInputStream(file).getChannel();
            long cid = cacheTxn.insertEntry(input);
            input.close();
            StringBuilder metaString = new StringBuilder(uri.length() + 32);
            metaString.append("uri=").append(uri).append("\nc.id=").append(cid).append("\nc.tid=").append(cacheTxnId);
            ByteBuffer metaEntry = BufferUtil.INSTANCE.asciiBuffer(metaString);
            metaTxn.insertEntry(metaEntry);
            ++count;
        }
