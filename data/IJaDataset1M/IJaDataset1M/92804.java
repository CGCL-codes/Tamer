package com.meidusa.amoeba.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * net Event handler
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public interface NetEventHandler extends IdleChecker {

    /**
	 * ��ʱhandler��Ҫ���� when ʱ�� �� handle �������¼���
	 * 
	 * @param when
	 * @return
	 */
    public int handleEvent(long when);

    public SelectionKey getSelectionKey();

    public void setSelectionKey(SelectionKey selkey);

    public boolean doWrite() throws IOException;
}
