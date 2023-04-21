    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_name != "") writer.WriteAttributeString("name", CodeDOM_Utils.Att_ToString(p_name));
        if (p_internalname != "") writer.WriteAttributeString("internalname", CodeDOM_Utils.Att_ToString(p_internalname));
        if (p_externalname != "") writer.WriteAttributeString("externalname", CodeDOM_Utils.Att_ToString(p_externalname));
        if (p_access != XplAccesstype_enum.PRIVATE) writer.WriteAttributeString("access", CodeDOM_STV.XPLACCESSTYPE_ENUM[(int) p_access]);
        if (p_storage != XplVarstorage_enum.AUTO) writer.WriteAttributeString("storage", CodeDOM_STV.XPLVARSTORAGE_ENUM[(int) p_storage]);
        if (p_doc != "") writer.WriteAttributeString("doc", CodeDOM_Utils.Att_ToString(p_doc));
        if (p_helpURL != "") writer.WriteAttributeString("helpURL", CodeDOM_Utils.Att_ToString(p_helpURL));
        if (p_ldsrc != "") writer.WriteAttributeString("ldsrc", CodeDOM_Utils.Att_ToString(p_ldsrc));
        if (p_iny != false) writer.WriteAttributeString("iny", CodeDOM_Utils.Att_ToString(p_iny));
        if (p_inydata != "") writer.WriteAttributeString("inydata", CodeDOM_Utils.Att_ToString(p_inydata));
        if (p_inyby != "") writer.WriteAttributeString("inyby", CodeDOM_Utils.Att_ToString(p_inyby));
        if (p_lddata != "") writer.WriteAttributeString("lddata", CodeDOM_Utils.Att_ToString(p_lddata));
        if (p_donotrender != false) writer.WriteAttributeString("donotrender", CodeDOM_Utils.Att_ToString(p_donotrender));
        if (p_firstbit != 0) writer.WriteAttributeString("firstbit", CodeDOM_Utils.Att_ToString(p_firstbit));
        if (p_lastbit != 0) writer.WriteAttributeString("lastbit", CodeDOM_Utils.Att_ToString(p_lastbit));
        if (p_address != "") writer.WriteAttributeString("address", CodeDOM_Utils.Att_ToString(p_address));
        if (p_atomicwrite != false) writer.WriteAttributeString("atomicwrite", CodeDOM_Utils.Att_ToString(p_atomicwrite));
        if (p_atomicread != false) writer.WriteAttributeString("atomicread", CodeDOM_Utils.Att_ToString(p_atomicread));
        if (p_isfactory != false) writer.WriteAttributeString("isfactory", CodeDOM_Utils.Att_ToString(p_isfactory));
        if (p_new != false) writer.WriteAttributeString("new", CodeDOM_Utils.Att_ToString(p_new));
        if (p_type != null) if (!p_type.Write(writer)) result = false;
        if (p_i != null) if (!p_i.Write(writer)) result = false;
        writer.WriteEndElement();
        return result;
    }
