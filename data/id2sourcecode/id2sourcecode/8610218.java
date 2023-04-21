    public int detectEncoding(URL testurl) {
        byte[] rawtext = new byte[10000];
        int bytesread = 0, byteoffset = 0;
        int guess = OTHER;
        InputStream chinesestream;
        try {
            chinesestream = testurl.openStream();
            while ((bytesread = chinesestream.read(rawtext, byteoffset, rawtext.length - byteoffset)) > 0) {
                byteoffset += bytesread;
            }
            ;
            chinesestream.close();
            guess = detectEncoding(rawtext);
        } catch (Exception e) {
            System.err.println("Error loading or using URL " + e.toString());
            guess = OTHER;
        }
        return guess;
    }
