    private void writeGroups() throws IOException {
        out.openElement("groups");
        for (Group group : show.getGroups()) {
            out.openElement("group");
            out.addAttribute("name", group.getName());
            writeComment("");
            out.openElement("fixtures");
            for (Channel channel : group.getChannels()) {
                out.openElement("fixture");
                out.addAttribute("id", channel.getId() + 1);
                out.closeElement();
            }
            out.closeElement();
            out.closeElement();
        }
        out.closeElement();
    }
