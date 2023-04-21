    @Override
    public final String getMD5Digest() {
        final byte[] myMD5 = new byte[info.c64dataLen + 6 + info.songs + (info.clockSpeed == SidTune.Clock.NTSC ? 1 : 0)];
        System.arraycopy(program, fileOffset, myMD5, 0, info.c64dataLen);
        int i = info.c64dataLen;
        myMD5[i++] = (byte) (info.initAddr & 0xff);
        myMD5[i++] = (byte) (info.initAddr >> 8);
        myMD5[i++] = (byte) (info.playAddr & 0xff);
        myMD5[i++] = (byte) (info.playAddr >> 8);
        myMD5[i++] = (byte) (info.songs & 0xff);
        myMD5[i++] = (byte) (info.songs >> 8);
        for (int s = 1; s <= info.songs; s++) {
            myMD5[i++] = (byte) songSpeed[s - 1].speedValue();
        }
        if (info.clockSpeed == SidTune.Clock.NTSC) {
            myMD5[i++] = (byte) info.clockSpeed.ordinal();
        }
        StringBuilder md5 = new StringBuilder();
        final byte[] encryptMsg = md5Digest.digest(myMD5);
        for (final byte anEncryptMsg : encryptMsg) {
            md5.append(String.format("%02x", anEncryptMsg & 0xff));
        }
        return md5.toString();
    }
