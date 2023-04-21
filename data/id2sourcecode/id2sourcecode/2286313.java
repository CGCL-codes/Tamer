    public void dumpPart(Part p) throws MessagingException, IOException {
        String ct = p.getContentType();
        String strFile = new String();
        if ((p.getFileName() != null)) {
            if (!p.getFileName().equals("")) {
                this.setFileName(MimeUtility.decodeText(p.getFileName()));
            }
        }
        Object content = p.getContent();
        String strValue = new String("");
        if (p.isMimeType("text/plain")) {
            System.out.println("���� Asccii���ʽ");
            System.out.println("---------------------------");
            String disp = toString(p.getDisposition());
            if (!disp.equals(Part.ATTACHMENT)) {
                strValue = MimeUtility.decodeText((String) p.getContent());
                sbContent.append(strValue);
            }
            strValue = null;
            disp = null;
        } else if (p.isMimeType("text/html")) {
            System.out.println("����  html ��ʽ");
            System.out.println("---------------------------");
            String disp = toString(p.getDisposition());
            if (!disp.equals(Part.ATTACHMENT)) {
                strValue = MimeUtility.decodeText((String) p.getContent());
                sbContent.append(strValue);
            }
            strValue = null;
            disp = null;
        } else if (p.isMimeType("multipart/*")) {
            System.out.println("---���ڶ������ʼ�-----------");
            System.out.println("---------------------------");
            Multipart mp = (Multipart) p.getContent();
            level++;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) dumpPart(mp.getBodyPart(i));
            level--;
        } else if (p.isMimeType("message/rfc822")) {
            level++;
            System.out.println("This is a Nested Message");
            System.out.println("----------- ����InternetЭ�� ----------------");
            dumpPart((Part) p.getContent());
            level--;
        } else {
            Object o = p.getContent();
            if (o instanceof String) {
                System.out.println("���ִ���");
                System.out.println("------------ String ---------------");
                sbContent.append((String) o);
            } else {
                System.out.println("���ڲ�֪��������");
                System.out.println("---------- unkown type-----------------");
                pr(o.toString());
            }
        }
        if (level != 0 && !p.isMimeType("multipart/*")) {
            String disp = p.getDisposition();
            System.out.println("------> " + level + "  disp---> " + disp);
            if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equals(Part.INLINE))) {
                try {
                    System.out.println("�Ǹ���---------------------------");
                    strBuf.setLength(0);
                    strBuf.append(serverPath).append(uploadFile.getDir()).append("\\mail_web\\");
                    if (this.getFileName() == null) this.setFileName("����");
                    String fileDirName = strBuf.toString();
                    System.out.println("--- file dir is " + fileDirName);
                    uploadFile.createDirtory(fileDirName);
                    String realFileName = IdMng.getModuleID(String.valueOf(userId)) + this.getExtName(this.getFileName());
                    strBuf.append(realFileName);
                    String filePathName = strBuf.toString();
                    File file = new File(filePathName);
                    if (file.exists()) throw new IOException("�ļ��Ѵ���");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    InputStream is = p.getInputStream();
                    int c;
                    while ((c = is.read()) != -1) os.write(c);
                    os.close();
                    String insertFilePath = uploadFile.getDir() + "mail_web/" + realFileName;
                    this.addAttchList(this.getFileName(), insertFilePath);
                } catch (IOException ex) {
                    System.out.println("------�洢����ʧ��--------: " + ex);
                }
                System.out.println("---------------------------");
            }
        }
    }
