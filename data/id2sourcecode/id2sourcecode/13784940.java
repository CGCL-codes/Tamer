    private void writeFixtures() throws IOException {
        out.openElement("fixtures");
        for (Channel channel : show.getChannels()) {
            out.openElement("fixture");
            out.addAttribute("id", channel.getId() + 1);
            out.addAttribute("name", channel.getName());
            out.closeElement();
        }
        out.closeElement();
    }
