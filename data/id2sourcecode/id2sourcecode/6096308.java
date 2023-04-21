    private static List<String> nacitajURL(final String url) {
        InputStream is = null;
        final List<String> ret = new LinkedList<String>();
        try {
            is = (new URL(url)).openStream();
            final BufferedReader dis = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String s = dis.readLine();
            while (s != null) {
                ret.add(s);
                s = dis.readLine();
            }
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Chybne URL:\n" + ClientUtils.getStackTrace(e));
            return null;
        } catch (IOException e) {
            logger.log(Level.WARNING, "I/O chyba:\n" + ClientUtils.getStackTrace(e));
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "I/O chyba:\n" + ClientUtils.getStackTrace(e));
            }
        }
        return ret;
    }
