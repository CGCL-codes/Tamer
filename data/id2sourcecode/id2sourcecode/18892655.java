    protected void writeProgrammeInfo(final Writer out, TVProgramme prog) throws IOException {
        out.write("  <programme start=\"" + DATE_FORMAT.format(new Date(prog.getStart())) + "\" stop=\"" + DATE_FORMAT.format(new Date(prog.getEnd())) + "\" channel=\"" + StringHelper.toXML(prog.getChannel().getID()) + "\">\n");
        out.write("    <title>" + StringHelper.toXML(prog.getTitle()) + "</title>\n");
        if (prog.getDescription() != null) {
            out.write("    <desc>" + StringHelper.toXML(prog.getDescription()) + "</desc>\n");
        }
        if (prog.getExtraTags() != null) {
            Iterator itExtra = prog.getExtraTags().keySet().iterator();
            while (itExtra.hasNext()) {
                String tag = (String) itExtra.next();
                Map attrs = (Map) prog.getExtraTags().get(tag);
                if (attrs != null) {
                    if ((attrs.size() == 1) && attrs.containsKey("")) {
                        out.write("    <" + tag + ">");
                        out.write(StringHelper.toXML((String) attrs.get("")));
                        out.write("</" + tag + ">\n");
                    } else {
                        out.write("    <" + tag);
                        Iterator itAttr = attrs.keySet().iterator();
                        while (itAttr.hasNext()) {
                            String attr = (String) itAttr.next();
                            String value = (String) attrs.get(attr);
                            out.write(" " + attr + "=\"");
                            out.write(StringHelper.toXML(value));
                            out.write("\"");
                        }
                        out.write("/>\n");
                    }
                }
            }
        }
        out.write("  </programme>\n");
    }
