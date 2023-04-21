    public static void main(String[] args) throws IOException {
        InputStream in = null;
        String filepath;
        if (args.length > 0) {
            filepath = args[0];
        } else {
            filepath = "bean.xml";
        }
        try {
            URL url = new URL(filepath);
            in = url.openConnection().getInputStream();
        } catch (MalformedURLException e) {
            in = StartMe.class.getClassLoader().getResourceAsStream(filepath);
        }
        if (in != null) {
            StartMe s = new StartMe();
            final XMLObjectContainer xml = new XMLObjectContainer(in, s, StartMe.class.getClassLoader());
            in.close();
            xml.equals(null);
            Iterator i;
            System.out.println("~List");
            i = s.l.iterator();
            while (i.hasNext()) {
                System.out.println(i.next());
            }
        }
    }
