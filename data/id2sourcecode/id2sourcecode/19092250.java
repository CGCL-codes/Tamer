    public String saveString() {
        StringBuffer sb = new StringBuffer(Save.st(this.ssn));
        sb.append(Save.st("c"));
        sb.append(Integer.toString(this.getChannel()));
        sb.append(Save.et("c"));
        sb.append(Save.st("t"));
        sb.append(Integer.toString(this.getCtrlType()));
        sb.append(Save.et("t"));
        sb.append(Save.st("name"));
        sb.append(this.getName());
        sb.append(Save.et("name"));
        sb.append(Save.et(this.ssn));
        return sb.toString();
    }
