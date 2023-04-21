package org.nomicron.suber.model.factory;

import org.nomicron.suber.model.bean.PointType;
import org.nomicron.suber.model.dao.PointEventDao;
import org.nomicron.suber.model.object.Player;
import org.nomicron.suber.model.object.PointEvent;
import java.math.BigDecimal;
import java.util.List;

/**
 * Factory for PointEvent objects.
 */
public class PointEventFactory {

    private PointEventDao pointEventDao;

    /**
     * Set the Dao through the spring application context.
     *
     * @param pointEventDao PointEventDao
     */
    public void setPointEventDao(PointEventDao pointEventDao) {
        this.pointEventDao = pointEventDao;
    }

    /**
     * Get the PointEvent with the specified id.
     *
     * @param id id
     * @return PointEvent
     */
    public PointEvent getPointEventById(Integer id) {
        return pointEventDao.findById(id);
    }

    /**
     * Find the list of valid (excluding DELETED) point events for the specified player and point type.
     *
     * @param player    Player to get points for
     * @param pointType PointType
     * @return List of PointEvents
     */
    public List<PointEvent> findValidByPlayerAndPointType(Player player, PointType pointType) {
        return pointEventDao.findValidByPlayerAndPointType(player, pointType);
    }

    /**
     * Find the list of point events for the specified player, point type and status.
     *
     * @param player           Player to get points for
     * @param pointType        PointType
     * @param pointEventStatus PointEventStatus
     * @return List of PointEvents
     */
    public List<PointEvent> findByPlayerPointTypeAndStatus(Player player, PointType pointType, PointEvent.PointEventStatus pointEventStatus) {
        return pointEventDao.findByPlayerPointTypeAndStatus(player, pointType, pointEventStatus);
    }

    /**
     * Find the list of PointEvents for the specified Player and PointEventStatus.
     *
     * @param player           Player to get points for
     * @param pointEventStatus PointEventStatus
     * @return List of PointEvents
     */
    public List<PointEvent> findByPlayerAndStatus(Player player, PointEvent.PointEventStatus pointEventStatus) {
        return pointEventDao.findByPlayerAndStatus(player, pointEventStatus);
    }

    /**
     * Get the player's point total for the specified PointType.
     *
     * @param player    Player
     * @param pointType PointType
     * @return point total
     */
    public BigDecimal getPointTotal(Player player, PointType pointType) {
        BigDecimal pointTotal = new BigDecimal(0);
        for (PointEvent pointEvent : findValidByPlayerAndPointType(player, pointType)) {
            if (pointEvent.getPointAmount() != null) {
                pointTotal = pointTotal.add(pointEvent.getPointAmount());
            }
        }
        return pointTotal;
    }
}
