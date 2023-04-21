    public Double getElevation(final double longitude, final double latitude) {
        try {
            final URL url = new URL("http://gisdata.usgs.net/xmlwebservices2/elevation_service.asmx/getElevation?X_Value=" + longitude + "&Y_Value=" + latitude + "&Elevation_Units=meters&Elevation_Only=true&Source_Layer=-1");
            final InputStream is = url.openStream();
            final Element elevationElement = XmlHelper.createElementNoValidate(is);
            if (elevationElement != null) {
                final String value = elevationElement.getTextTrim();
                if (value != null && !"".equals(value)) {
                    return Double.parseDouble(value);
                }
            }
        } catch (NumberFormatException e) {
            LOG.error("NumberFormatException while parsing the elevation as a double", e);
        } catch (IOException e) {
            LOG.error("IOException while reading the USGS web service", e);
        } catch (JDOMException e) {
            LOG.error("JDOMException while parsing the USGS web service response", e);
        }
        return null;
    }
