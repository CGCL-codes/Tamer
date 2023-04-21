    public CetCompo(final Composite parent, int style) {
        super(parent, style);
        setLayout(new FormLayout());
        final Group group = new Group(this, SWT.NONE);
        group.setText("标准查询(99宿舍)");
        final FormData fd_group = new FormData();
        fd_group.bottom = new FormAttachment(0, 210);
        fd_group.right = new FormAttachment(50, 0);
        fd_group.left = new FormAttachment(0, 5);
        group.setLayoutData(fd_group);
        group.setLayout(new FormLayout());
        final CLabel label_8 = new CLabel(group, SWT.NONE);
        label_8.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/classf_obj.gif"));
        final FormData fd_label_8 = new FormData();
        fd_label_8.right = new FormAttachment(0, 110);
        fd_label_8.left = new FormAttachment(0, 5);
        fd_label_8.bottom = new FormAttachment(0, 30);
        fd_label_8.top = new FormAttachment(0, 5);
        label_8.setLayoutData(fd_label_8);
        label_8.setText("准考证号码：");
        sIdSText = new StyledText(group, SWT.SINGLE | SWT.BORDER);
        final FormData fd_sIdSText = new FormData();
        fd_sIdSText.left = new FormAttachment(label_8, 110, SWT.LEFT);
        fd_sIdSText.right = new FormAttachment(100, -5);
        fd_sIdSText.bottom = new FormAttachment(label_8, 0, SWT.BOTTOM);
        fd_sIdSText.top = new FormAttachment(label_8, 0, SWT.TOP);
        sIdSText.setLayoutData(fd_sIdSText);
        final CLabel label_9 = new CLabel(group, SWT.NONE);
        label_9.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/javadoc.gif"));
        final FormData fd_label_9 = new FormData();
        fd_label_9.left = new FormAttachment(label_8, 0, SWT.LEFT);
        fd_label_9.top = new FormAttachment(label_8, 5, SWT.BOTTOM);
        fd_label_9.right = new FormAttachment(0, 113);
        fd_label_9.bottom = new FormAttachment(0, 60);
        label_9.setLayoutData(fd_label_9);
        label_9.setText("考试类型：");
        final Button cet4But = new Button(group, SWT.RADIO);
        cet4But.setSelection(true);
        final FormData fd_cet4But = new FormData();
        fd_cet4But.left = new FormAttachment(label_9, 5, SWT.RIGHT);
        fd_cet4But.right = new FormAttachment(0, 195);
        fd_cet4But.bottom = new FormAttachment(label_9, 0, SWT.BOTTOM);
        fd_cet4But.top = new FormAttachment(label_8, 5, SWT.BOTTOM);
        cet4But.setLayoutData(fd_cet4But);
        cet4But.setText("英语四级");
        final Button cet6But;
        cet6But = new Button(group, SWT.RADIO);
        final FormData fd_cet6But = new FormData();
        fd_cet6But.right = new FormAttachment(0, 275);
        fd_cet6But.left = new FormAttachment(cet4But, 5, SWT.RIGHT);
        fd_cet6But.bottom = new FormAttachment(cet4But, 0, SWT.BOTTOM);
        fd_cet6But.top = new FormAttachment(cet4But, 0, SWT.TOP);
        cet6But.setLayoutData(fd_cet6But);
        cet6But.setText("英语六级");
        final Button speakBut = new Button(group, SWT.RADIO);
        final FormData fd_speakBut = new FormData();
        fd_speakBut.right = new FormAttachment(0, 355);
        fd_speakBut.left = new FormAttachment(cet6But, 5, SWT.RIGHT);
        fd_speakBut.bottom = new FormAttachment(cet6But, 0, SWT.BOTTOM);
        fd_speakBut.top = new FormAttachment(cet6But, 0, SWT.TOP);
        speakBut.setLayoutData(fd_speakBut);
        speakBut.setText("英语口语");
        final Button resetBut = new Button(group, SWT.NONE);
        resetBut.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/refresh.gif"));
        final FormData fd_resetBut = new FormData();
        fd_resetBut.right = new FormAttachment(24, 0);
        fd_resetBut.bottom = new FormAttachment(100, -5);
        fd_resetBut.left = new FormAttachment(label_9, 0, SWT.LEFT);
        fd_resetBut.top = new FormAttachment(label_9, 5, SWT.BOTTOM);
        resetBut.setLayoutData(fd_resetBut);
        resetBut.setText("清空");
        standSoBut = new Button(group, SWT.NONE);
        standSoBut.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/synch_synch.gif"));
        final FormData fd_standSoBut = new FormData();
        fd_standSoBut.bottom = new FormAttachment(resetBut, 0, SWT.BOTTOM);
        fd_standSoBut.left = new FormAttachment(56, 0);
        fd_standSoBut.right = new FormAttachment(sIdSText, 0, SWT.RIGHT);
        fd_standSoBut.top = new FormAttachment(cet6But, 5, SWT.BOTTOM);
        standSoBut.setLayoutData(fd_standSoBut);
        standSoBut.setText("查询");
        Group group_1;
        group_1 = new Group(this, SWT.NONE);
        group_1.setText("查询结果");
        final FormData fd_group_1 = new FormData();
        fd_group_1.top = new FormAttachment(group, 0, SWT.TOP);
        fd_group_1.right = new FormAttachment(100, -5);
        fd_group_1.left = new FormAttachment(group, 5, SWT.RIGHT);
        group_1.setLayoutData(fd_group_1);
        group_1.setLayout(new FormLayout());
        schoolLab = new CLabel(group_1, SWT.NONE);
        schoolLab.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/info.gif"));
        final FormData fd_schoolLab = new FormData();
        fd_schoolLab.bottom = new FormAttachment(0, 30);
        fd_schoolLab.right = new FormAttachment(100, -5);
        fd_schoolLab.top = new FormAttachment(0, 5);
        fd_schoolLab.left = new FormAttachment(0, 5);
        schoolLab.setLayoutData(fd_schoolLab);
        schoolLab.setText("学校：");
        nameLab = new CLabel(group_1, SWT.NONE);
        nameLab.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/info.gif"));
        final FormData fd_nameLab = new FormData();
        fd_nameLab.bottom = new FormAttachment(0, 60);
        fd_nameLab.right = new FormAttachment(100, -5);
        fd_nameLab.top = new FormAttachment(schoolLab, 5, SWT.BOTTOM);
        fd_nameLab.left = new FormAttachment(schoolLab, 0, SWT.LEFT);
        nameLab.setLayoutData(fd_nameLab);
        nameLab.setText("姓名：");
        totalLab = new CLabel(group_1, SWT.NONE);
        totalLab.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/info.gif"));
        final FormData fd_totalLab = new FormData();
        fd_totalLab.bottom = new FormAttachment(0, 90);
        fd_totalLab.right = new FormAttachment(100, -5);
        fd_totalLab.top = new FormAttachment(nameLab, 5, SWT.BOTTOM);
        fd_totalLab.left = new FormAttachment(nameLab, 0, SWT.LEFT);
        totalLab.setLayoutData(fd_totalLab);
        totalLab.setText("总分：");
        lisLab = new CLabel(group_1, SWT.NONE);
        lisLab.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/css_editor.gif"));
        final FormData fd_lisLab = new FormData();
        fd_lisLab.left = new FormAttachment(0, 30);
        fd_lisLab.bottom = new FormAttachment(0, 120);
        fd_lisLab.top = new FormAttachment(totalLab, 5, SWT.BOTTOM);
        lisLab.setLayoutData(fd_lisLab);
        lisLab.setText("听力：");
        readLab = new CLabel(group_1, SWT.NONE);
        readLab.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/cpyqual_menu.gif"));
        final FormData fd_readLab = new FormData();
        fd_readLab.bottom = new FormAttachment(0, 150);
        fd_readLab.top = new FormAttachment(lisLab, 5, SWT.BOTTOM);
        fd_readLab.left = new FormAttachment(lisLab, 0, SWT.LEFT);
        readLab.setLayoutData(fd_readLab);
        readLab.setText("阅读：");
        mutiLab = new CLabel(group_1, SWT.NONE);
        fd_lisLab.right = new FormAttachment(mutiLab, -5, SWT.LEFT);
        mutiLab.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/generate.gif"));
        final FormData fd_mutiLab = new FormData();
        fd_mutiLab.left = new FormAttachment(schoolLab, 155, SWT.LEFT);
        fd_mutiLab.right = new FormAttachment(totalLab, 0, SWT.RIGHT);
        fd_mutiLab.top = new FormAttachment(lisLab, -25, SWT.BOTTOM);
        fd_mutiLab.bottom = new FormAttachment(lisLab, 0, SWT.BOTTOM);
        mutiLab.setLayoutData(fd_mutiLab);
        mutiLab.setText("综合：");
        writeLab = new CLabel(group_1, SWT.NONE);
        fd_readLab.right = new FormAttachment(writeLab, -5, SWT.LEFT);
        writeLab.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/editor_pane.gif"));
        final FormData fd_writeLab = new FormData();
        fd_writeLab.bottom = new FormAttachment(readLab, 0, SWT.BOTTOM);
        fd_writeLab.left = new FormAttachment(mutiLab, 0, SWT.LEFT);
        writeLab.setLayoutData(fd_writeLab);
        writeLab.setText("写作：");
        CLabel label_15;
        label_15 = new CLabel(group_1, SWT.NONE);
        fd_writeLab.right = new FormAttachment(label_15, 0, SWT.RIGHT);
        label_15.setText("如果对结果的正确性有所怀疑，可以登陆考试官网查询。");
        label_15.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/warning_obj.gif"));
        final FormData fd_label_15 = new FormData();
        fd_label_15.bottom = new FormAttachment(0, 185);
        fd_label_15.top = new FormAttachment(0, 160);
        fd_label_15.right = new FormAttachment(100, 1);
        fd_label_15.left = new FormAttachment(0, 8);
        label_15.setLayoutData(fd_label_15);
        Label label;
        label = new Label(group_1, SWT.WRAP);
        final FormData fd_label = new FormData();
        fd_label.bottom = new FormAttachment(100, -5);
        fd_label.top = new FormAttachment(label_15, 5, SWT.BOTTOM);
        fd_label.right = new FormAttachment(100, -2);
        fd_label.left = new FormAttachment(0, 14);
        label.setLayoutData(fd_label);
        label.setText("声明：\n    本功能的查询结果均来自大学四六级考试官方网站。\n\n    本软件作者不对所获取的结果数据的正确性作任何保证，亦不对其产生的相关信息负责，请勿用于商业用途。\n\n欲知更多详细信息可以登陆：\n    99宿舍：http://cet.99sushe.com/ \n    广东省考试中心：http://www.eesc.com.cn/");
        Group group_2;
        group_2 = new Group(this, SWT.NONE);
        fd_group_1.bottom = new FormAttachment(group_2, 0, SWT.BOTTOM);
        group_2.setText("模糊查询");
        final FormData fd_group_2 = new FormData();
        fd_group_2.bottom = new FormAttachment(0, 420);
        fd_group_2.top = new FormAttachment(group, 5, SWT.BOTTOM);
        fd_group_2.right = new FormAttachment(50, 0);
        fd_group_2.left = new FormAttachment(group, 0, SWT.LEFT);
        group_2.setLayoutData(fd_group_2);
        group_2.setLayout(new FormLayout());
        final CLabel label_7 = new CLabel(group_2, SWT.NONE);
        label_7.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/classf_obj.gif"));
        final FormData fd_label_7 = new FormData();
        fd_label_7.bottom = new FormAttachment(0, 30);
        fd_label_7.top = new FormAttachment(0, 5);
        fd_label_7.right = new FormAttachment(100, -5);
        fd_label_7.left = new FormAttachment(0, 5);
        label_7.setLayoutData(fd_label_7);
        label_7.setText("准考证号码(同校其它同学的准考证号，注意号码组成)：");
        idSText = new StyledText(group_2, SWT.SINGLE | SWT.BORDER);
        idSText.setToolTipText("例如：我的同学的准考证号为：[4409500811 021 21]，代表在第21考室，座位号为21。\n如果我在第1考室，座位号为1，则输入[4409500800101]。");
        final FormData fd_idSText = new FormData();
        fd_idSText.bottom = new FormAttachment(0, 60);
        fd_idSText.top = new FormAttachment(label_7, 5, SWT.BOTTOM);
        fd_idSText.right = new FormAttachment(100, -5);
        fd_idSText.left = new FormAttachment(0, 30);
        idSText.setLayoutData(fd_idSText);
        final CLabel label_10 = new CLabel(group_2, SWT.NONE);
        label_10.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/generate.gif"));
        final FormData fd_label_10 = new FormData();
        fd_label_10.bottom = new FormAttachment(0, 120);
        fd_label_10.right = new FormAttachment(0, 110);
        fd_label_10.left = new FormAttachment(label_7, 0, SWT.LEFT);
        label_10.setLayoutData(fd_label_10);
        label_10.setText("你的姓名：");
        nameSText = new StyledText(group_2, SWT.SINGLE | SWT.BORDER);
        nameSText.setToolTipText("全名");
        final FormData fd_nameSText = new FormData();
        fd_nameSText.right = new FormAttachment(52, 0);
        fd_nameSText.bottom = new FormAttachment(label_10, 0, SWT.BOTTOM);
        fd_nameSText.left = new FormAttachment(label_10, 5, SWT.RIGHT);
        fd_nameSText.top = new FormAttachment(label_10, 0, SWT.TOP);
        nameSText.setLayoutData(fd_nameSText);
        classSText = new StyledText(group_2, SWT.BORDER);
        final FormData fd_classSText = new FormData();
        fd_classSText.top = new FormAttachment(nameSText, 0, SWT.TOP);
        classSText.setLayoutData(fd_classSText);
        CLabel label_1;
        label_1 = new CLabel(group_2, SWT.NONE);
        fd_classSText.left = new FormAttachment(label_1, 5, SWT.RIGHT);
        fd_classSText.bottom = new FormAttachment(label_1, 0, SWT.BOTTOM);
        final FormData fd_label_1 = new FormData();
        fd_label_1.right = new FormAttachment(71, 0);
        fd_label_1.left = new FormAttachment(nameSText, 5, SWT.DEFAULT);
        fd_label_1.bottom = new FormAttachment(nameSText, 0, SWT.BOTTOM);
        fd_label_1.top = new FormAttachment(nameSText, 0, SWT.TOP);
        label_1.setLayoutData(fd_label_1);
        label_1.setText("考室号：");
        CLabel label_11;
        label_11 = new CLabel(group_2, SWT.NONE);
        label_11.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/javadoc.gif"));
        final FormData fd_label_11 = new FormData();
        fd_label_11.bottom = new FormAttachment(0, 150);
        fd_label_11.top = new FormAttachment(label_10, 5, SWT.BOTTOM);
        fd_label_11.left = new FormAttachment(0, 5);
        fd_label_11.right = new FormAttachment(label_10, 0, SWT.RIGHT);
        label_11.setLayoutData(fd_label_11);
        label_11.setText("考试类型：");
        final Button cet4But_1 = new Button(group_2, SWT.RADIO);
        cet4But_1.setSelection(true);
        final FormData fd_cet4But_1 = new FormData();
        fd_cet4But_1.right = new FormAttachment(0, 195);
        fd_cet4But_1.left = new FormAttachment(label_11, 5, SWT.RIGHT);
        fd_cet4But_1.top = new FormAttachment(label_11, 0, SWT.TOP);
        fd_cet4But_1.bottom = new FormAttachment(label_11, 0, SWT.BOTTOM);
        cet4But_1.setLayoutData(fd_cet4But_1);
        cet4But_1.setText("英语四级");
        final Button cet6But_1;
        cet6But_1 = new Button(group_2, SWT.RADIO);
        final FormData fd_cet6But_1 = new FormData();
        fd_cet6But_1.left = new FormAttachment(cet4But_1, 5, SWT.RIGHT);
        fd_cet6But_1.right = new FormAttachment(0, 265);
        fd_cet6But_1.bottom = new FormAttachment(cet4But_1, 0, SWT.BOTTOM);
        fd_cet6But_1.top = new FormAttachment(cet4But_1, 0, SWT.TOP);
        cet6But_1.setLayoutData(fd_cet6But_1);
        cet6But_1.setText("英语六级");
        final Button speakBut_1;
        speakBut_1 = new Button(group_2, SWT.RADIO);
        final FormData fd_speakBut_1 = new FormData();
        fd_speakBut_1.left = new FormAttachment(label_1, 0, SWT.RIGHT);
        fd_speakBut_1.bottom = new FormAttachment(cet6But_1, 0, SWT.BOTTOM);
        fd_speakBut_1.top = new FormAttachment(cet6But_1, 0, SWT.TOP);
        speakBut_1.setLayoutData(fd_speakBut_1);
        speakBut_1.setText("英语口语");
        Button resetBut_1;
        resetBut_1 = new Button(group_2, SWT.NONE);
        resetBut_1.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/refresh.gif"));
        final FormData fd_resetBut_1 = new FormData();
        fd_resetBut_1.top = new FormAttachment(label_11, 5, SWT.BOTTOM);
        fd_resetBut_1.bottom = new FormAttachment(100, -5);
        fd_resetBut_1.right = new FormAttachment(24, 0);
        fd_resetBut_1.left = new FormAttachment(label_11, 0, SWT.LEFT);
        resetBut_1.setLayoutData(fd_resetBut_1);
        resetBut_1.setText("清空");
        cetSoBut = new Button(group_2, SWT.NONE);
        fd_speakBut_1.right = new FormAttachment(cetSoBut, 0, SWT.RIGHT);
        cetSoBut.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/synch_synch.gif"));
        final FormData fd_cetSoBut = new FormData();
        fd_cetSoBut.top = new FormAttachment(speakBut_1, 5, SWT.BOTTOM);
        fd_cetSoBut.bottom = new FormAttachment(100, -5);
        fd_cetSoBut.left = new FormAttachment(56, 0);
        fd_cetSoBut.right = new FormAttachment(100, -5);
        cetSoBut.setLayoutData(fd_cetSoBut);
        cetSoBut.setText("开始查询");
        cetSoStopBut = new Button(group_2, SWT.NONE);
        cetSoStopBut.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/delete_edit.gif"));
        final FormData fd_cetSoStopBut = new FormData();
        fd_cetSoStopBut.top = new FormAttachment(cet4But_1, 5, SWT.BOTTOM);
        fd_cetSoStopBut.left = new FormAttachment(31, 0);
        fd_cetSoStopBut.right = new FormAttachment(cetSoBut, -8, SWT.LEFT);
        fd_cetSoStopBut.bottom = new FormAttachment(cetSoBut, 0, SWT.BOTTOM);
        cetSoStopBut.setLayoutData(fd_cetSoStopBut);
        cetSoStopBut.setText("停止");
        cetSoStopBut.setEnabled(false);
        CLabel label_14;
        label_14 = new CLabel(group_2, SWT.NONE);
        fd_label_10.top = new FormAttachment(label_14, 5, SWT.BOTTOM);
        fd_classSText.right = new FormAttachment(label_14, 0, SWT.RIGHT);
        label_14.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/ihigh_obj.gif"));
        final FormData fd_label_14 = new FormData();
        fd_label_14.bottom = new FormAttachment(0, 90);
        fd_label_14.top = new FormAttachment(idSText, 5, SWT.BOTTOM);
        fd_label_14.right = new FormAttachment(idSText, 0, SWT.RIGHT);
        fd_label_14.left = new FormAttachment(label_7, 0, SWT.LEFT);
        label_14.setLayoutData(fd_label_14);
        label_14.setText("号码组成：学校代码(4409500821)+考室号(xxx)+座位号(xx)");
        Group group_3;
        group_3 = new Group(this, SWT.NONE);
        fd_group.top = new FormAttachment(group_3, 5, SWT.BOTTOM);
        group_3.setText("广东英语四六级查询(www.eesc.com.cn)");
        final FormData fd_group_3 = new FormData();
        fd_group_3.bottom = new FormAttachment(0, 90);
        fd_group_3.right = new FormAttachment(group_1, 0, SWT.RIGHT);
        fd_group_3.top = new FormAttachment(0, 5);
        fd_group_3.left = new FormAttachment(group, 0, SWT.LEFT);
        group_3.setLayoutData(fd_group_3);
        group_3.setLayout(new FormLayout());
        final CLabel label_8_1 = new CLabel(group_3, SWT.NONE);
        label_8_1.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/classf_obj.gif"));
        final FormData fd_label_8_1 = new FormData();
        fd_label_8_1.left = new FormAttachment(0, 5);
        fd_label_8_1.right = new FormAttachment(0, 90);
        label_8_1.setLayoutData(fd_label_8_1);
        label_8_1.setText("准考证：");
        gdIdSText = new StyledText(group_3, SWT.SINGLE | SWT.BORDER);
        fd_label_8_1.bottom = new FormAttachment(gdIdSText, 0, SWT.BOTTOM);
        fd_label_8_1.top = new FormAttachment(gdIdSText, 0, SWT.TOP);
        final FormData fd_gdIdSText = new FormData();
        fd_gdIdSText.bottom = new FormAttachment(0, 30);
        fd_gdIdSText.left = new FormAttachment(label_8_1, 5, SWT.RIGHT);
        fd_gdIdSText.top = new FormAttachment(0, 5);
        fd_gdIdSText.right = new FormAttachment(34, 0);
        gdIdSText.setLayoutData(fd_gdIdSText);
        gdSoBut = new Button(group_3, SWT.NONE);
        gdSoBut.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/synch_synch.gif"));
        FormData fd_gdSoBut;
        fd_gdSoBut = new FormData();
        fd_gdSoBut.left = new FormAttachment(100, -175);
        fd_gdSoBut.bottom = new FormAttachment(100, -5);
        gdSoBut.setLayoutData(fd_gdSoBut);
        gdSoBut.setText("查询");
        CLabel label_8_1_1;
        label_8_1_1 = new CLabel(group_3, SWT.NONE);
        label_8_1_1.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/build_exec.gif"));
        final FormData fd_label_8_1_1 = new FormData();
        fd_label_8_1_1.right = new FormAttachment(45, 0);
        fd_label_8_1_1.left = new FormAttachment(gdIdSText, 6, SWT.DEFAULT);
        fd_label_8_1_1.bottom = new FormAttachment(gdIdSText, 25, SWT.TOP);
        fd_label_8_1_1.top = new FormAttachment(gdIdSText, 0, SWT.TOP);
        label_8_1_1.setLayoutData(fd_label_8_1_1);
        label_8_1_1.setText("报名号：");
        gdSignupSText = new StyledText(group_3, SWT.SINGLE | SWT.BORDER);
        final FormData fd_gdSignupSText = new FormData();
        fd_gdSignupSText.bottom = new FormAttachment(label_8_1_1, 0, SWT.BOTTOM);
        fd_gdSignupSText.top = new FormAttachment(0, 5);
        fd_gdSignupSText.right = new FormAttachment(62, 0);
        fd_gdSignupSText.left = new FormAttachment(label_8_1_1, 5, SWT.DEFAULT);
        gdSignupSText.setLayoutData(fd_gdSignupSText);
        gdIdentitySText = new StyledText(group_3, SWT.SINGLE | SWT.BORDER);
        fd_gdSoBut.right = new FormAttachment(gdIdentitySText, 0, SWT.RIGHT);
        fd_gdSoBut.top = new FormAttachment(gdIdentitySText, 5, SWT.BOTTOM);
        final FormData fd_gdIdentitySText = new FormData();
        fd_gdIdentitySText.right = new FormAttachment(100, -5);
        gdIdentitySText.setLayoutData(fd_gdIdentitySText);
        CLabel label_8_1_2;
        label_8_1_2 = new CLabel(group_3, SWT.NONE);
        fd_gdIdentitySText.left = new FormAttachment(label_8_1_2, 9, SWT.DEFAULT);
        label_8_1_2.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/build_exec.gif"));
        fd_gdIdentitySText.bottom = new FormAttachment(label_8_1_2, 25, SWT.TOP);
        fd_gdIdentitySText.top = new FormAttachment(label_8_1_2, 0, SWT.TOP);
        final FormData fd_label_8_1_2 = new FormData();
        fd_label_8_1_2.left = new FormAttachment(gdSignupSText, 5, SWT.DEFAULT);
        fd_label_8_1_2.right = new FormAttachment(73, 0);
        fd_label_8_1_2.bottom = new FormAttachment(gdSignupSText, 25, SWT.TOP);
        fd_label_8_1_2.top = new FormAttachment(gdSignupSText, 0, SWT.TOP);
        label_8_1_2.setLayoutData(fd_label_8_1_2);
        label_8_1_2.setText("身份证：");
        CLabel label_2;
        label_2 = new CLabel(group_3, SWT.NONE);
        label_2.setImage(SWTResourceManager.getImage(CetCompo.class, "/cn/imgdpu/ico/ihigh_obj.gif"));
        final FormData fd_label_2 = new FormData();
        fd_label_2.bottom = new FormAttachment(gdSoBut, 0, SWT.BOTTOM);
        fd_label_2.top = new FormAttachment(label_8_1, 5, SWT.BOTTOM);
        fd_label_2.left = new FormAttachment(label_8_1, 0, SWT.LEFT);
        label_2.setLayoutData(fd_label_2);
        label_2.setText("以上准考证号、报名号、身份证号任填一个即可查询。(仅针对广东考生！)");
        resetBut.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                sIdSText.setText("");
            }
        });
        resetBut_1.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                idSText.setText("");
                nameSText.setText("");
                classSText.setText("");
            }
        });
        gdSoBut.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                String gdIdStr = gdIdSText.getText();
                String gdSignupStr = gdSignupSText.getText();
                String gdIdentityStr = gdIdentitySText.getText();
                if (gdIdStr.length() == 15) {
                    gdSoBut.setEnabled(false);
                    cn.imgdpu.GSAGUI.setStatusAsyn("正在用力的查找中...");
                    new GetGdCetInfo(gdIdStr).start();
                } else if (gdSignupStr.length() == 12 || gdIdentityStr.length() == 15 || gdIdentityStr.length() == 18) {
                    gdSoBut.setEnabled(false);
                    cn.imgdpu.GSAGUI.setStatusAsyn("正在用力的查找中...");
                    new GetGdCetNum(gdSignupStr, gdIdStr, gdIdentityStr).start();
                } else {
                    cn.imgdpu.GSAGUI.setStatusAsyn("你好像什么都没填!准考证号、报名号、身份证号任填一个即可！~(>_<)~");
                }
            }
        });
        standSoBut.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                String cet = null;
                if (cet4But.getSelection()) {
                    cet = "4";
                } else if (cet6But.getSelection()) {
                    cet = "6";
                } else if (speakBut.getSelection()) {
                    cet = "10";
                }
                if (((cet == "4" || cet == "6") && sIdSText.getText().length() == 15) || (cet == "10" && sIdSText.getText().length() == 13)) {
                    cn.imgdpu.GSAGUI.setStatus("正在用力的查找中...");
                    new GetCetInfo(cet, sIdSText.getText()).start();
                    standSoBut.setEnabled(false);
                } else {
                    cn.imgdpu.GSAGUI.setStatus("准考证号码输入不正确！~(>_<)~");
                }
            }
        });
        cetSoBut.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                String cet = null;
                if (cet4But_1.getSelection()) {
                    cet = "4";
                } else if (cet6But_1.getSelection()) {
                    cet = "6";
                } else if (speakBut_1.getSelection()) {
                    cet = "10";
                }
                if (idSText.getText().trim().equals("") || classSText.getText().trim().equals("") || nameSText.getText().trim().equals("")) {
                    cn.imgdpu.GSAGUI.setStatus("同校同学准考证号、你的姓名、考室号都必需填写，不然就不好找了！~(>_<)~");
                } else if (classSText.getText().trim().length() != 3) {
                    cn.imgdpu.GSAGUI.setStatus("考室号为三位数字！例如：017");
                } else {
                    if (((cet == "4" || cet == "6") && idSText.getText().trim().length() == 15) || (cet == "10" && idSText.getText().trim().length() == 13)) {
                        getCetInfo = new GetCetInfo(cet, idSText.getText().trim(), classSText.getText().trim(), nameSText.getText().trim());
                        cn.imgdpu.GSAGUI.setStatus("开始模糊查询！");
                        cetSoBut.setEnabled(false);
                        cetSoStopBut.setEnabled(true);
                        getCetInfo.getCetInfoNear(getCetInfo);
                    } else {
                        cn.imgdpu.GSAGUI.setStatus("准考证号码输入貌似不正确！~(>_<)~");
                    }
                }
            }
        });
        cetSoStopBut.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                getCetInfo.stopGetCetInfoNear();
                cetSoStopBut.setEnabled(false);
                cn.imgdpu.GSAGUI.setStatus("被用户中止！正在停止线程，请稍等。");
            }
        });
    }
