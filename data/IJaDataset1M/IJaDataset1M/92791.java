package org.msgroad.sgip.message;

import org.apache.mina.common.ByteBuffer;
import org.msgroad.ByteUtil;
import org.msgroad.sgip.ProtocolCommandID;

/**
 * @author Jason
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DeliverRespMessage extends SGIPMessage {

    private static final long serialVersionUID = (long) ProtocolCommandID.SGIP_DELIVER_RESP;

    private int result;

    public DeliverRespMessage() {
        commandId = ProtocolCommandID.SGIP_DELIVER_RESP;
    }

    public void encodeBody(ByteBuffer bt) {
        byte result = 0;
        bt.put(result);
        bt.put(ByteUtil.intArray(8));
    }

    public void decodeBody(byte[] body) {
    }

    public int getResult() {
        return result;
    }
}
