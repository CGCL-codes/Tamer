    protected byte[] getMD5Hash(URLBuilder builder, Artifact artifact) throws SavantException {
        byte[] bytes = null;
        URL url = builder.buildMD5URL(defaultDomain, mapping, artifact);
        if (url != null) {
            URLConnection uc = openConnection(url);
            if (isValid(uc)) {
                StringBuffer buf = new StringBuffer();
                try {
                    InputStream is = uc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    char[] c = new char[1024];
                    int count;
                    while ((count = br.read(c, 0, 1024)) != -1) {
                        for (int i = 0; i < count; i++) {
                            if (Character.isWhitespace(c[i])) {
                                continue;
                            }
                            buf.append(c[i]);
                        }
                    }
                    br.close();
                    isr.close();
                    is.close();
                    if (buf.length() > 0) {
                        bytes = StringTools.fromHex(buf.toString());
                    }
                } catch (IOException ioe) {
                    Log.log("Unable to download MD5 (skipping) for artifact [" + artifact + "]", Log.DEBUG);
                } catch (IllegalArgumentException iae) {
                    Log.log("Unable to download MD5 (skipping) for artifact [" + artifact + "]", Log.DEBUG);
                }
            }
        }
        return bytes;
    }
