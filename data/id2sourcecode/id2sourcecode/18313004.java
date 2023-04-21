    public List<Ipentry> getIPEntries(String s) {
        List<Ipentry> ret = new ArrayList<Ipentry>();
        try {
            if (mbb == null) {
                FileChannel fc = ipFile.getChannel();
                mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
                mbb.order(ByteOrder.LITTLE_ENDIAN);
            }
            int endOffset = (int) ipEnd;
            for (int offset = (int) ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
                int temp = readInt3(offset);
                if (temp != -1) {
                    IPLocation loc = getIPLocation(temp);
                    if (loc.country.indexOf(s) != -1 || loc.area.indexOf(s) != -1) {
                        Ipentry entry = new Ipentry();
                        entry.country = loc.country;
                        entry.area = loc.area;
                        readIP(offset - 4, b4);
                        entry.beginIp = Iputils.getIpStringFromBytes(b4);
                        readIP(temp, b4);
                        entry.endIp = Iputils.getIpStringFromBytes(b4);
                        ret.add(entry);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }
