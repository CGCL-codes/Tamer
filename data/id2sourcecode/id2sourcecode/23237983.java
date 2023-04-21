    private static void test_render() {
        try {
            SwKernel kernel = new SwKernel("/srv/www/htdocs/swemas", "org.swemas.data.xml.SwXml");
            SwXhtmlComposer cmp = new SwXhtmlComposer(kernel);
            Document doc = (Document) cmp.render(null, null).get(0);
            IXmlChannel ixml = (IXmlChannel) kernel.getChannel(IXmlChannel.class);
            File f = new File("/srv/www/htdocs/swemas/test_out.xml");
            FileOutputStream fs = new FileOutputStream(f);
            ixml.save(doc, fs);
        } catch (KernelException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        } catch (RenderingException e) {
            e.printStackTrace();
        }
    }
