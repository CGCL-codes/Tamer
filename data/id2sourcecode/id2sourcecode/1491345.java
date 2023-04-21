    public AttachmentPanel(MimeBodyPart mm) {
        initComponents();
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        } else {
            jbuOpen.setEnabled(false);
        }
        this.mm = mm;
        try {
            if (mm.getContentType().contains("image")) {
                try {
                    jpContainer.add(new ImagePanel(mm.getContent()), "image");
                    CardLayout cl = (CardLayout) jpContainer.getLayout();
                    cl.last(jpContainer);
                } catch (IOException ex) {
                    Logger.getLogger(AttachmentPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (mm.getContentType().contains("text")) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                System.out.println(mm.getEncoding());
                mm.writeTo(bos);
                jtaInfo.append(MimeUtility.decodeText(bos.toString()));
                jtaInfo.setCaretPosition(0);
            } else {
                jtaInfo.append(mm.getContentType() + "\n");
                jtaInfo.append(mm.getFileName() + "\n");
            }
            fileName = mm.getFileName();
            content = mm.getContentType();
        } catch (Exception ex) {
            Logger.getLogger(AttachmentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
