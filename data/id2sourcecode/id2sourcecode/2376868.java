    public String[] getWordsThatStartWith(String string) {
        List<String> result = new ArrayList<String>(10);
        string = string.toLowerCase();
        long startTime = System.currentTimeMillis();
        RandomAccessFile raf = null;
        try {
            File file = new File(DICTIONARY_PATH);
            raf = new RandomAccessFile(file, "r");
            long low = 0;
            long high = file.length();
            long p = -1;
            while (low < high) {
                long mid = (low + high) / 2;
                p = mid;
                while (p >= 0) {
                    raf.seek(p);
                    char c = (char) raf.readByte();
                    if (c == '\n') break;
                    p--;
                }
                if (p < 0) raf.seek(0);
                String line = raf.readLine();
                if (line == null) {
                    low = high;
                } else {
                    int compare = line.compareTo(string);
                    if (compare < 0) {
                        low = mid + 1;
                    } else if (compare == 0) {
                        low = p;
                        break;
                    } else {
                        high = mid;
                    }
                }
            }
            p = low;
            while (p >= 0 && p < high) {
                raf.seek(p);
                if (((char) raf.readByte()) == '\n') break;
                p--;
            }
            if (p < 0) raf.seek(0);
            String line = raf.readLine();
            while (line != null && line.startsWith(string)) {
                result.add(line);
                line = raf.readLine();
            }
        } catch (Throwable t) {
            Raptor.getInstance().onError("Error reading dictionary file: " + DICTIONARY_PATH, t);
        } finally {
            try {
                raf.close();
            } catch (Throwable t) {
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Searched " + string + " (" + (System.currentTimeMillis() - startTime) + ") " + result);
            }
        }
        return result.toArray(new String[0]);
    }
