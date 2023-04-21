    public XMLWeatherReader(String cityName) throws IOException {
        String locationId = CityCodes.getString(cityName);
        try {
            URL url = new URL(baseUrl + "?p=" + locationId + "&u=" + unit);
            URLConnection urlconn = url.openConnection();
            InputStream in = urlconn.getInputStream();
            SAXBuilder builder = new SAXBuilder();
            this.docin = builder.build(in);
            root = this.docin.getRootElement();
            channel = root.getChild("channel");
            item = channel.getChild("item");
        } catch (JDOMException e) {
            throw new IOException(e.getMessage());
        }
    }
