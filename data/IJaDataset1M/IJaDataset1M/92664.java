package com.oha.etereum.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.oha.etereum.shared.dto.LyricDTO;

@RemoteServiceRelativePath("lyricMgmtRPCService")
public interface LyricMgmtRPCService extends RemoteService {

    void saveLyric(LyricDTO lyric);
}
