    public String prvunreadcount(HkRequest req, HkResponse resp) {
        try {
            User user = this.getUser(req);
            int count = this.noticeDao.countByUseridForUnread(user.getUserid());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("count", count);
            APIUtil.writeData(resp, map, "vm/unreadcount.vm");
        } catch (Exception e) {
            log.error(e.getMessage());
            this.writeSysErr(resp);
        }
        return null;
    }
