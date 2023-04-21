    public int writetrack(FileInputStream bf, Track track, String bname, Params params) throws IOException {
        String fname;
        BufferedOutputStream f = null;
        byte buf[] = new byte[Constants.SECTLEN + 10];
        long sz, sect, realsz;
        int reallen;
        int p, p2, ep;
        byte c;
        int l;
        short i;
        float fl;
        fname = String.format("%s%02d.%s", new Object[] { bname, Integer.valueOf(track.getNum()), track.getExtension() });
        System.out.println(String.format("%2d: %s", new Object[] { Integer.valueOf(track.getNum()), fname }));
        try {
            f = new BufferedOutputStream(new FileOutputStream(fname));
        } catch (FileNotFoundException ex1) {
            System.err.println(" Could not fopen track file \"" + fname + "\": " + ex1.getLocalizedMessage());
            return IO_ERROR;
        }
        bf.getChannel().position(0);
        if (bf.skip(track.getStart()) != track.getStart()) {
            System.err.println(" Could not skip to track location");
            return IO_ERROR;
        }
        reallen = (track.getStopsect() - track.getStartsect() + 1) * track.getBsize();
        if (params.isVerbose()) {
            System.out.println(" mmc sectors " + track.getStartsect() + "->" + track.getStopsect() + " (" + (track.getStopsect() - track.getStartsect() + 1) + ")");
            System.out.println(" mmc bytes " + track.getStart() + "->" + track.getStop() + " (" + (track.getStop() - track.getStart() + 1) + ")");
            System.out.println(" sector data at " + track.getBstart() + ", " + track.getBsize() + " bytes per sector");
            System.out.println(" real data " + ((track.getStopsect() - track.getStartsect() + 1) * track.getBsize()) + " bytes");
            System.out.println();
        }
        System.out.print("                                          ");
        if (track.isAudio() && params.isToWav()) {
            f.write("RIFF".getBytes());
            l = reallen + Constants.WAV_DATA_HLEN + Constants.WAV_FORMAT_HLEN + 4;
            f.write(Helper.getIntBytes(l));
            f.write("WAVE".getBytes());
            f.write("fmt ".getBytes());
            l = 0x10;
            f.write(Helper.getIntBytes(l));
            i = 0x01;
            f.write(Helper.getShortBytes(i));
            i = 0x02;
            f.write(Helper.getShortBytes(i));
            l = 44100;
            f.write(Helper.getIntBytes(l));
            l = 44100 * 4;
            f.write(Helper.getIntBytes(l));
            i = 4;
            f.write(Helper.getShortBytes(i));
            i = 2 * 8;
            f.write(Helper.getShortBytes(i));
            f.write("data".getBytes());
            l = reallen;
            f.write(Helper.getIntBytes(l));
        }
        realsz = 0;
        sz = track.getStart();
        sect = track.getStartsect();
        fl = 0;
        while ((sect <= track.getStopsect()) && (bf.read(buf, 0, Constants.SECTLEN) > 0)) {
            if (track.isAudio()) {
                if (params.isSwabAudio()) {
                    p = track.getBstart();
                    ep = p + track.getBsize();
                    while (p < ep) {
                        p2 = p + 1;
                        c = buf[p];
                        buf[p] = buf[p2];
                        buf[p2] = c;
                        p += 2;
                    }
                }
            }
            try {
                f.write(buf, track.getBstart(), track.getBsize());
            } catch (IOException ex) {
                System.err.println(" Could not write to track: " + ex.getLocalizedMessage());
                System.exit(4);
            }
            sect++;
            sz += Constants.SECTLEN;
            realsz += track.getBsize();
            if (((sz / Constants.SECTLEN) % 500) == 0) {
                fl = (float) realsz / (float) reallen;
                System.out.print(String.format("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b%4d/%-4d MB  [%s] %3.0f %%", new Object[] { new Long(realsz >> 20), new Long(reallen >> 20), progressbar(fl, 20), new Float(fl * 100) }));
                System.out.flush();
            }
        }
        fl = (float) realsz / (float) reallen;
        System.out.print(String.format("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b%4d/%-4d MB  [%s] %3.0f %%", new Object[] { new Long(realsz >> 20), new Long(reallen >> 20), progressbar(1, 20), new Float(fl * 100) }));
        System.out.flush();
        System.out.println();
        return 0;
    }
