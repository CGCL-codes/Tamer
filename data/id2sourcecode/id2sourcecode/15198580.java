    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, java.io.IOException {
        User user = this.getUser(request);
        UserProfile userProfile = this.getUserService().getUserProfileById(user.getId());
        request.setAttribute("user", user);
        request.setAttribute("userProfile", userProfile);
        String part = request.getParameter("action");
        if (part == null) part = "name";
        request.setAttribute("current", part);
        try {
            JSONObject json = new JSONObject();
            json.put("current", part);
            String success = "";
            if (request.getAttribute("success") != null) success = (String) request.getAttribute("success");
            json.put("success", success);
            String error = "";
            if (request.getAttribute("error") != null) error = (String) request.getAttribute("error");
            json.put("error", error);
            if (part.equals("channels")) {
                json.put("view", "myAccountChannels.view");
                List<UserObjectRole> userChannels = this.getUserService().getUserChannels(user.getId(), user.getOrgId());
                List<String> selectedChannels = new ArrayList<String>();
                if (userChannels != null) {
                    for (UserObjectRole ur : userChannels) {
                        selectedChannels.add(ur.getRole().getName());
                    }
                }
                List<LabelBean> channels = OrganizationThreadLocal.getOrg().getChannels();
                for (LabelBean channel : channels) {
                    if (selectedChannels.contains(channel.getId())) channel.setDefaulted(true); else channel.setDefaulted(false);
                }
                request.setAttribute("channels", channels);
                request.setAttribute("totalChannels", channels.size());
                json.put("totalChannels", channels.size());
                json.put("channels", JSONUtil.getLabelBeans(channels));
            } else if (part.equals("invite")) {
                json.put("view", "myAccountInvite.view");
                List<UserInvite> invites = this.getUserService().getUserInvites(this.getUser(request).getId());
                int inviteCount = 0;
                if (invites != null) inviteCount = invites.size();
                request.setAttribute("inviteCount", inviteCount);
                request.setAttribute("invites", invites);
                json.put("inviteCount", inviteCount);
                json.put("invites", JSONUtil.getUserInviteJArray(invites));
            } else if (part.equals("block")) {
                json.put("view", "myAccountBlock.view");
                List<UserBlock> blocks = this.getUserService().getUserBlocks(this.getUser(request).getId());
                int blockCount = 0;
                if (blocks != null) blockCount = blocks.size();
                request.setAttribute("blockCount", blockCount);
                request.setAttribute("blocks", blocks);
                json.put("blockCount", blockCount);
                json.put("blocks", JSONUtil.getUserBlockJArray(blocks));
            } else if (part.equals("password")) {
                json.put("view", "myAccountPassword.view");
            } else {
                if (part.equals("basic")) json.put("view", "myAccountBasic.view"); else if (part.equals("interests")) json.put("view", "myAccountInterests.view"); else if (part.equals("background")) json.put("view", "myAccountBackground.view"); else if (part.equals("privacy")) json.put("view", "myAccountPrivacy.view"); else json.put("view", "myAccountName.view");
                json.put("user", JSONUtil.getUserJSON(user));
                json.put("userProfile", JSONUtil.getUserProfileJSON(userProfile));
            }
            response.getWriter().write(json.toString());
        } catch (Exception e) {
        }
    }
