    public boolean grep(File file, String strToSearch) {
        compile(strToSearch);
        boolean bFound = false;
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            CharBuffer cb = decoder.decode(bb);
            Matcher lm = linePattern.matcher(cb);
            Matcher pm = null;
            int lines = 0;
            while (lm.find()) {
                lines++;
                CharSequence cs = lm.group();
                if (pm == null) pm = pattern.matcher(cs); else pm.reset(cs);
                if (pm.find()) {
                    System.out.print(file + ":" + lines + ":" + cs);
                    bFound = true;
                }
                if (lm.end() == cb.limit()) break;
            }
            fc.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            return bFound;
        }
        return bFound;
    }
