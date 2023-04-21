    private Data processData(String nodeName) {
        Data data = new Data();
        boolean atEnd = false;
        while (!atEnd) {
            if (reader.isStartElement()) {
                byte[] b = textValue().getBytes();
                if (b.length > 0) {
                    QByteArray hexData = new QByteArray(b);
                    QByteArray binData = new QByteArray(QByteArray.fromHex(hexData));
                    data.setBody(binData.toByteArray());
                    MessageDigest md;
                    try {
                        md = MessageDigest.getInstance("MD5");
                        md.update(b);
                        data.setBodyHash(md.digest());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (reader.name().equalsIgnoreCase(nodeName) && reader.isEndElement()) atEnd = true; else reader.readNext();
        }
        return data;
    }
