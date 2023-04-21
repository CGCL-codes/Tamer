    @WebMethod(operationName = "getContent")
    public List<Float> getCoordsContent(@WebParam(name = "city") String city) {
        ArrayList<Float> ret = new ArrayList<Float>(2);
        try {
            InputStream is = new URL("http://ws.geonames.org/search?q=" + city.replace(" ", "%20").toLowerCase() + "&maxRows=1&lang=es").openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(8096);
            int read = 0;
            byte[] buff = new byte[8096];
            while (read != -1) {
                read = is.read(buff);
                if (read == -1) {
                    break;
                }
                baos.write(buff, 0, read);
            }
            String tmp = new String(baos.toByteArray());
            if (tmp != null && tmp.length() > 0) {
                String[] val;
                if ((val = tmp.split("<lat>")).length > 1) {
                    ret.add(Float.valueOf(val[1].split("</lat>")[0]));
                }
                if ((val = tmp.split("<lng>")).length > 1) {
                    ret.add(Float.valueOf(val[1].split("</lng>")[0]));
                }
            }
        } catch (IOException ex) {
            Log.getLogger().error(ex.getMessage());
        }
        return ret;
    }
