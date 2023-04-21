    protected void saveAsXMLZip(File base) {
        FileOutputStream fout = null;
        XStream xs = getXStream();
        JDBMMapConverter cnv = new JDBMMapConverter(xs.getClassMapper(), "class", dm);
        xs.registerConverter(cnv);
        try {
            fout = new FileOutputStream(new File(base, "gameXml.zip"));
            ZipOutputStream zout = new ZipOutputStream(fout);
            OutputStreamWriter owriter = new OutputStreamWriter(zout);
            synchronized (maps) {
                long namesId = db.getRoot(0);
                if (namesId != 0) {
                    Map dir = (Map) db.fetch(namesId);
                    Map.Entry entry;
                    Map map;
                    String name;
                    long id;
                    for (Iterator i = dir.entrySet().iterator(); i.hasNext(); ) {
                        entry = (Map.Entry) i.next();
                        name = (String) entry.getKey();
                        id = ((Long) entry.getValue()).longValue();
                        if (id != namesId) {
                            map = getMap(name, false);
                            cnv.setMap(map);
                            ZipEntry zentry = new ZipEntry(name + ".xml");
                            zout.putNextEntry(zentry);
                            xs.toXML(map, owriter);
                            zout.closeEntry();
                        }
                    }
                    zout.close();
                }
            }
        } catch (IOException ex) {
            log.error("Unable to save as xml", ex);
            try {
                fout.close();
            } catch (IOException exe) {
            }
        }
    }
