    public void writeSongIntoDatabase(String name, int p, File f) {
        String insertStmt = "INSERT INTO wavs (songid, filename, wav, samplerate, framesize, stereo, bigend, ssizeinbits, time, signed) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            long modtime = f.lastModified();
            AudioInputStream AFStreamA = AudioSystem.getAudioInputStream(f);
            AudioFormat afFormat = AFStreamA.getFormat();
            if (afFormat.isBigEndian()) {
                AudioFormat targetFormat = new AudioFormat(afFormat.getEncoding(), afFormat.getSampleRate(), afFormat.getSampleSizeInBits(), afFormat.getChannels(), afFormat.getFrameSize(), afFormat.getFrameRate(), false);
                afFormat = targetFormat;
            }
            if (afFormat.getEncoding().toString().startsWith("MPEG")) {
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, afFormat.getSampleRate(), 16, afFormat.getChannels(), afFormat.getChannels() * 2, afFormat.getSampleRate(), false);
                afFormat = targetFormat;
            }
            AudioInputStream AFStream = AudioSystem.getAudioInputStream(afFormat, AFStreamA);
            double samplerate = AFStream.getFormat().getSampleRate();
            int stereo = AFStream.getFormat().getChannels();
            int FrameSize = AFStream.getFormat().getFrameSize();
            int length = (int) (FrameSize * AFStream.getFrameLength());
            int bigend = 0;
            if (AFStream.getFormat().isBigEndian()) {
                bigend = 1;
            }
            AudioFormat.Encoding afe = AFStream.getFormat().getEncoding();
            int signed = 0;
            if (afe.toString().startsWith("PCM_SIGNED")) {
                signed = 1;
            }
            int ssizebits = AFStream.getFormat().getSampleSizeInBits();
            System.out.println(samplerate + " " + stereo + " " + FrameSize + " " + length + " " + afe.toString());
            stmt = con.prepareStatement(insertStmt);
            stmt.setString(2, name);
            stmt.setInt(1, p);
            stmt.setBinaryStream(3, AFStream, length);
            stmt.setDouble(4, samplerate);
            stmt.setInt(5, FrameSize);
            stmt.setInt(6, stereo);
            stmt.setInt(7, bigend);
            stmt.setInt(8, ssizebits);
            stmt.setLong(9, modtime);
            stmt.setInt(10, signed);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }
