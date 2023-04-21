    @Override
    public User doRegister(User user) {
        User queryUser = new User();
        queryUser.setEmail(user.getEmail());
        if (null != this.queryForObject(queryUser)) {
            throw new MyException("该email已经被注册！");
        }
        user.setName(user.getEmail().substring(0, user.getEmail().indexOf('@')));
        user.setType("0");
        user.setStatus("1");
        user.setPassword(StringUtil.md5Hex(user.getPassword()));
        this.save(user);
        User ret = (User) this.queryForObject(queryUser);
        try {
            String userId = ret.getId();
            File midDefault = new File(Constant.s("web.uploadHeadPic.mid.defaultFile"));
            File mid = new File(Constant.s("web.uploadHeadPic.mid.dir"), userId + "." + Constant.s("web.uploadHeadPic.mid.suffix"));
            FileUtils.copyFile(midDefault, mid);
            File smallDefault = new File(Constant.s("web.uploadHeadPic.mid.defaultFile"));
            File small = new File(Constant.s("web.uploadHeadPic.mid.dir"), userId + "." + Constant.s("web.uploadHeadPic.small.suffix"));
            FileUtils.copyFile(smallDefault, small);
        } catch (IOException e) {
            throw new MyException("生成默认图片发异常");
        }
        return ret;
    }
