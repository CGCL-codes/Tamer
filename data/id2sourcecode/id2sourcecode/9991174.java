    @Override
    public EntrySet read(EntrySet set) throws ReadFailedException {
        if (content == null) throw new RuntimeException("Boenk!");
        if (set == null) set = new EntrySet();
        InputStream iis = null;
        if (index.isURL()) try {
            iis = index.url().openStream();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } else try {
            iis = new FileInputStream(index.file());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        FaidxIndex index = new FaidxIndex(iis);
        for (String name : index.names()) {
            Entry e = set.getOrCreateEntry(name);
            try {
                e.setSequence(new FaidxData(index, content, name));
            } catch (Exception ex) {
                System.err.println("Faidx error locator: " + data);
                System.err.println("Faidx error index locator: " + index);
                throw new ReadFailedException(ex);
            }
        }
        return set;
    }
