package com.l2jserver.gameserver.network.serverpackets;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mmocore.network.SendablePacket;
import com.l2jserver.Config;
import com.l2jserver.gameserver.network.L2GameClient;

/**
 *
 * @author  KenM
 */
public abstract class L2GameServerPacket extends SendablePacket<L2GameClient> {

    private static final Logger _log = Logger.getLogger(L2GameServerPacket.class.getName());

    protected boolean _invisible = false;

    /**
	 * 
	 * @return True if packet originated from invisible character.
	 */
    public boolean isInvisible() {
        return _invisible;
    }

    /**
	 * Set "invisible" boolean flag in the packet.
	 * Packets from invisible characters will not be broadcasted to players.
	 * @param b
	 */
    public void setInvisible(boolean b) {
        _invisible = b;
    }

    /**
	 * @see com.l2jserver.mmocore.network.SendablePacket#write()
	 */
    @Override
    protected void write() {
        try {
            writeImpl();
        } catch (Exception e) {
            _log.log(Level.SEVERE, "Client: " + getClient().toString() + " - Failed writing: " + getType() + " - L2J Server Version: " + Config.SERVER_VERSION + " - DP Revision: " + Config.DATAPACK_VERSION + " ; " + e.getMessage(), e);
        }
    }

    public void runImpl() {
    }

    protected abstract void writeImpl();

    /**
	 * @return A String with this packet name for debuging purposes
	 */
    public abstract String getType();
}
