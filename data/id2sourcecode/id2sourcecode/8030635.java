    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            String tmpStr = "";
            boolean flag = false;
            int count = 0;
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("name")) {
                    this.set_name(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("internalname")) {
                    this.set_internalname(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("externalname")) {
                    this.set_externalname(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("donotrender")) {
                    this.set_donotrender(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("storage")) {
                    tmpStr = CodeDOM_Utils.StringAtt_To_STRING(reader.Value());
                    count = 0;
                    flag = false;
                    for (int n = 0; n < CodeDOM_STV.XPLVARSTORAGE_ENUM.length; n++) {
                        String str = CodeDOM_STV.XPLVARSTORAGE_ENUM[n];
                        if (str.equals(tmpStr)) {
                            this.set_storage(count);
                            flag = true;
                            break;
                        }
                        count++;
                    }
                } else if (reader.Name().equals("doc")) {
                    this.set_doc(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("helpURL")) {
                    this.set_helpURL(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("ldsrc")) {
                    this.set_ldsrc(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("iny")) {
                    this.set_iny(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("inydata")) {
                    this.set_inydata(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("inyby")) {
                    this.set_inyby(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("lddata")) {
                    this.set_lddata(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("address")) {
                    this.set_address(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("atomicwrite")) {
                    this.set_atomicwrite(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("atomicread")) {
                    this.set_atomicread(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else {
                    throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Atributo '" + reader.Name() + "' invalido en elemento '" + this.get_Name() + "'.");
                }
            }
            reader.MoveToElement();
        }
        this.p_type = null;
        this.p_aliasref = null;
        this.p_i = null;
        if (!reader.IsEmptyElement()) {
            reader.Read();
            while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
                XplNode tempNode = null;
                switch(reader.NodeType()) {
                    case XmlNodeType.ELEMENT:
                        if (reader.Name().equals("type")) {
                            tempNode = new XplType();
                            tempNode.Read(reader);
                            if (this.get_type() != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' repetido como hijo de elemento '" + this.get_Name() + "'.");
                            this.set_type((XplType) tempNode);
                        } else if (reader.Name().equals("aliasref")) {
                            tempNode = new XplNode(XplNodeType_enum.STRING);
                            tempNode.Read(reader);
                            if (this.get_aliasref() != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' repetido como hijo de elemento '" + this.get_Name() + "'.");
                            this.set_aliasref(tempNode);
                        } else if (reader.Name().equals("i")) {
                            tempNode = new XplInitializerList();
                            tempNode.Read(reader);
                            if (this.get_i() != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' repetido como hijo de elemento '" + this.get_Name() + "'.");
                            this.set_i((XplInitializerList) tempNode);
                        } else {
                            throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nombre de nodo '" + reader.Name() + "' inesperado como hijo de elemento '" + this.get_Name() + "'.");
                        }
                        break;
                    case XmlNodeType.ENDELEMENT:
                        break;
                    case XmlNodeType.TEXT:
                        throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".No se esperaba texto en este nodo.");
                    default:
                        break;
                }
                reader.Read();
            }
        }
        return this;
    }
