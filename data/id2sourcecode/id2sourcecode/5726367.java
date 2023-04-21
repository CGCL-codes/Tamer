    public void tsetUp() {
        Set<Channel> channels = new HashSet<Channel>();
        ms = new MemberService();
        StudyService studyService = new StudyService();
        int studyId = studyService.setStudy(new Study("Hogeschool Utrecht", "Nieuwe Media"));
        Study study = studyService.getStudy(studyId);
        Set<MyStudy> myStudies = new HashSet<MyStudy>();
        myStudies.add(new MyStudy(Member.DATE_NOT_SET.getTime(), study));
        FunctionService fs = new FunctionService();
        functions.add(fs.getFunction(fs.setFunction(new Function("Function 1"))));
        functions.add(fs.getFunction(fs.setFunction(new Function("Function 2"))));
        ChannelService cs = new ChannelService();
        channels.add(cs.getChannel(cs.setChannel(new Channel("Verjaardagen"))));
        channels.add(cs.getChannel(cs.setChannel(new Channel("Activiteiten"))));
        Member member = new Member("ing.", "Astrid", (++unique) + "Amersfoort", "van", Member.FEMALE, new Date(), "Kerkplein 10", "1234 AB", "Arnhem", "The Netherlands", "024-1234567", "06-123456789", "bla@here.com", functions, channels, myStudies, "Bank city", "A. van Amersfoort", "12.34.56.789", "MyBank inc.");
        member.setDepartment(department);
        logger.info("Password: " + member.getPassword());
        addedMemberIds.add(ms.setMember(member, loggedInId));
    }
