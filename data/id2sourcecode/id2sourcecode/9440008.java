    public String readFile(String filename) {
        if (logger.isTraceEnabled()) {
            logger.trace("readFile(" + filename + ") - start");
        }
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(filename);
            FileChannel fChannel = fstream.getChannel();
            int size = (int) fChannel.size();
            CharBuffer cb = decoder.decode(fChannel.map(FileChannel.MapMode.READ_ONLY, 0, size));
            String returnString = cb.toString();
            if (logger.isTraceEnabled()) {
                logger.trace("readFile(" + filename + ") - end");
            }
            return returnString;
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("readFile(" + filename + ") - end");
        }
        return "";
    }
