    public static void writeChannelsToFile(List<Channel> channels, String fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.write("<opml version=\"1.0\">\n");
        out.write("<head>\n");
        out.write("<title>Subskrypcje kanałów</title>\n");
        out.write("</head>\n");
        out.write("<body>\n");
        Collections.sort(channels, channelComparator);
        for (Channel channel : channels) {
            if (channel.getTags().size() == 0) {
                out.write("<outline text=\"");
                out.write(channel.getTitle());
                out.write("\" title=\"");
                out.write(channel.getTitle());
                out.write("\" type=\"rss\"\n");
                out.write("    xmlUrl=\"");
                out.write(channel.getChannelURL().replace("&", "&amp;"));
                out.write("\" htmlUrl=\"");
                out.write(channel.getLink());
                out.write("\"/>\n");
            }
        }
        for (String tag : JReader.getTags()) {
            out.write("<outline title=\"" + tag + "\" text=\"" + tag + "\">\n");
            for (Channel channel : channels) {
                if (channel.containsTag(tag)) {
                    out.write("    <outline text=\"");
                    out.write(channel.getTitle());
                    out.write("\" title=\"");
                    out.write(channel.getTitle());
                    out.write("\" type=\"rss\"\n");
                    out.write("        xmlUrl=\"");
                    out.write(channel.getChannelURL().replace("&", "&amp;"));
                    out.write("\" htmlUrl=\"");
                    out.write(channel.getLink());
                    out.write("\"/>\n");
                }
            }
            out.write("</outline>\n");
        }
        out.write("</body>\n");
        out.write("</opml>\n");
        out.close();
    }
