    public String getMyText() throws IOException {
        FileChannel channel = new FileInputStream(mFile).getChannel();
        channel.position(mRecodeOffset[mPage]);
        StringBuilder body = new StringBuilder();
        ByteBuffer bodyBuffer;
        if (mPage + 1 < mCount) {
            int length = mRecodeOffset[mPage + 1] - mRecodeOffset[mPage];
            bodyBuffer = channel.map(MapMode.READ_ONLY, mRecodeOffset[mPage], length).order(ByteOrder.BIG_ENDIAN);
            byte[] tmpCache = new byte[bodyBuffer.capacity()];
            bodyBuffer.get(tmpCache);
            if (mFormat == 1) {
                byte[] ttt = new byte[8192];
                InflaterInputStream input = new InflaterInputStream(new ByteArrayInputStream(tmpCache));
                int c = 0;
                while ((c = input.read(ttt)) > 0) {
                    String str = new String(ttt, 0, c, mEncode);
                    body.append(replaceString(str));
                    if (isStop) {
                        isStop = false;
                        break;
                    }
                }
                input.close();
            } else {
                String str = new String(tmpCache, mEncode);
                body.append(str);
            }
        } else {
            bodyBuffer = ByteBuffer.wrap(new byte[8192]);
            int idx;
            while ((idx = channel.read(bodyBuffer)) > 0) {
                String str = new String(bodyBuffer.array(), mEncode);
                body.append(str);
                if (isStop) {
                    isStop = false;
                    break;
                }
            }
        }
        channel.close();
        return filter(body);
    }
