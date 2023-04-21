    public Element toXML(Document doc, String name) {
        Element format_ele;
        Element file_size_ele;
        Element bandwidth_ele;
        Element rate_ele;
        Element tc_rate_ele;
        format_ele = doc.createElementNS(Namespace.MPEG7, name);
        format_ele.setAttributeNS(Namespace.XSI, "xsi:type", "MediaFormatType");
        format_ele.appendChild(content.toXML(doc, "Content"));
        if (medium != null) format_ele.appendChild(medium.toXML(doc, "Medium"));
        if (file_format != null) format_ele.appendChild(file_format.toXML(doc, "FileFormat"));
        if (file_size != null) {
            file_size_ele = doc.createElement("FileSize");
            Utils.setContent(doc, file_size_ele, file_size.toString());
            format_ele.appendChild(file_size_ele);
        }
        if (system != null) format_ele.appendChild(system.toXML(doc, "System"));
        if (bandwidth != null) {
            bandwidth_ele = doc.createElement("Bandwidth");
            Utils.setContent(doc, bandwidth_ele, bandwidth.toString());
            format_ele.appendChild(bandwidth_ele);
        }
        if ((bit_rate != null) || (bit_rate_variable != null) || (bit_rate_minimum != null) || (bit_rate_average != null) || (bit_rate_maximum != null)) {
            rate_ele = doc.createElement("BitRate");
            format_ele.appendChild(rate_ele);
            if (bit_rate_variable != null) rate_ele.setAttribute("variable", bit_rate_variable.toString());
            if (bit_rate_minimum != null) rate_ele.setAttribute("minimum", bit_rate_minimum.toString());
            if (bit_rate_average != null) rate_ele.setAttribute("average", bit_rate_average.toString());
            if (bit_rate_maximum != null) rate_ele.setAttribute("maximum", bit_rate_maximum.toString());
            if (bit_rate != null) Utils.setContent(doc, rate_ele, bit_rate.toString());
        }
        if (target_channel_bit_rate != null) {
            tc_rate_ele = doc.createElement("TargetChannelBitRate");
            Utils.setContent(doc, tc_rate_ele, target_channel_bit_rate.toString());
            format_ele.appendChild(tc_rate_ele);
        }
        if (audio_coding != null) format_ele.appendChild(audio_coding.toXML(doc, "AudioCoding"));
        if (scene_coding_format != null) format_ele.appendChild(scene_coding_format.toXML(doc, "SceneCodingFormat"));
        if (graphics_coding_format != null) format_ele.appendChild(graphics_coding_format.toXML(doc, "GraphicsCodingFormat"));
        if (other_coding_format != null) format_ele.appendChild(other_coding_format.toXML(doc, "OtherCodingFormat"));
        return (format_ele);
    }
