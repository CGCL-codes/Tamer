    public static Map<String, Integer> addWordsToMap(String filename, Map<String, Integer> map) throws IOException {
        FileInputStream input = new FileInputStream(filename);
        FileChannel channel = input.getChannel();
        int fileLength = (int) channel.size();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = decoder.decode(buffer);
        Pattern linePattern = Pattern.compile(".*$", Pattern.MULTILINE);
        Pattern wordBreakPattern = Pattern.compile("[\\p{Punct}\\s}]");
        Matcher lineMatcher = linePattern.matcher(charBuffer);
        Integer ONE = new Integer(1);
        while (lineMatcher.find()) {
            CharSequence line = lineMatcher.group();
            String words[] = wordBreakPattern.split(line);
            for (int i = 0, n = words.length; i < n; i++) {
                if (words[i].length() > 0) {
                    Integer frequency = (Integer) map.get(words[i]);
                    if (frequency == null) {
                        frequency = ONE;
                    } else {
                        int value = frequency.intValue();
                        frequency = new Integer(value + 1);
                    }
                    map.put(words[i], frequency);
                }
            }
        }
        return map;
    }
