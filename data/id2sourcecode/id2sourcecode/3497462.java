    public static void main(String[] args) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html>");
        stringBuffer.append("<body>");
        stringBuffer.append("这是一个表格66:");
        stringBuffer.append("<table border='1'  bgColor='#ffffff'>");
        stringBuffer.append("<tr style='background-color:#eeeeee'>");
        stringBuffer.append("<td bgcolor='#eeeeee' colSpan='2'>");
        stringBuffer.append("<table>");
        stringBuffer.append("<tr><td>表格嵌套2</td><td>表格嵌套2</td></tr>");
        stringBuffer.append("<tr><td>嵌套单元3</td><td>嵌套单元3</td></tr>");
        stringBuffer.append("</table>");
        stringBuffer.append("</td>");
        stringBuffer.append("<td>单元1</td>");
        stringBuffer.append("</tr>");
        stringBuffer.append("<tr>");
        stringBuffer.append("<td colSpan='2'>单元2</td><td>单元2</td>");
        stringBuffer.append("</tr>");
        stringBuffer.append("<tr>");
        stringBuffer.append("<td colSpan='2'>单元3</td><td>单元3</td>");
        stringBuffer.append("</tr>");
        stringBuffer.append("<tr>");
        stringBuffer.append("<td colSpan='2'><p>单元4</p></td><td>单元4</td>");
        stringBuffer.append("</tr>");
        stringBuffer.append("<tr>");
        stringBuffer.append("<td colSpan='2'>单元5</td><td>单元5</td>");
        stringBuffer.append("</tr>");
        stringBuffer.append("</table><br/>");
        stringBuffer.append("</body>");
        stringBuffer.append("</html>");
        System.out.println(stringBuffer.toString());
        PdfReader reader = new PdfReader("d:/test.pdf");
        PdfReader reader1 = new PdfReader("d:/test1.pdf");
        PdfReader reader2 = new PdfReader("d:/test2.pdf");
        Document document = new Document(reader.getPageSizeWithRotation(1));
        PdfCopy copy = new PdfCopy(document, new FileOutputStream("d:/marge.pdf"));
        document.open();
        int pageSize = reader.getNumberOfPages();
        for (int i = 1; i <= pageSize; i++) {
            PdfImportedPage page = copy.getImportedPage(reader, i);
            copy.addPage(page);
        }
        pageSize = reader1.getNumberOfPages();
        for (int i = 1; i <= pageSize; i++) {
            PdfImportedPage page = copy.getImportedPage(reader, i);
            copy.addPage(page);
        }
        pageSize = reader2.getNumberOfPages();
        for (int i = 1; i <= pageSize; i++) {
            PdfImportedPage page = copy.getImportedPage(reader, i);
            copy.addPage(page);
        }
        document.close();
    }
