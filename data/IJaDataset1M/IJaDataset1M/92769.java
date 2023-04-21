package pl.mn.communicator.packet.handlers;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.mn.communicator.GGException;
import pl.mn.communicator.IRemoteStatus;
import pl.mn.communicator.IUser;
import pl.mn.communicator.packet.GGUtils;
import pl.mn.communicator.packet.in.GGNotifyReply60;

/**
 * Created on 2004-12-12
 * 
 * @author <a href="mailto:mnaglik@gazeta.pl">Marcin Naglik</a>
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: GGNotifyReply60PacketHandler.java,v 1.1 2005/11/05 23:34:53 winnetou25 Exp $
 */
public class GGNotifyReply60PacketHandler implements PacketHandler {

    private static final Log logger = LogFactory.getLog(GGNotifyReply60PacketHandler.class);

    /**
	 * @see pl.mn.communicator.packet.handlers.PacketHandler#handle(pl.mn.communicator.packet.handlers.Context)
	 */
    public void handle(PacketContext context) throws GGException {
        if (logger.isDebugEnabled()) {
            logger.debug("NotifyPacketReply60 packet received.");
            logger.debug("PacketHeader: " + context.getHeader());
            logger.debug("PacketBody: " + GGUtils.prettyBytesToString(context.getPackageContent()));
        }
        GGNotifyReply60 notifyReply = new GGNotifyReply60(context.getPackageContent());
        context.getSessionAccessor().notifyGGPacketReceived(notifyReply);
        Map usersStatuses = notifyReply.getUsersStatus();
        for (Iterator it = usersStatuses.keySet().iterator(); it.hasNext(); ) {
            IUser user = (IUser) it.next();
            IRemoteStatus status = (IRemoteStatus) usersStatuses.get(user);
            context.getSessionAccessor().notifyUserChangedStatus(user, status);
        }
    }
}
