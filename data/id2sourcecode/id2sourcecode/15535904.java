    public List getIPEntityList(String ipString) {
        List list = new ArrayList();
        try {
            if (ipByteBuffer == null) {
                FileChannel fileChannel = ipFile.getChannel();
                ipByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
                ipByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            }
            int endOffset = (int) ipEnd;
            for (int offset = (int) ipStart + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
                int value = readInt3(offset);
                if (value != -1) {
                    IPEntity ipEntity = getIPEntity(value);
                    if (ipEntity.getCountry().indexOf(ipString) != -1 || ipEntity.getRegion().indexOf(ipString) != -1) {
                        IPEntity entity = new IPEntity();
                        entity.setCountry(ipEntity.getCountry());
                        entity.setRegion(ipEntity.getRegion());
                        readIP(offset - 4, b4);
                        entity.setStartIP(getString(b4));
                        readIP(value, b4);
                        entity.setEndIP(getString(b4));
                        list.add(entity);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
