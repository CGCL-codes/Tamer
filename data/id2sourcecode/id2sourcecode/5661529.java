    public String toXml() {
        String xml = "";
        xml += "  <monome>\n";
        xml += "    <prefix>" + this.prefix + "</prefix>\n";
        xml += "    <sizeX>" + this.sizeX + "</sizeX>\n";
        xml += "    <sizeY>" + this.sizeY + "</sizeY>\n";
        xml += "    <usePageChangeButton>" + (this.usePageChangeButton ? "true" : "false") + "</usePageChangeButton>\n";
        xml += "    <useMIDIPageChanging>" + (this.useMIDIPageChanging ? "true" : "false") + "</useMIDIPageChanging>\n";
        for (int i = 0; i < this.midiPageChangeRules.size(); i++) {
            MIDIPageChangeRule mpcr = this.midiPageChangeRules.get(i);
            if (mpcr != null) {
                xml += "    <MIDIPageChangeRule>\n";
                xml += "      <pageIndex>" + mpcr.getPageIndex() + "</pageIndex>\n";
                xml += "      <note>" + mpcr.getNote() + "</note>\n";
                xml += "      <channel>" + mpcr.getChannel() + "</channel>\n";
                xml += "    </MIDIPageChangeRule>\n";
            }
        }
        float[] min = this.adcObj.getMin();
        xml += "    <min>" + min[0] + "</min>\n";
        xml += "    <min>" + min[1] + "</min>\n";
        xml += "    <min>" + min[2] + "</min>\n";
        xml += "    <min>" + min[3] + "</min>\n";
        float[] max = this.adcObj.getMax();
        xml += "    <max>" + max[0] + "</max>\n";
        xml += "    <max>" + max[1] + "</max>\n";
        xml += "    <max>" + max[2] + "</max>\n";
        xml += "    <max>" + max[3] + "</max>\n";
        xml += "    <adcEnabled>" + this.adcObj.isEnabled() + "</adcEnabled>\n";
        for (int i = 0; i < this.numPages; i++) {
            if (this.pages.get(i).toXml() != null) {
                xml += "    <page class=\"" + this.pages.get(i).getClass().getName() + "\">\n";
                xml += this.pages.get(i).toXml();
                xml += "    </page>\n";
            }
        }
        for (int i = 0; i < this.numPages; i++) {
            int patternLength = this.patternBanks.get(i).getPatternLength();
            int quantization = this.patternBanks.get(i).getQuantization();
            xml += "    <patternlength>" + patternLength + "</patternlength>\n";
            xml += "    <quantization>" + quantization + "</quantization>\n";
        }
        xml += "  </monome>\n";
        return xml;
    }
