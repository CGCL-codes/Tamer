    public void store(String name, File file) {
        try {
            String md5 = null;
            if (_useHashes && file.length() > HASHABLE_BYTECOUNT) {
                try {
                    md5 = Tools.MD5File(file);
                    if (_index.containsHash(md5, file.length())) {
                        String hashed = _index.getHashed(md5, file.length());
                        if (!name.equals(hashed)) {
                            _index.alias(hashed, name);
                        }
                        return;
                    }
                } catch (NoSuchAlgorithmException e) {
                    _useHashes = false;
                    System.err.println("Hashes has been disabled! Hash algorithm " + Tools.HASH_ALGORITHM + " is not available here!");
                    e.printStackTrace();
                }
            }
            List<Segment> segments = new ArrayList<Segment>();
            FileChannel source = new FileInputStream(file).getChannel();
            for (final Segment segment : _index.freeSegments((int) source.size(), _contiguous)) {
                xferSegment(source, segment);
                segments.add(segment);
            }
            if (source.position() < source.size()) {
                Segment segment = new Segment(_channel, _index.end(), (int) (source.size() - source.position()));
                xferSegment(source, segment);
                segments.add(segment);
            }
            _index.addSegments(name, segments);
            _index.setHash(name, md5, file.length());
            flush(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
