    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ZipFile zip = new ZipFile(new File(args[0]), ZipFile.OPEN_READ);
        Enumeration entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.getName().endsWith("arff.zip")) {
                File temp = File.createTempFile("PARSER", ".zip");
                temp.deleteOnExit();
                PrintStream writer = new PrintStream(new FileOutputStream(temp));
                BufferedInputStream reader = new BufferedInputStream(zip.getInputStream(entry));
                byte[] buffer = new byte[4096];
                int read = -1;
                while ((read = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, read);
                }
                writer.close();
                reader.close();
                Dataset dataset = new ArffHelper().read(temp);
                Information info = new Parser().read(dataset, CurveFactory.PRECISION_RECALL, new HashSet<String>(Arrays.asList(new String[] { "GO0003674", "GO0005575", "GO0008150" })));
                System.out.println(entry.getName() + ": AU(PRC) " + info.getCurve().area());
            }
        }
    }
