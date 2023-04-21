    public static synchronized void writeXML(final Collection<StoredObject> objects, final File file) {
        Logger logger = Logger.getLogger(XMLContainer.class.getName());
        if (file.exists()) {
            File backup = new File(file.getAbsolutePath() + ".backup");
            if (backup.exists()) {
                if (!backup.delete()) {
                    logger.log(Level.WARNING, "Was not able to delete the old backup file: {0}", backup.getAbsolutePath());
                }
            }
            try {
                FileUtils.copyFile(file, backup);
            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE, null, e);
            }
        }
        List<StoredObject> list = new ArrayList<StoredObject>();
        list.addAll(query(objects, Budget.class));
        list.addAll(query(objects, Config.class));
        list.addAll(query(objects, CommodityNode.class));
        list.addAll(query(objects, ExchangeRate.class));
        list.addAll(query(objects, RootAccount.class));
        list.addAll(query(objects, Reminder.class));
        Iterator<StoredObject> i = list.iterator();
        while (i.hasNext()) {
            StoredObject o = i.next();
            if (o.isMarkedForRemoval()) {
                i.remove();
            }
        }
        Collections.sort(list, new StoredObjectComparator());
        logger.info("Writing XML file");
        ObjectOutputStream out = null;
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            try {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<?fileVersion " + Engine.CURRENT_VERSION + "?>\n");
                XStream xstream = configureXStream(new XStream(new PureJavaReflectionProvider(), new KXml2Driver()));
                out = xstream.createObjectOutputStream(new PrettyPrintWriter(writer));
                out.writeObject(list);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, null, e);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        logger.info("Writing XML file complete");
    }
