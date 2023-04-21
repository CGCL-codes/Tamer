    private boolean searchFile(File file) throws IOException {
        FileInputStream fis = null;
        FileChannel fc = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            CharBuffer cb = decoder.decode(bb);
            boolean matchFound = false;
            if ((targetPattern.flags() & Pattern.DOTALL) != 0) {
                if (targetMatcher == null) {
                    targetMatcher = targetPattern.matcher(cb);
                } else {
                    targetMatcher.reset(cb);
                }
                if (targetMatcher.find()) {
                    matchFound = true;
                }
            } else {
                Matcher lm = linePattern.matcher(cb);
                while (!matchFound && lm.find()) {
                    CharSequence cs = lm.group();
                    if (targetMatcher == null) {
                        targetMatcher = targetPattern.matcher(cs);
                    } else {
                        targetMatcher.reset(cs);
                    }
                    if (targetMatcher.find()) {
                        matchFound = true;
                    }
                    if (lm.end() == cb.limit()) {
                        break;
                    }
                }
            }
            return matchFound;
        } finally {
            if (fc != null) {
                fc.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }
