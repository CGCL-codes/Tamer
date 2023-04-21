    @Override
    public synchronized List<F> open(List<F> files, boolean complete, boolean isVerifying) throws IOException {
        List<F> ret = super.open(files, complete, isVerifying);
        bufMap = new HashMap<RandomAccessFile, MappedByteBuffer>(files.size());
        for (int i = 0; i < _fos.length; i++) {
            if (_files.get(i).length() >= Integer.MAX_VALUE) continue;
            try {
                bufMap.put(_fos[i], _fos[i].getChannel().map(complete ? MapMode.READ_ONLY : MapMode.READ_WRITE, 0, _files.get(i).length()));
                if (LOG.isDebugEnabled()) LOG.debug("mapped " + _files.get(i));
            } catch (IOException mapFailed) {
                if (LOG.isDebugEnabled()) LOG.debug("didn't map " + _files.get(i), mapFailed);
            }
        }
        return ret;
    }
