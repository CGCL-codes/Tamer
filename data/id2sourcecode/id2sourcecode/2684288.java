    public DataDDS getDataFromUrl(URL url, StatusUI statusUI, BaseTypeFactory btf) throws MalformedURLException, IOException, ParseException, DDSException, DODSException {
        InputStream is = openConnection(url);
        DataDDS dds = new DataDDS(ver, btf);
        ByteArrayInputStream bis = null;
        if (dumpStream) {
            System.out.println("DConnect to " + url);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            copy(is, bos);
            bis = new ByteArrayInputStream(bos.toByteArray());
            is = bis;
        }
        try {
            if (dumpStream) {
                bis.mark(1000);
                System.out.println("DConnect parse header: ");
                dump(bis);
                bis.reset();
            }
            dds.parse(new HeaderInputStream(is));
            if (dumpStream) {
                bis.mark(20);
                System.out.println("DConnect done with header, next bytes are: ");
                dumpBytes(bis, 20);
                bis.reset();
            }
            dds.readData(is, statusUI);
        } catch (Exception e) {
            System.out.println("DConnect dds.parse: " + url + "\n " + e);
            e.printStackTrace();
            throw new DODSException("Connection cannot be read " + url);
        } finally {
            is.close();
            if (connection instanceof HttpURLConnection) ((HttpURLConnection) connection).disconnect();
        }
        return dds;
    }
