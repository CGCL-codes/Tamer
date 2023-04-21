    private final long computeAssetsCheckSum(String sfExe) {
        InputStream is = null;
        try {
            is = context.getAssets().open(sfExe);
            if (sfExe.endsWith(".mygz")) is = new GZIPInputStream(is);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] buf = new byte[8192];
            while (true) {
                int len = is.read(buf);
                if (len <= 0) break;
                md.update(buf, 0, len);
            }
            byte[] digest = md.digest(new byte[] { 0 });
            long ret = 0;
            for (int i = 0; i < 8; i++) {
                ret ^= ((long) digest[i]) << (i * 8);
            }
            return ret;
        } catch (IOException e) {
            return -1;
        } catch (NoSuchAlgorithmException e) {
            return -1;
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException ex) {
            }
        }
    }
