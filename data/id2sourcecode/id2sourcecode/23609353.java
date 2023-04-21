    @Override
    protected Integer processCytosine(CpgBackedByGatk thisC) {
        FileChannel fc = null;
        String chr = thisC.getChrom();
        outfilesMapLock.lock();
        try {
            if (outfilesByChr.containsKey(chr)) {
                fc = outfilesByChr.get(chr);
            } else {
                String name = String.format("%s-%s", this.outPrefix, chr);
                String outfn = String.format("%s.txt", name);
                logger.info("NEW file " + outfn);
                FileOutputStream fout = null;
                fout = new FileOutputStream(outfn);
                fc = fout.getChannel();
                outfilesByChr.put(chr, fc);
            }
        } catch (Exception e) {
            System.err.printf("Fatal error , could not write to file for  %s\n%s\n", chr, e.toString());
            e.printStackTrace();
            System.exit(1);
        } finally {
            outfilesMapLock.unlock();
        }
        try {
            fc.write(charset.newEncoder().encode(CharBuffer.wrap(thisC.toString() + "\n")));
        } catch (Exception e) {
            System.err.printf("Fatal error , could not write to file for %s\n%s\n", chr, e.toString());
            e.printStackTrace();
            System.exit(1);
        }
        return 1;
    }
