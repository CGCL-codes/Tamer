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
        try {
            IndexedMAF maf = new IndexedMAF(content, iis);
            for (String name : maf.getNames()) {
                String[] nameParts = name.split("\\.");
                String chrom = nameParts[nameParts.length - 1];
                Entry e = null;
                if (set.getEntry(chrom) != null) {
                    e = set.getOrCreateEntry(chrom);
                } else {
                    e = set.getOrCreateEntry(name);
                }
                IndexedMAF idxMaf = new IndexedMAF(name, maf);
                e.add(Type.get(data.toString()), idxMaf);
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Mafix error data: " + data + "\n\n" + "Mafix error index: " + index, ex);
            throw new ReadFailedException(ex);
        }
        return set;
    }
