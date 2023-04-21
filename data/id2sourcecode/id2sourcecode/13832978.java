    public int setMember(Member newMember, int who) {
        logger.debug("setMember Id:" + newMember.getId() + " Who:" + who);
        int id = -1;
        Member updatedMember = mmd.getMember(newMember.getId());
        Member oldMember = mmd.getMember(newMember.getId());
        ObjectLock lock = lockManager.getLock(updatedMember);
        if (who == MemberPermissions.USER_ID_ADMIN_INTERN || lock == null || lock.getWho() == who) {
            MemberPermissions permissions = authenticationManager.getPermissions(who, newMember);
            updatedMember.setLoginFrom(newMember.getLoginFrom());
            updatedMember.setLoginLast(newMember.getLoginLast());
            updatedMember.setLoginTotal(newMember.getLoginTotal());
            if (permissions.isApproved()) updatedMember.setApproved(newMember.isApproved());
            if (permissions.isAlumniFunctions()) updatedMember.setAlumniFunctions(newMember.getAlumniFunctions());
            if (permissions.isAlumniMarried()) updatedMember.setAlumniMarried(newMember.getAlumniMarried());
            if (permissions.isAlumniMemberTill()) updatedMember.setAlumniMemberTill(newMember.getAlumniMemberTill());
            if (permissions.isAlumniWorkCity()) updatedMember.setAlumniWorkCity(newMember.getAlumniWorkCity());
            if (permissions.isAlumniWorkCompany()) updatedMember.setAlumniWorkCompany(newMember.getAlumniWorkCompany());
            if (permissions.isAlumniWorkCountry()) updatedMember.setAlumniWorkCountry(newMember.getAlumniWorkCountry());
            if (permissions.isAlumniWorkFunction()) updatedMember.setAlumniWorkFunction(newMember.getAlumniWorkFunction());
            if (permissions.isBirthday()) updatedMember.setBirthday(newMember.getBirthday());
            if (permissions.isCity()) updatedMember.setCity(newMember.getCity());
            if (permissions.isCountry()) updatedMember.setCountry(newMember.getCountry());
            if (permissions.isDepartment()) updatedMember.setDepartment(newMember.getDepartment());
            if (permissions.isEmail()) updatedMember.setEmail(newMember.getEmail());
            if (permissions.isGender()) updatedMember.setGender(newMember.getGender());
            if (permissions.isLastPayed()) updatedMember.setLastPayed(newMember.getLastPayed());
            if (permissions.isMemberSince()) updatedMember.setMemberSince(newMember.getMemberSince());
            if (permissions.isMemberSince2()) updatedMember.setMemberSince2(newMember.getMemberSince2());
            if (permissions.isNameFirst()) updatedMember.setNameFirst(newMember.getNameFirst());
            if (permissions.isNameInsertion()) updatedMember.setNameInsertion(newMember.getNameInsertion());
            if (permissions.isNameLast()) updatedMember.setNameLast(newMember.getNameLast());
            if (permissions.isNameTitle()) updatedMember.setNameTitle(newMember.getNameTitle());
            if (permissions.isNumber()) updatedMember.setNumber(newMember.getNumber());
            if (permissions.isParentsCity()) updatedMember.setParentsCity(newMember.getParentsCity());
            if (permissions.isParentsCountry()) updatedMember.setParentsCountry(newMember.getParentsCountry());
            if (permissions.isParentsPostcode()) updatedMember.setParentsPostcode(newMember.getParentsPostcode());
            if (permissions.isParentsStreet()) updatedMember.setParentsStreet(newMember.getParentsStreet());
            if (permissions.isParentsTelephone()) updatedMember.setParentsTelephone(newMember.getParentsTelephone());
            if (permissions.isPostcode()) updatedMember.setPostcode(newMember.getPostcode());
            if (permissions.isRemarks()) updatedMember.setRemarks(newMember.getRemarks());
            if (permissions.isStreet()) updatedMember.setStreet(newMember.getStreet());
            if (permissions.isTelephone1()) updatedMember.setTelephone1(newMember.getTelephone1());
            if (permissions.isTelephone2()) updatedMember.setTelephone2(newMember.getTelephone2());
            if (permissions.isWebsite()) {
                updatedMember.setWebsite(newMember.getWebsite());
            }
            if (permissions.isBranch()) {
                updatedMember.setBranch(newMember.getBranch());
            }
            if (permissions.isRecommendation()) {
                updatedMember.setRecommendation1(newMember.getRecommendation1());
                updatedMember.setRecommendation2(newMember.getRecommendation2());
            }
            if (permissions.isAlumniWorkDepartment()) {
                updatedMember.setAlumniWorkDepartment(newMember.getAlumniWorkDepartment());
            }
            if (permissions.isRemarksBoard()) {
                updatedMember.setRemarksBoard(newMember.getRemarksBoard());
            }
            if (permissions.isLastPayedDonation()) {
                updatedMember.setLastPayedDonation(newMember.getLastPayedDonation());
            }
            if (permissions.isAmountPayed()) {
                updatedMember.setAmountPayed(newMember.getAmountPayed());
            }
            if (permissions.isAmountPayedDonation()) {
                updatedMember.setAmountPayedDonation(newMember.getAmountPayedDonation());
            }
            if (permissions.isBank()) {
                updatedMember.setBankAccountCity(newMember.getBankAccountCity());
                updatedMember.setBankAccountName(newMember.getBankAccountName());
                updatedMember.setBankAccountNumber(newMember.getBankAccountNumber());
                updatedMember.setBankName(newMember.getBankName());
            }
            if (permissions.isPassword()) {
                if (newMember.getPassword().startsWith("_")) {
                    updatedMember.setPassword(newMember.getPassword().substring(1));
                } else if (!newMember.getPassword().equals(Member.DO_NOT_CHANGE) && !updatedMember.getPassword().equals(newMember.getPassword())) {
                    logger.debug("Encrypt: " + newMember.getPassword());
                    updatedMember.setPassword(Password.md5sum(newMember.getPassword()));
                }
            }
            if (permissions.isStudy()) {
                Set<MyStudy> myStudies = newMember.getMyStudies();
                Set<MyStudy> myUpdatedStudies = new HashSet<MyStudy>();
                for (MyStudy newStudy : myStudies) {
                    Study study = studyManager.getStudy(newStudy.getStudy().getInstitution(), newStudy.getStudy().getStudy());
                    myUpdatedStudies.add(new MyStudy(newStudy.getStudyEnd(), study));
                }
                updatedMember.setMyStudies(myUpdatedStudies);
            }
            if (permissions.isChannels()) {
                ArrayList<Channel> out = new ArrayList<Channel>();
                ArrayList in = newMember.getChannels();
                for (Iterator iter = in.iterator(); iter.hasNext(); ) {
                    Object object = iter.next();
                    if (object != null) {
                        String name = "";
                        if (object instanceof String) {
                            name = (String) object;
                        } else {
                            Channel tmp = (Channel) object;
                            name = tmp.getName();
                        }
                        Channel retrievedChannel = channelManager.getChannel(name);
                        logger.debug(retrievedChannel.getName());
                        out.add(retrievedChannel);
                    }
                }
                updatedMember.setChannels(out);
            }
            if (permissions.isFunctions()) {
                ArrayList<Function> out = new ArrayList<Function>();
                ArrayList in = newMember.getFunctions();
                for (Iterator iter = in.iterator(); iter.hasNext(); ) {
                    Object object = iter.next();
                    if (object != null) {
                        String name = "";
                        if (object instanceof String) {
                            name = (String) object;
                        } else {
                            Function tmp = (Function) object;
                            name = tmp.getName();
                        }
                        Function retrievedFunction = functionManager.getFunction(name);
                        out.add(retrievedFunction);
                    }
                }
                updatedMember.setFunctions(out);
            }
            try {
                id = mmd.setMember(updatedMember);
                lockManager.releaseLock(lock);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        if (who != MemberPermissions.USER_ID_ADMIN_INTERN) {
            logManager.save(compareMembers(oldMember, updatedMember, who, ""));
            new Rules(this).process();
        }
        return id;
    }
