package org.messageforge.quickfixj.admin.client.bus;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.messageforge.quickfixj.admin.client.FixAdminContext;
import org.messageforge.quickfixj.admin.client.RecordDefRepositoryService;
import org.messageforge.quickfixj.admin.client.SessionAdminMBeanChangedEvent;
import org.messageforge.quickfixj.admin.client.SessionKey;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.pagebus.PageBus;
import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListenerAdapter;

public class EventServicePageBusAdapter implements SessionEventListener {

    private static final Map<String, String> lastStateMap = new HashMap<String, String>();

    private final FixAdminContext context;

    private final Set<SessionKey> keys = new HashSet<SessionKey>();

    private RemoteEventService eventService;

    static {
        lastStateMap.put("disconnect", "Disconnected");
        lastStateMap.put("connect", "Connected");
        lastStateMap.put("reset", "Reset");
        lastStateMap.put("logon", "Logon");
        lastStateMap.put("logout", "Logout");
        lastStateMap.put("refresh", "Refresh");
        lastStateMap.put("heartBeatTimeout", "Timeout");
    }

    public EventServicePageBusAdapter(FixAdminContext context) {
        this.context = context;
    }

    public String getLastState(String key) {
        if (lastStateMap.containsKey(key)) {
            context.log("state change: " + key + " to " + lastStateMap.get(key));
            return lastStateMap.get(key);
        } else {
            context.log("state change: " + key + " has no mapping");
            return key;
        }
    }

    public void run() {
        final RemoteEventServiceFactory eventServiceFactory = RemoteEventServiceFactory.getInstance();
        eventService = eventServiceFactory.getRemoteEventService();
        eventService.addListener(SessionAdminMBeanChangedEvent.DOMAIN, this);
        eventService.addUnlistenListener(UnlistenEventListener.Scope.LOCAL, new UnlistenEventListenerAdapter() {

            public void onUnlisten(UnlistenEvent anUnlistenEvent) {
                PageBus.publish(RecordDefRepositoryService.TIMEOUT, createTimeoutRecord());
            }
        }, new AsyncCallback<Void>() {

            public void onSuccess(Void result) {
                context.log("Timeout listener established.");
            }

            public void onFailure(Throwable e) {
                context.log(e);
            }
        });
        update();
    }

    public void update() {
        context.getEvents().getSnapshot(new AsyncCallback<SessionAdminMBeanChangedEvent[]>() {

            public void onSuccess(SessionAdminMBeanChangedEvent[] result) {
                for (final SessionAdminMBeanChangedEvent info : result) {
                    onSnapshot(info);
                }
            }

            public void onFailure(Throwable caught) {
                context.log(caught);
            }
        });
    }

    public void log(String message) {
        final Object[] data = new Object[] { new Date(), message };
        PageBus.publish(RecordDefRepositoryService.LOGGING, context.getRecordDefRepositoryService().getLoggingRecordDef().createRecord(data));
    }

    public Record createTimeoutRecord() {
        final Object[] data = new Object[] { new Date() };
        return context.getRecordDefRepositoryService().getServerTimeoutRecordDef().createRecord(data);
    }

    public Record createRecord(SessionAdminMBeanChangedEvent sessionInfo) {
        final Object[] data = new Object[] { sessionInfo.getBeginString(), sessionInfo.getSenderCompID(), sessionInfo.getTargetCompID(), format(sessionInfo.getLastLoginTime()), format(sessionInfo.getLastLogoutTime()), format(sessionInfo.getLastMessageTime()), sessionInfo.getNextSenderMsgSeqNum(), sessionInfo.getNextTargetSeqNum(), sessionInfo.isLoggedOn(), sessionInfo.getConnectionRole(), getLastState(sessionInfo.getLastStatus()), sessionInfo.getUpdateTime() };
        return context.getRecordDefRepositoryService().getFixSessionRecordDef().createRecord(data);
    }

    public void onSnapshot(SessionAdminMBeanChangedEvent sessionInfo) {
        PageBus.publish(RecordDefRepositoryService.FIX_SNAP_SESSION_TOPIC, createRecord(sessionInfo));
    }

    public void onSessionChangedEvent(SessionAdminMBeanChangedEvent sessionInfo) {
        PageBus.publish(RecordDefRepositoryService.FIX_SESSION_UPDATE_TOPIC, createRecord(sessionInfo));
    }

    public void apply(Event event) {
        if (event instanceof SessionAdminMBeanChangedEvent) {
            onSessionChangedEvent((SessionAdminMBeanChangedEvent) event);
        } else {
            context.log("unknown event class=" + event.getClass());
        }
    }

    private Date format(Date date) {
        return date;
    }
}
