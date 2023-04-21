    public static void main(String args[]) throws Exception {
        MD5 md5Gudy = new MD5();
        BrokenMd5Hasher md5Jmule = new BrokenMd5Hasher();
        MessageDigest md5Sun = MessageDigest.getInstance("MD5");
        ByteBuffer bhashJ = ByteBuffer.allocate(16);
        System.out.println("Gudy : " + ByteFormatter.nicePrint(md5Gudy.digest()));
        md5Gudy.reset();
        md5Jmule.finalDigest(bhashJ);
        bhashJ.rewind();
        byte hashJ[] = bhashJ.array();
        System.out.println("Jmule: " + ByteFormatter.nicePrint(hashJ));
        System.out.println("Sun: " + ByteFormatter.nicePrint(md5Sun.digest()));
        for (int i = 0; i < 1; i++) {
            ByteBuffer test = ByteBuffer.allocate(i);
            while (test.remaining() > 0) {
                test.put((byte) (Math.random() * 256));
            }
            test.rewind();
            byte hashG[] = md5Gudy.digest(test);
            md5Gudy.reset();
            md5Jmule.update(test);
            bhashJ.rewind();
            md5Jmule.finalDigest(bhashJ);
            bhashJ.rewind();
            hashJ = bhashJ.array();
            test.rewind();
            md5Sun.update(test.array());
            byte hashS[] = md5Sun.digest();
            System.out.println("Gudy : " + ByteFormatter.nicePrint(hashG));
            System.out.println("Jmule: " + ByteFormatter.nicePrint(hashJ));
            System.out.println("Sun: " + ByteFormatter.nicePrint(hashS));
        }
    }
