    public static void main(String[] args) {
        if (args.length < 2) {
        } else if (args.length == 2) {
            if (args[1].toLowerCase().equals("write")) {
                System.out.println("Write mode");
                try {
                    long time = System.currentTimeMillis();
                    HSSF hssf = new HSSF(args[0], true);
                    System.out.println("" + (System.currentTimeMillis() - time) + " ms generation time");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("readwrite test");
                try {
                    HSSF hssf = new HSSF(args[0]);
                    HSSFWorkbook wb = hssf.hssfworkbook;
                    FileOutputStream stream = new FileOutputStream(args[1]);
                    wb.write(stream);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((args.length == 3) && args[2].toLowerCase().equals("modify1")) {
            try {
                HSSF hssf = new HSSF(args[0]);
                HSSFWorkbook wb = hssf.hssfworkbook;
                FileOutputStream stream = new FileOutputStream(args[1]);
                HSSFSheet sheet = wb.getSheetAt(0);
                for (int k = 0; k < 25; k++) {
                    HSSFRow row = sheet.getRow(k);
                    sheet.removeRow(row);
                }
                for (int k = 74; k < 100; k++) {
                    HSSFRow row = sheet.getRow(k);
                    sheet.removeRow(row);
                }
                HSSFRow row = sheet.getRow(39);
                HSSFCell cell = row.getCell((short) 3);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue("MODIFIED CELL!!!!!");
                wb.write(stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
