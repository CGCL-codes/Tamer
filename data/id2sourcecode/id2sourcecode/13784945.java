    private void writePatch() throws IOException {
        out.openElement("patch-lines");
        for (Dimmer dimmer : show.getDimmers()) {
            out.openElement("patch-line");
            out.addAttribute("id", dimmer.getId() + 1);
            out.addAttribute("name", dimmer.getName());
            if (dimmer.getChannelId() != -1) {
                out.addAttribute("fixture-id", dimmer.getChannelId() + 1);
            }
            out.closeElement();
        }
        out.closeElement();
    }
