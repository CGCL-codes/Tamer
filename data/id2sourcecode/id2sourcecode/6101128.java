    @Override
    public int importIP4CountryCSV() {
        try {
            getIp4CountryDAO().deleteAll();
            final URL url = new URL(this.updateUrl);
            final URLConnection conn = url.openConnection();
            final InputStream istream = conn.getInputStream();
            final BufferedReader in = new BufferedReader(new InputStreamReader(istream));
            try {
                final Pattern splitterPattern = Pattern.compile(",");
                int counter = 0;
                String aLine = null;
                while (null != (aLine = in.readLine())) {
                    final String[] array = splitterPattern.split(aLine.trim());
                    final long ip = Long.parseLong(array[0]);
                    final long country = Long.parseLong(array[1]);
                    final Ip4Country tmp = this.ip4CountryDAO.getNewIp4Country();
                    tmp.setI4coCcdId(country);
                    tmp.setI4coIp(ip);
                    getIp4CountryDAO().saveOrUpdate(tmp);
                    if (logger.isDebugEnabled() && ++counter % 100 == 0) {
                        logger.debug("Aktueller Zaehler: " + counter);
                    }
                }
            } finally {
                in.close();
                istream.close();
            }
            return getIp4CountryDAO().getCountAllIp4Countries();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
