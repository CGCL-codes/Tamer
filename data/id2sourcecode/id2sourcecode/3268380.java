    private void findUnusedKeysRegEx(Set keys, File source) throws IOException {
        if (source.isDirectory()) {
            File[] childs = source.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    return file.isDirectory() || file.getName().endsWith("java");
                }
            });
            for (int i = 0; i < childs.length; i++) {
                findUnusedKeysRegEx(keys, childs[i]);
            }
        } else {
            System.out.println("searching " + keys.size() + " : " + source);
            FileInputStream inStream = new FileInputStream(source);
            FileChannel channel = inStream.getChannel();
            ByteBuffer byteBuf = ByteBuffer.allocate((int) source.length());
            channel.read(byteBuf);
            byteBuf.rewind();
            CharBuffer charBuf = Charset.forName("US-ASCII").decode(byteBuf);
            String[] keyArr = (String[]) keys.toArray(new String[keys.size()]);
            for (int i = 0; i < keyArr.length; i++) {
                Pattern pattern = Pattern.compile(".*" + keyArr[i] + ".*", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(charBuf);
                if (matcher.matches()) {
                    keys.remove(keyArr[i]);
                }
            }
        }
    }
