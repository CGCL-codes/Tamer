    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digest = ");
        byte[] digest = this.digest();
        if (digest != null) {
            for (int i = 0; i < digest.length; i++) {
                int d = 0xff & digest[i];
                if (d < 0x10) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(d));
            }
        }
        sb.append("\n[");
        sb.append(Fixed1616.toString(sfntVersion));
        sb.append(", ");
        sb.append(this.numTables());
        sb.append("]\n");
        Iterator<? extends Table> iter = this.iterator();
        while (iter.hasNext()) {
            FontDataTable table = iter.next();
            sb.append("\t");
            sb.append(table);
            sb.append("\n");
        }
        return sb.toString();
    }
