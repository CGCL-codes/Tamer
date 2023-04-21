    private void findUnusedKeys(Set keys, File source) throws IOException {
        if (source.isDirectory()) {
            File[] childs = source.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    return file.isDirectory() || file.getName().endsWith("java");
                }
            });
            for (int i = 0; i < childs.length; i++) {
                findUnusedKeys(keys, childs[i]);
            }
        } else {
            FileInputStream inStream = new FileInputStream(source);
            FileChannel channel = inStream.getChannel();
            ByteBuffer byteBuf = ByteBuffer.allocate((int) source.length());
            channel.read(byteBuf);
            byteBuf.rewind();
            String fileString = new String(byteBuf.array(), "US-ASCII");
            String[] keyArr = (String[]) keys.toArray(new String[keys.size()]);
            for (int i = 0; i < keyArr.length; i++) {
                if (fileString.indexOf(keyArr[i]) != -1) {
                    keys.remove(keyArr[i]);
                }
            }
        }
    }
