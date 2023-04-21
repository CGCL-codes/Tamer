    private void getOnlineSeriesData() {
        String id;
        if (seriesUUID == null) id = String.valueOf(Math.abs(new Random().nextInt())); else id = seriesUUID;
        new File("resources/seriesXMLs/" + language + "/" + id).mkdirs();
        File data_xml_file = new File("resources/seriesXMLs/" + language + "/" + id + "/data.ser");
        File banners_xml_file = new File("resources/seriesXMLs/" + language + "/" + id + "/banners.ser");
        File actors_xml_file = new File("resources/seriesXMLs/" + language + "/" + id + "/actors.ser");
        try {
            SAXBuilder builder = new SAXBuilder();
            String zippath = getRandomZipmirror();
            URL url = new URL(zippath + "/api/" + API_KEY + "/series/" + KEY_VALUE_ID + "/all/" + language + ".zip");
            URLConnection con = url.openConnection();
            con.setReadTimeout(TIMEOUT);
            ZipInputStream zipstream = new ZipInputStream(con.getInputStream());
            ZipEntry entry = null;
            while ((entry = zipstream.getNextEntry()) != null) {
                ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                int bytesRead;
                byte[] tempBuffer = new byte[8192 * 2];
                while ((bytesRead = zipstream.read(tempBuffer)) != -1) {
                    streamBuilder.write(tempBuffer, 0, bytesRead);
                }
                if (entry.getName().equals(language + ".xml")) {
                    FileOutputStream fo_stream = new FileOutputStream(data_xml_file);
                    streamBuilder.writeTo(fo_stream);
                    data_xml = builder.build(data_xml_file).getRootElement();
                    fo_stream.close();
                } else if (entry.getName().equals("banners.xml")) {
                    FileOutputStream fo_stream = new FileOutputStream(banners_xml_file);
                    streamBuilder.writeTo(fo_stream);
                    banners_xml = builder.build(banners_xml_file).getRootElement();
                    fo_stream.close();
                } else if (entry.getName().equals("actors.xml")) {
                    FileOutputStream fo_stream = new FileOutputStream(actors_xml_file);
                    streamBuilder.writeTo(fo_stream);
                    actors_xml = builder.build(actors_xml_file).getRootElement();
                    fo_stream.close();
                }
            }
        } catch (Exception E) {
            log.error("Could not get series data", E);
        } finally {
            if (!cacheData || seriesUUID == null) {
                data_xml_file.delete();
                banners_xml_file.delete();
                actors_xml_file.delete();
                new File("resources/seriesXMLs/" + language + "/" + id).delete();
                new File("resources/seriesXMLs/" + language).delete();
                new File("resources/seriesXMLs").delete();
            }
        }
    }
