    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_uniqueName != "") writer.WriteAttributeString("uniqueName", CodeDOM_Utils.Att_ToString(p_uniqueName));
        if (p_simpleName != "") writer.WriteAttributeString("simpleName", CodeDOM_Utils.Att_ToString(p_simpleName));
        if (p_minVersion != "") writer.WriteAttributeString("minVersion", CodeDOM_Utils.Att_ToString(p_minVersion));
        if (p_maxVersion != "") writer.WriteAttributeString("maxVersion", CodeDOM_Utils.Att_ToString(p_maxVersion));
        if (p_description != "") writer.WriteAttributeString("description", CodeDOM_Utils.Att_ToString(p_description));
        if (p_supportLevel != XplPlatformsupportlevel_enum.COMPLETE) writer.WriteAttributeString("supportLevel", CodeDOM_STV.XPLPLATFORMSUPPORTLEVEL_ENUM[(int) p_supportLevel]);
        if (p_operatingsystem != "") writer.WriteAttributeString("operatingsystem", CodeDOM_Utils.Att_ToString(p_operatingsystem));
        if (p_aplication != "") writer.WriteAttributeString("aplication", CodeDOM_Utils.Att_ToString(p_aplication));
        if (p_multitask != "Preemtive") writer.WriteAttributeString("multitask", CodeDOM_Utils.Att_ToString(p_multitask));
        if (p_memorymodel != XplMemorymodel_enum.LINEAL) writer.WriteAttributeString("memorymodel", CodeDOM_STV.XPLMEMORYMODEL_ENUM[(int) p_memorymodel]);
        if (p_defaultbitorder != XplBitorder_enum.IGNORE) writer.WriteAttributeString("defaultbitorder", CodeDOM_STV.XPLBITORDER_ENUM[(int) p_defaultbitorder]);
        if (p_defaultbyteorder != XplBitorder_enum.IGNORE) writer.WriteAttributeString("defaultbyteorder", CodeDOM_STV.XPLBITORDER_ENUM[(int) p_defaultbyteorder]);
        if (p_addresswidth != 32) writer.WriteAttributeString("addresswidth", CodeDOM_Utils.Att_ToString(p_addresswidth));
        if (p_databus != 32) writer.WriteAttributeString("databus", CodeDOM_Utils.Att_ToString(p_databus));
        if (p_commonregisterssize != 32) writer.WriteAttributeString("commonregisterssize", CodeDOM_Utils.Att_ToString(p_commonregisterssize));
        if (p_segments != 0) writer.WriteAttributeString("segments", CodeDOM_Utils.Att_ToString(p_segments));
        if (p_segmentsize != 0) writer.WriteAttributeString("segmentsize", CodeDOM_Utils.Att_ToString(p_segmentsize));
        if (p_threading != "") writer.WriteAttributeString("threading", CodeDOM_Utils.Att_ToString(p_threading));
        writer.WriteEndElement();
        return result;
    }
