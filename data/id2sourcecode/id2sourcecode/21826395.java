    public final void writeAttrib() throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        out.putNextEntry(new ZipEntry("datagram"));
        Set keys = attributes.keySet();
        Iterator keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            Integer key = (Integer) keyIterator.next();
            Attrib attrib = (Attrib) attributes.get(key);
            Set propkeys = attrib.fields.keySet();
            Iterator propIterator = propkeys.iterator();
            while (propIterator.hasNext()) {
                String propkey = (String) propIterator.next();
                String prop = (String) attrib.fields.get(propkey);
                putZipElt(out, (byte) 11, propkey);
                putZipElt(out, (byte) 12, prop);
            }
            putZipElt(out, (byte) 41, attrib.datagram.stringValue);
        }
        out.close();
    }
