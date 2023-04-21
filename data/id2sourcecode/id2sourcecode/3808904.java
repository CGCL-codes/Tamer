    public String debugPrintData() {
        String str = "";
        for (int i = 0; i < buf.length; i++) {
            String tmpstr = "" + buf[i];
            if (i == this.readOfs) {
                tmpstr = "(" + tmpstr + ")";
            }
            if (i == this.writeOfs) {
                tmpstr = "[" + tmpstr + "]";
            }
            str += " " + tmpstr;
        }
        return str + "   []:writeOfs ():readOfs";
    }
