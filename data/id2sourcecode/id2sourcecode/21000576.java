    private Class loadClass(String name, URL url) throws IOException {
        InputStream in = null;
        try {
            URLConnection c = url.openConnection();
            c.setAllowUserInteraction(false);
            in = HttpURLConnection.openConnectionCheckRedirects(c);
            int len = c.getContentLength();
            byte data[] = new byte[(len == -1) ? 4096 : len];
            int total = 0, n;
            while ((n = in.read(data, total, data.length - total)) >= 0) {
                if ((total += n) == data.length) {
                    if (len < 0) {
                        byte newdata[] = new byte[total * 2];
                        System.arraycopy(data, 0, newdata, 0, total);
                        data = newdata;
                    } else {
                        break;
                    }
                }
            }
            if (verbose) System.err.println("load:" + name + "\t" + total + "bytes");
            in.close();
            in = null;
            return defineClass(name, data, 0, total);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (in != null) {
                in.close();
                System.gc();
                System.runFinalization();
            }
        }
    }
