    @SuppressWarnings("unchecked")
    protected Image getImageFromPictStream(InputStream is) {
        try {
            java.lang.Object[] nullObjects = null;
            java.lang.Class<?>[] nullClasses = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] header = new byte[512];
            byte[] buf = new byte[4096];
            int retval = 0;
            int size = 0;
            Method m;
            baos.write(header, 0, 512);
            while ((retval = is.read(buf, 0, 4096)) > 0) baos.write(buf, 0, retval);
            baos.close();
            size = baos.size();
            if (size <= 0) return null;
            byte[] imgBytes = baos.toByteArray();
            Class c = Class.forName("quicktime.QTSession");
            m = c.getMethod("isInitialized", nullClasses);
            Boolean b = (Boolean) m.invoke(nullObjects, nullObjects);
            if (b.booleanValue() == false) {
                m = (Method) c.getMethod("open", nullClasses).invoke(null, nullObjects);
            }
            c = Class.forName("quicktime.util.QTHandle");
            Constructor con = c.getConstructor(new Class[] { imgBytes.getClass() });
            Object handle = con.newInstance(new Object[] { imgBytes });
            String s = new String("PICT");
            c = Class.forName("quicktime.util.QTUtils");
            m = c.getMethod("toOSType", new Class[] { s.getClass() });
            Integer type = (Integer) m.invoke(nullObjects, new Object[] { s });
            c = Class.forName("quicktime.std.image.GraphicsImporter");
            con = c.getConstructor(new Class[] { Integer.TYPE });
            Object importer = con.newInstance(new Object[] { type });
            m = c.getMethod("setDataHandle", new Class[] { Class.forName("quicktime.util." + "QTHandleRef") });
            m.invoke(importer, new Object[] { handle });
            m = c.getMethod("getNaturalBounds", nullClasses);
            Object rect = m.invoke(importer, nullObjects);
            c = Class.forName("quicktime.app.view.GraphicsImporterDrawer");
            con = c.getConstructor(new Class[] { importer.getClass() });
            Object iDrawer = con.newInstance(new Object[] { importer });
            m = rect.getClass().getMethod("getWidth", nullClasses);
            Integer width = (Integer) m.invoke(rect, nullObjects);
            m = rect.getClass().getMethod("getHeight", nullClasses);
            Integer height = (Integer) m.invoke(rect, nullObjects);
            Dimension d = new Dimension(width.intValue(), height.intValue());
            c = Class.forName("quicktime.app.view.QTImageProducer");
            con = c.getConstructor(new Class[] { iDrawer.getClass(), d.getClass() });
            Object producer = con.newInstance(new Object[] { iDrawer, d });
            if (producer instanceof ImageProducer) return (Toolkit.getDefaultToolkit().createImage((ImageProducer) producer));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
