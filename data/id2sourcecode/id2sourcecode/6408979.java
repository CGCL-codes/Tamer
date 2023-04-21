    public long writeTo(WritableByteChannel channel) throws IOException {
        long amount = 0;
        amount += ChannelUtil.writeInt(channel, 1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        _sig.save(baos);
        byte[] sigBytes = baos.toByteArray();
        amount += ChannelUtil.writeLong(channel, (long) sigBytes.length);
        amount += channel.write(ByteBuffer.wrap(sigBytes));
        Collection<String> fileNames = _snapshot.getFileNames();
        amount += ChannelUtil.writeInt(channel, fileNames.size());
        for (String fileName : fileNames) {
            amount += ChannelUtil.writeString(channel, fileName);
            amount += _dirMgr.transferFromFileToChannel(fileName, channel);
        }
        return amount;
    }
