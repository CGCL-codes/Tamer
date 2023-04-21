    @Override
    public byte[] loadSE(String seName) {
        InputStream is = null;
        for (String ext : seExts) {
            try {
                URL u = resourceFetcher.getResource("/res/se/" + seName + "." + ext);
                if (u != null) is = u.openStream();
            } catch (IOException e) {
            }
            if (is != null) {
                break;
            }
        }
        if (is == null) {
            return null;
        }
        byte[] buf = new byte[65536];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        try {
            while ((read = is.read(buf)) >= 0) {
                baos.write(buf, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        is = new ByteArrayInputStream(baos.toByteArray());
        baos.reset();
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(format, AudioSystem.getAudioInputStream(is));
            while ((read = ais.read(buf)) >= 0) {
                baos.write(buf, 0, read);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ais.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
