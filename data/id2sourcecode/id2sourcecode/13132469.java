    public void saveFile(javax.servlet.http.HttpServletRequest request) {
        ServletInputStream sis = null;
        BufferedOutputStream buffOS = null;
        try {
            sis = request.getInputStream();
            int readNum = 0;
            byte[] flagChar = new byte[3];
            readNum = sis.read(flagChar, 0, 3);
            if (readNum != -1) System.out.println("flage char : " + (char) flagChar[0] + (char) flagChar[1] + (char) flagChar[2]);
            String fileName;
            String pathName = this.getServletContext().getRealPath("");
            if ((char) flagChar[2] == 'f') pathName = pathName + "\\files\\"; else pathName = pathName + "\\templ\\";
            if ((char) flagChar[1] == 'j') fileName = pathName + "1.jpg"; else fileName = pathName + "1.gif";
            buffOS = new BufferedOutputStream(new FileOutputStream(fileName));
            StringBuffer sb = new StringBuffer();
            int startPos = 0;
            byte[] arrByte = new byte[5000];
            do {
                readNum = sis.read(arrByte, 0, 5000);
                if (readNum != -1) {
                    buffOS.write(arrByte, 0, readNum);
                    buffOS.flush();
                }
                startPos += readNum;
                System.out.println("startPos+readNum " + startPos + "  " + readNum);
            } while (readNum != -1);
            System.out.println("totol file" + startPos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffOS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
