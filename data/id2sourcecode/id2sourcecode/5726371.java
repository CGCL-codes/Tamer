    public void ttestSetMember() {
        Set<Channel> channels = new HashSet<Channel>();
        Set<Function> functions2 = new HashSet<Function>();
        FunctionService fs = new FunctionService();
        functions.add(fs.getFunctionByName("Test function 1"));
        ChannelService cs = new ChannelService();
        int channelId = cs.setChannel(new Channel("Verjaardagen"));
        channels.add(cs.getChannel(channelId));
        StudyService studyService = new StudyService();
        int studyId = studyService.setStudy(new Study("Radboud Universiteit Nijmegen", "Informatica"));
        Study study = studyService.getStudy(studyId);
        Set<MyStudy> myStudies = new HashSet<MyStudy>();
        myStudies.add(new MyStudy(Member.DATE_NOT_SET.getTime(), study));
        Member member = new Member("", "Piet", "Puk", "", Member.MALE, new Date(), "Dorpsplein 201", "4321 BA", "Antwerpen", "Belgium", "+32 (0)9-1234567", "0486-123456789", "abcdefg@there.be", functions2, channels, myStudies, "Bank city", "A. van Amersfoort", "12.34.56.789", "MyBank inc.");
        member.setDepartment(department);
        int id = ms.setMember(member, loggedInId);
        addedMemberIds.add(id);
        assertEquals("Piet", ms.getMember(id, MemberPermissions.USER_ID_ADMIN_INTERN).getNameFirst());
    }
