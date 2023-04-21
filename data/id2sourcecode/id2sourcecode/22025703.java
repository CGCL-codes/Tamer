    ObservationDataStruct getMultimediaObsData(ObservationDataStruct uriNode) {
        UniversalResourceIdentifier uri = UniversalResourceIdentifierHelper.extract(uriNode.value[0]);
        String location = uri.address;
        try {
            URL url = new URL(location);
            InputStream inputStream = new BufferedInputStream(url.openStream());
            int len = inputStream.available();
            byte[] bytes = new byte[len];
            inputStream.read(bytes, 0, len);
            inputStream.close();
            String filename = url.getFile();
            int pos = filename.lastIndexOf(".");
            String ext;
            if (pos > 0) {
                ext = filename.substring(pos + 1);
            } else {
                ext = "";
            }
            Multimedia multimedia = new Multimedia("image", ext, bytes, len, null);
            org.omg.CORBA.Any[] any = { orb.create_any() };
            MultimediaHelper.insert(any[0], multimedia);
            ObservationDataStruct multimediaNode = new ObservationDataStruct(XML.MultiMedia, new ObservationDataStruct[0], new ObservationDataStruct[0], any);
            return multimediaNode;
        } catch (java.io.IOException e) {
            cat.error("Can't open URL " + location + " ; error " + e);
            return uriNode;
        }
    }
