    private static String[] performHeaderScan(File file, Pattern pattern) {
        final CharSequence chars;
        final Closeable closeable;
        try {
            final FileChannel channel = new FileInputStream(file).getChannel();
            final MappedByteBuffer byteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
            chars = UTF8.decode(byteBuffer);
            closeable = channel;
        } catch (IOException e) {
            return null;
        }
        try {
            final List<String> ans = new ArrayList<String>();
            final Matcher matcher = pattern.matcher(chars);
            while (matcher.find()) {
                if (matcher.groupCount() < 1) continue;
                final String header = matcher.group(1);
                ans.add(header);
                OutputChannel.HEADER.printf("header found: %s\n", header);
            }
            return ans.toArray(new String[ans.size()]);
        } finally {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
