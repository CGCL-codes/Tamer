    private void processKeystoreFromLocation(String location) {
        InputStream in = null;
        char[] buff = new char[4096];
        int indexOf$ = location.indexOf("${");
        int indexOfCurly = location.indexOf('}', indexOf$);
        if (indexOf$ != -1 && indexOfCurly != -1) {
            String prop = FrameworkProperties.getProperty(location.substring(indexOf$ + 2, indexOfCurly));
            String location2 = location.substring(0, indexOf$);
            location2 += prop;
            location2 += location.substring(indexOfCurly + 1);
            location = location2;
        }
        try {
            URL url = new URL(location);
            in = url.openStream();
            Reader reader = new InputStreamReader(in);
            int result = reader.read(buff);
            StringBuffer contentBuff = new StringBuffer();
            while (result != -1) {
                contentBuff.append(buff, 0, result);
                result = reader.read(buff);
            }
            if (contentBuff.length() > 0) {
                String content = new String(contentBuff.toString());
                int indexOfKeystore = content.indexOf("keystore");
                if (indexOfKeystore != -1) {
                    int indexOfSemiColumn = content.indexOf(';', indexOfKeystore);
                    processKeystoreFromString(content.substring(indexOfKeystore, indexOfSemiColumn), url);
                    return;
                }
            }
        } catch (MalformedURLException e) {
            SignedBundleHook.log(e.getMessage(), FrameworkLogEntry.WARNING, e);
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
