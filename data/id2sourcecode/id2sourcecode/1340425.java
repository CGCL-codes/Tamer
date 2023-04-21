    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("typeStr")) {
                    this.set_typeStr(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("valueStr")) {
                    this.set_valueStr(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
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
                } else {
                    throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Atributo '" + reader.Name() + "' invalido en elemento '" + this.get_Name() + "'.");
                }
            }
            reader.MoveToElement();
        }
        this.p_texpression = null;
        if (!reader.IsEmptyElement()) {
            reader.Read();
            while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
                XplNode tempNode = null;
                switch(reader.NodeType()) {
                    case XmlNodeType.ELEMENT:
                        if (reader.Name().equals("a")) {
                            tempNode = new XplAssing();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("new")) {
                            tempNode = new XplNewExpression();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("bo")) {
                            tempNode = new XplBinaryoperator();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("uo")) {
                            tempNode = new XplUnaryoperator();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("to")) {
                            tempNode = new XplTernaryoperator();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("b")) {
                            tempNode = new XplFunctioncall();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("n")) {
                            tempNode = new XplNode(XplNodeType_enum.STRING);
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("lit")) {
                            tempNode = new XplLiteral();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("fc")) {
                            tempNode = new XplFunctioncall();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("cfc")) {
                            tempNode = new XplComplexfunctioncall();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("cast")) {
                            tempNode = new XplCastexpression();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("delete")) {
                            tempNode = new XplExpression();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("onpointer")) {
                            tempNode = new XplExpression();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("writecode")) {
                            tempNode = new XplWriteCodeBody();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("t")) {
                            tempNode = new XplType();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("toft")) {
                            tempNode = new XplType();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("is")) {
                            tempNode = new XplCastexpression();
                            tempNode.Read(reader);
                        } else if (reader.Name().equals("empty")) {
                            tempNode = new XplNode(XplNodeType_enum.EMPTY);
                            tempNode.Read(reader);
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
                if (this.get_texpression() != null && tempNode != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' incorrecto como hijo de elemento '" + this.get_Name() + "'."); else if (tempNode != null) this.set_texpression(tempNode);
                reader.Read();
            }
        }
        return this;
    }
