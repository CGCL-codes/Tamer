    @SuppressWarnings("unchecked")
    public static void process(File pdf) {
        String pdfFile = pdf.getAbsolutePath();
        String pdfFileName = pdf.getName();
        String strippedPdfName = pdfFileName.substring(pdfFileName.indexOf("_") + 1, pdfFileName.indexOf("."));
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("src" + File.separator + "net" + File.separator + "sf" + File.separator + "gateway" + File.separator + "mef" + File.separator + "pdf" + File.separator + pdfFileName.substring(0, pdfFileName.indexOf("_")).toLowerCase() + File.separator + strippedPdfName + ".java"));
            PdfReader reader = new PdfReader(pdfFile);
            AcroFields fields = reader.getAcroFields();
            HashMap<String, Item> fieldsMap = fields.getFields();
            Set<String> fieldNames = fieldsMap.keySet();
            String taxyear = pdfFileName.substring(0, pdfFileName.indexOf("_"));
            out.println("package net.sf.gateway.mef.pdf." + taxyear.toLowerCase() + ";\n");
            out.println("import com.itextpdf.text.Element;");
            out.println("import com.itextpdf.text.pdf.PdfContentByte;");
            out.println("import com.itextpdf.text.DocumentException;");
            out.println("import com.itextpdf.text.pdf.AcroFields;");
            out.println("import com.itextpdf.text.pdf.BaseFont;");
            out.println("import com.itextpdf.text.Font;");
            out.println("import com.itextpdf.text.FontFactory;");
            out.println("import com.itextpdf.text.pdf.PdfReader;");
            out.println("import com.itextpdf.text.pdf.PdfStamper;");
            out.println("import com.itextpdf.text.BaseColor;");
            out.println("import java.io.FileOutputStream;");
            out.println("import java.io.IOException;");
            out.println("import java.util.Date;");
            out.println("");
            out.println("/**");
            out.println(" * Generated class for filling '" + strippedPdfName + "'");
            out.println(" */");
            out.println("public class " + strippedPdfName + " {\n");
            out.println("\t/**");
            out.println("\t * Form Filler -- Call this method once you have set all the form fields.");
            out.println("\t * @param src The source -- a fillable PDF.");
            out.println("\t * @param dest The destination -- a filled PDF.");
            out.println("\t * @throws IOException thrown when one of the files cannot be opened.");
            out.println("\t * @throws DocumentException thrown when one of the fields cannot be filled.");
            out.println("\t */");
            out.println("\tpublic void fill(String src, String dest, String user) throws IOException, DocumentException {");
            out.println("\t\tPdfReader reader = new PdfReader(src);");
            out.println("\t\tFileOutputStream writer = new FileOutputStream(dest);");
            out.println("\t\tPdfStamper stamper = new PdfStamper(reader, writer);");
            out.println("\t\tstamper.setEncryption(true, \"\", \"Gu7ruc*YAWaStEbr\", 0);");
            out.println("\t\tAcroFields fields = stamper.getAcroFields();");
            out.println("\t\tFont font = FontFactory.getFont(FontFactory.COURIER_BOLD);");
            out.println("\t\tfont.setSize((float) 20.2);");
            out.println("\t\tBaseFont baseFont = font.getBaseFont();");
            for (String fieldName : fieldNames) {
                out.println("\t\tfields.setFieldProperty(\"" + fieldName + "\", \"textsize\", new Float(20.2), null);");
                out.println("\t\tfields.setFieldProperty(\"" + fieldName + "\", \"textfont\", baseFont, null);");
                out.println("\t\tfields.setField(\"" + fieldName + "\", this.get" + toJavaFieldName(fieldName) + "());");
            }
            out.println("\t\tstamper.setFormFlattening(true);");
            out.println("\t\tstamper.setFullCompression();");
            out.println("\t\tfor (int i = 0; i < reader.getNumberOfPages()+1; i++) {");
            out.println("\t\t\tPdfContentByte overContent = stamper.getOverContent(i);");
            out.println("\t\t\tif (overContent != null) {");
            out.println("\t\t\t\toverContent.beginText();");
            out.println("\t\t\t\tfont = FontFactory.getFont(FontFactory.TIMES_ITALIC);");
            out.println("\t\t\t\tfont.setColor(BaseColor.BLUE);");
            out.println("\t\t\t\tbaseFont = font.getBaseFont();");
            out.println("\t\t\t\toverContent.setColorFill(BaseColor.BLUE);");
            out.println("\t\t\t\toverContent.setFontAndSize(baseFont, 24);");
            out.println("\t\t\t\toverContent.showTextAligned(Element.ALIGN_RIGHT|Element.ALIGN_TOP, \"Electronically filed via Modernized eFile\", 20, 175, 90);");
            out.println("\t\t\t\toverContent.endText();");
            out.println("\t\t\t\toverContent.beginText();");
            out.println("\t\t\t\tfont = FontFactory.getFont(FontFactory.TIMES);");
            out.println("\t\t\t\tfont.setColor(BaseColor.RED);");
            out.println("\t\t\t\tbaseFont = font.getBaseFont();");
            out.println("\t\t\t\toverContent.setColorFill(BaseColor.RED);");
            out.println("\t\t\t\toverContent.setFontAndSize(baseFont, 8);");
            out.println("\t\t\t\toverContent.showTextAligned(Element.ALIGN_CENTER|Element.ALIGN_BOTTOM, \"Retrieved by \" + user + \" on \" + new Date().toString(), 220, 3, 0);");
            out.println("\t\t\t\toverContent.endText();");
            out.println("\t\t\t}");
            out.println("\t\t}");
            out.println("\t\tstamper.close();");
            out.println("\t\treader.close();");
            out.println("\t}\n");
            for (String fieldName : fieldNames) {
                out.println("\t/**");
                out.println("\t * Class member corresponding to the field '" + fieldName + "' in the PDF.");
                out.println("\t */");
                out.println("\tprivate String " + toJavaFieldName(fieldName) + " = \"\";\n");
                out.println("\t/**");
                out.println("\t * Mutator Method for x" + fieldName + "");
                out.println("\t * @param " + fieldName + " the new value for '" + fieldName + "'");
                out.println("\t */");
                out.println("\tpublic void set" + toJavaFieldName(fieldName) + "(String " + toJavaFieldName(fieldName) + ") {\n\t\tthis." + toJavaFieldName(fieldName) + " = " + toJavaFieldName(fieldName) + ";\n\t}\n");
                out.println("\t/**");
                out.println("\t * Accessor Method for x" + fieldName + "");
                out.println("\t * @return the value of '" + fieldName + "'");
                out.println("\t */");
                out.println("\tpublic String get" + toJavaFieldName(fieldName) + "() {\n\t\treturn this." + toJavaFieldName(fieldName) + ";\n\t}\n");
            }
            out.println("}");
            reader.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
