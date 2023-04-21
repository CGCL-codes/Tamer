    public String getSiteContent(final Site site) throws HtmlSiteRecuperatorException {
        URL url;
        try {
            url = new URL(site.getAddress());
            final URLConnection connection = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;
            final StringBuffer strBuff = new StringBuffer();
            while ((str = in.readLine()) != null) {
                strBuff.append(str);
            }
            in.close();
            return strBuff.toString();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new HtmlSiteRecuperatorException(site, Language.getInstance().get("MalformedURL") + site.getName());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
