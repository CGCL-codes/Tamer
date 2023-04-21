    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PlaylistEntryFormBean playlistEntryForm = (PlaylistEntryFormBean) form;
        UserBean currentUser = getCurrentUser(request);
        if (playlistEntryForm.getPlaylistId() <= 0) {
            request.setAttribute("message", "invalid playlist id.");
            return mapping.findForward("error");
        }
        PlaylistBE playlistBe = BEFactory.getPlaylistBE(currentUser);
        PlaylistBean playlist = playlistBe.getPlaylist(playlistEntryForm.getPlaylistId());
        if (playlist == null) {
            request.setAttribute("message", "no such playlist.");
            return mapping.findForward("error");
        }
        if ((playlist.getOwner().getId() != currentUser.getId()) && (playlist.getVisibility() == Globals.VISIBILITY_PRIVATE)) {
            request.setAttribute("message", "ACCESS DENIED - Playlist is owned by " + playlist.getOwner().getName() + " and marked as private.");
            return mapping.findForward("error");
        }
        if ((playlistEntryForm.getAction() != null) || (playlistEntryForm.getDeletebt() != null)) {
            if ((playlist.getOwner().getId() != currentUser.getId()) && (playlist.getVisibility() != Globals.VISIBILITY_PREAD_WRITE)) {
                request.setAttribute("message", "ACCESS DENIED - Playlist is owned by " + playlist.getOwner().getName() + " and not marked as read/write.");
                return mapping.findForward("error");
            }
            if (playlistEntryForm.getAction() != null) {
                if (playlistEntryForm.getAction().equals("up")) {
                    if (playlistEntryForm.getFileId() > 0) playlistBe.moveFile(playlist.getId(), playlistEntryForm.getFileId(), true);
                } else if (playlistEntryForm.getAction().equals("down")) {
                    if (playlistEntryForm.getFileId() > 0) playlistBe.moveFile(playlist.getId(), playlistEntryForm.getFileId(), false);
                } else if (playlistEntryForm.getAction().equals("del")) {
                    if (playlistEntryForm.getFileId() > 0) playlistBe.removeFile(playlist.getId(), playlistEntryForm.getFileId());
                }
            } else if (playlistEntryForm.getDeletebt() != null) {
                if (playlistEntryForm.getFileIds() != null) {
                    int[] fileIds = playlistEntryForm.getFileIds();
                    if ((fileIds != null) && (fileIds.length > 0)) for (int i = 0; i < fileIds.length; i++) if (fileIds[i] > 0) playlistBe.removeFile(playlist.getId(), fileIds[i]);
                }
            }
        }
        ViewBE viewBe = BEFactory.getViewBE(currentUser);
        viewBe.clearFilter(Globals.FILTER_STREAM_PLEDITVIEW);
        viewBe.addFilter(Globals.FILTER_STREAM_PLEDITVIEW, new FilterBean(FilterBean.FILTER_TYPE_PLAYLIST_ORDERBYPOS, playlistEntryForm.getPlaylistId()));
        FileBean[] fileList = viewBe.getFiles(Globals.FILTER_STREAM_PLEDITVIEW);
        request.setAttribute("fileList", fileList);
        request.setAttribute("playlistEntryFormBean", playlistEntryForm);
        return mapping.findForward("success");
    }
