    private boolean subtitleUpload(Movie movie, File movieFile[], File subtitleFile[]) {
        try {
            String ret = "";
            String xml = "";
            String idmovieimdb = movie.getId(ImdbPlugin.IMDB_PLUGIN_ID).substring(2);
            idmovieimdb = String.valueOf(Integer.parseInt(idmovieimdb));
            String subfilename[] = new String[movieFile.length];
            String subhash[] = new String[movieFile.length];
            String subcontent[] = new String[movieFile.length];
            String moviehash[] = new String[movieFile.length];
            String moviebytesize[] = new String[movieFile.length];
            String movietimems[] = new String[movieFile.length];
            String movieframes[] = new String[movieFile.length];
            String moviefps[] = new String[movieFile.length];
            String moviefilename[] = new String[movieFile.length];
            for (int i = 0; i < movieFile.length; i++) {
                subfilename[i] = subtitleFile[i].getName();
                subhash[i] = "";
                subcontent[i] = "";
                moviehash[i] = getHash(movieFile[i]);
                moviebytesize[i] = String.valueOf(movieFile[i].length());
                movietimems[i] = "";
                movieframes[i] = "";
                moviefps[i] = String.valueOf(movie.getFps());
                moviefilename[i] = movieFile[i].getName();
                FileInputStream f = new FileInputStream(subtitleFile[i]);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte s[] = new byte[f.available()];
                f.read(s);
                f.close();
                md.update(s);
                subhash[i] = hashstring(md.digest());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DeflaterOutputStream a = new DeflaterOutputStream(baos);
                a.write(s);
                a.finish();
                a.close();
                subcontent[i] = tuBase64(baos.toByteArray());
            }
            xml = generateXMLRPCTUS(subhash, subfilename, moviehash, moviebytesize, movietimems, movieframes, moviefps, moviefilename);
            ret = sendRPC(xml);
            String alreadyindb = getIntValue("alreadyindb", ret);
            if (!alreadyindb.equals("0")) {
                logger.finer("OpenSubtitles Plugin: Subtitle already in db for " + movie.getBaseName());
                return true;
            }
            logger.finer("OpenSubtitles Plugin: Upload Subtitle for " + movie.getBaseName());
            xml = generateXMLRPCUS(idmovieimdb, subhash, subcontent, subfilename, moviehash, moviebytesize, movietimems, movieframes, moviefps, moviefilename);
            ret = sendRPC(xml);
            return true;
        } catch (Exception e) {
            logger.severe("OpenSubtitles Plugin: Update Failed");
            return false;
        }
    }
