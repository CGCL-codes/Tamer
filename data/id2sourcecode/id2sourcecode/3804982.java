    private void genGetTagsSwitchForComponent(DomFixComponent m, final BufferedWriter out) throws IOException {
        out.write("\t\tdo {\n");
        out.write("\t\t\t" + strReadableByteBuffer + " value;\n\n");
        out.write("\t\t\tvalue = buf;\n\n");
        out.write("\t\t\tswitch( id ) {\n\n");
        for (DomBase b : m.fieldsAndComponents) {
            if (b instanceof DomFixField) {
                DomFixField f = (DomFixField) b;
                if (f.name.contains("BeginString") || f.name.contains("BodyLength") || f.name.contains("MsgType")) continue;
                out.write("\t\t\tcase FixTags." + f.name.toUpperCase() + "_INT:\n");
                decodeFieldValue(null, f, out);
                if (f.domFixValues.size() > 0) {
                    out.write("\t\t\t\tif (!" + capFirst(f.name) + ".isValid(" + uncapFirst(f.name) + ") ) " + "throw new FixSessionException(SessionRejectReason.VALUE_IS_INCORRECT_OUT_OF_RANGE_FOR_THIS_TAG, (\"Invalid enumerated value(\" + " + uncapFirst(f.name) + " + \") for tag\").getBytes(), id, new byte[0] );\n");
                }
                out.write("\t\t\t\tbreak;\n\n");
            }
            if (b instanceof DomFixComponentRef) {
                DomFixComponentRef c = (DomFixComponentRef) b;
                if (c.isRepeating()) {
                    out.write("\t\t\tcase FixTags." + c.noInGroupTag().toUpperCase() + "_INT:\n");
                    out.write("\t\t\t\t" + uncapFirst(c.name) + "." + uncapFirst(c.noInGroupTag()) + " = FixUtils.getTagIntValue(null, FixTags." + c.noInGroupTag().toUpperCase() + "_INT, value );\n");
                    out.write("\t\t\t\t" + uncapFirst(c.name) + ".getAll(" + uncapFirst(c.name) + "." + uncapFirst(c.noInGroupTag()) + ", value );\n");
                } else {
                    out.write("\t\t\tcase FixTags." + c.getKeyTag().toUpperCase() + "_INT:\n");
                    out.write("\t\t\t\t" + uncapFirst(c.name) + ".getAll( FixTags." + c.getKeyTag().toUpperCase() + "_INT, value );\n");
                }
                out.write("\t\t\t\tbreak;\n\n");
            }
        }
        out.write("\t\t\t// we will always endup with unknown tag, unread and return to upper layer in hierarchy\n");
        out.write("\t\t\tdefault:\n");
        out.write("\t\t\t\tid = checkRequiredTags();\n");
        out.write("\t\t\t\tif (id > 0) throw new FixSessionException(SessionRejectReason.REQUIRED_TAG_MISSING, \"Required tag missing\".getBytes(), id, new byte[0] );\n\n");
        out.write("\t\t\t\tbuf.position( lastTagPosition );\n");
        out.write("\t\t\t\treturn;\n\n");
        out.write("\t\t\t}\n\n");
        out.write("\t\t\tlastTagPosition = buf.position();\n\n");
        out.write("\t\t} while ( ( id = FixUtils.getTagId( buf ) ) >= 0 );\n\n");
        out.write("\t\tbuf.position(startTagPosition);\n\n");
    }
