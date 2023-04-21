    @Override
    public int importIP2CountryCSV() {
        try {
            getIpToCountryDAO().deleteAll();
            final URL url = new URL(this.updateUrl);
            final URLConnection conn = url.openConnection();
            final InputStream istream = conn.getInputStream();
            final ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(istream));
            zipInputStream.getNextEntry();
            final BufferedReader in = new BufferedReader(new InputStreamReader(zipInputStream));
            final CsvBeanReader csvb = new CsvBeanReader(in, CsvPreference.STANDARD_PREFERENCE);
            IpToCountry tmp = null;
            int id = 1;
            while (null != (tmp = csvb.read(IpToCountry.class, this.stringNameMapping))) {
                tmp.setId(id++);
                getIpToCountryDAO().saveOrUpdate(tmp);
            }
            in.close();
            return getIpToCountryDAO().getCountAllIpToCountries();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
