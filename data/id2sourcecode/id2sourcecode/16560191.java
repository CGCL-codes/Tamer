    private static synchronized void initIndex(File snpFile) {
        pointerList = new LinkedList<Integer>();
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            FileInputStream fis = new FileInputStream(snpFile);
            fc = fis.getChannel();
            long filesize = fc.size();
            byte[] somebytes = new byte[2048];
            mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            while (mbb.remaining() > 2048) {
                int thispos = mbb.position();
                mbb.get(somebytes);
                for (int i = 0; i < 2048; i++) {
                    if (somebytes[i] == eol) {
                        pointerList.add(new Integer(thispos + i + 1));
                    }
                }
            }
            while (mbb.remaining() > 0) {
                if (mbb.get() == eol) pointerList.add(new Integer(mbb.position()));
            }
            pointerList.removeLast();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
