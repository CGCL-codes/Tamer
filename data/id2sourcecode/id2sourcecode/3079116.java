    public boolean setFieldProperty(String field, String name, Object value, int inst[]) {
        if (writer == null) throw new RuntimeException("This AcroFields instance is read-only.");
        try {
            Item item = (Item) fields.get(field);
            if (item == null) return false;
            InstHit hit = new InstHit(inst);
            if (name.equalsIgnoreCase("textfont")) {
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfString da = (PdfString) PdfReader.getPdfObject(((PdfDictionary) item.merged.get(k)).get(PdfName.DA));
                        PdfDictionary dr = (PdfDictionary) PdfReader.getPdfObject(((PdfDictionary) item.merged.get(k)).get(PdfName.DR));
                        if (da != null && dr != null) {
                            Object dao[] = splitDAelements(da.toUnicodeString());
                            PdfAppearance cb = new PdfAppearance();
                            if (dao[DA_FONT] != null) {
                                BaseFont bf = (BaseFont) value;
                                PdfName psn = (PdfName) PdfAppearance.stdFieldFontNames.get(bf.getPostscriptFontName());
                                if (psn == null) {
                                    psn = new PdfName(bf.getPostscriptFontName());
                                }
                                PdfDictionary fonts = (PdfDictionary) PdfReader.getPdfObject(dr.get(PdfName.FONT));
                                if (fonts == null) {
                                    fonts = new PdfDictionary();
                                    dr.put(PdfName.FONT, fonts);
                                }
                                PdfIndirectReference fref = (PdfIndirectReference) fonts.get(psn);
                                PdfDictionary top = (PdfDictionary) PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM));
                                markUsed(top);
                                dr = (PdfDictionary) PdfReader.getPdfObject(top.get(PdfName.DR));
                                if (dr == null) {
                                    dr = new PdfDictionary();
                                    top.put(PdfName.DR, dr);
                                }
                                markUsed(dr);
                                PdfDictionary fontsTop = (PdfDictionary) PdfReader.getPdfObject(dr.get(PdfName.FONT));
                                if (fontsTop == null) {
                                    fontsTop = new PdfDictionary();
                                    dr.put(PdfName.FONT, fontsTop);
                                }
                                markUsed(fontsTop);
                                PdfIndirectReference frefTop = (PdfIndirectReference) fontsTop.get(psn);
                                if (frefTop != null) {
                                    if (fref == null) fonts.put(psn, frefTop);
                                } else if (fref == null) {
                                    FontDetails fd;
                                    if (bf.getFontType() == BaseFont.FONT_TYPE_DOCUMENT) {
                                        fd = new FontDetails(null, ((DocumentFont) bf).getIndirectReference(), bf);
                                    } else {
                                        bf.setSubset(false);
                                        fd = writer.addSimple(bf);
                                        localFonts.put(psn.toString().substring(1), bf);
                                    }
                                    fontsTop.put(psn, fd.getIndirectReference());
                                    fonts.put(psn, fd.getIndirectReference());
                                }
                                ByteBuffer buf = cb.getInternalBuffer();
                                buf.append(psn.getBytes()).append(' ').append(((Float) dao[DA_SIZE]).floatValue()).append(" Tf ");
                                if (dao[DA_COLOR] != null) cb.setColorFill((Color) dao[DA_COLOR]);
                                PdfString s = new PdfString(cb.toString());
                                ((PdfDictionary) item.merged.get(k)).put(PdfName.DA, s);
                                ((PdfDictionary) item.widgets.get(k)).put(PdfName.DA, s);
                                markUsed((PdfDictionary) item.widgets.get(k));
                            }
                        }
                    }
                }
            } else if (name.equalsIgnoreCase("textcolor")) {
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfString da = (PdfString) PdfReader.getPdfObject(((PdfDictionary) item.merged.get(k)).get(PdfName.DA));
                        if (da != null) {
                            Object dao[] = splitDAelements(da.toUnicodeString());
                            PdfAppearance cb = new PdfAppearance();
                            if (dao[DA_FONT] != null) {
                                ByteBuffer buf = cb.getInternalBuffer();
                                buf.append(new PdfName((String) dao[DA_FONT]).getBytes()).append(' ').append(((Float) dao[DA_SIZE]).floatValue()).append(" Tf ");
                                cb.setColorFill((Color) value);
                                PdfString s = new PdfString(cb.toString());
                                ((PdfDictionary) item.merged.get(k)).put(PdfName.DA, s);
                                ((PdfDictionary) item.widgets.get(k)).put(PdfName.DA, s);
                                markUsed((PdfDictionary) item.widgets.get(k));
                            }
                        }
                    }
                }
            } else if (name.equalsIgnoreCase("textsize")) {
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfString da = (PdfString) PdfReader.getPdfObject(((PdfDictionary) item.merged.get(k)).get(PdfName.DA));
                        if (da != null) {
                            Object dao[] = splitDAelements(da.toUnicodeString());
                            PdfAppearance cb = new PdfAppearance();
                            if (dao[DA_FONT] != null) {
                                ByteBuffer buf = cb.getInternalBuffer();
                                buf.append(new PdfName((String) dao[DA_FONT]).getBytes()).append(' ').append(((Float) value).floatValue()).append(" Tf ");
                                if (dao[DA_COLOR] != null) cb.setColorFill((Color) dao[DA_COLOR]);
                                PdfString s = new PdfString(cb.toString());
                                ((PdfDictionary) item.merged.get(k)).put(PdfName.DA, s);
                                ((PdfDictionary) item.widgets.get(k)).put(PdfName.DA, s);
                                markUsed((PdfDictionary) item.widgets.get(k));
                            }
                        }
                    }
                }
            } else if (name.equalsIgnoreCase("bgcolor") || name.equalsIgnoreCase("bordercolor")) {
                PdfName dname = (name.equalsIgnoreCase("bgcolor") ? PdfName.BG : PdfName.BC);
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfObject obj = PdfReader.getPdfObject(((PdfDictionary) item.merged.get(k)).get(PdfName.MK));
                        markUsed(obj);
                        PdfDictionary mk = (PdfDictionary) obj;
                        if (mk == null) {
                            if (value == null) return true;
                            mk = new PdfDictionary();
                            ((PdfDictionary) item.merged.get(k)).put(PdfName.MK, mk);
                            ((PdfDictionary) item.widgets.get(k)).put(PdfName.MK, mk);
                            markUsed((PdfDictionary) item.widgets.get(k));
                        }
                        if (value == null) mk.remove(dname); else mk.put(dname, PdfFormField.getMKColor((Color) value));
                    }
                }
            } else return false;
            return true;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
