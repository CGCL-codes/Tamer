package com.rise.rois.server.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import com.rise.rois.server.db.util.DBManager;
import com.rise.rois.server.model.Computer;
import com.rise.rois.server.model.IndividualSession;
import com.rise.rois.server.model.ScheduledIndividualSession;
import com.rise.rois.server.model.Session;
import com.rise.rois.server.model.User;
import com.rise.rois.server.model.warehouse.ComputerWarehouse;
import com.rise.rois.server.model.warehouse.IndividualSessionWarehouse;
import com.rise.rois.server.model.warehouse.UserWarehouse;

public class ScheduledSessionUtil {

    public static String createTimer(int user_id, int computer_id, String password, long duration, String purpose, String sessionTemplate, long start) {
        DebugUtil.log("Create Timer...", Level.INFO);
        String errors = canStart(computer_id, user_id, start, duration, SessionUtil.SCHEDULED_INDIVIDUAL_ID);
        DebugUtil.log("Complete the [canStart] check for the scheduled session - " + errors, Level.INFO);
        if (errors.length() == 0) {
            DebugUtil.log("Scheduled session cleared for launch...", Level.INFO);
            int session_id = -1;
            ScheduledIndividualSession session = null;
            session = new ScheduledIndividualSession(session_id, 0, 0, duration, purpose);
            session.setComputerId(computer_id);
            session.setUserId(user_id);
            session.setScheduledTimeToStart(start);
            session.SQLCreate(DBManager.getConnection());
            session_id = session.getSessionId();
            IndividualSessionWarehouse.addSession(session);
            ScheduleTimer scheduledTimer = new ScheduleTimer(session, password, sessionTemplate);
            java.util.Timer timer = new java.util.Timer();
            scheduledTimer.setTimer(timer);
            Date startDate = new Date(start);
            DebugUtil.log(Level.INFO, "Schedule session to start at :  " + startDate.toString());
            timer.schedule(scheduledTimer, startDate);
            IndividualSessionWarehouse.addScheduledTimer(scheduledTimer, session_id);
            DebugUtil.log("Return Scheduled Sessions id...", Level.INFO);
            return "ID:" + session_id;
        }
        return errors;
    }

    /**
	 * Test the data from the proposed Session (ideally before the Session is actually created) to see if it clashes with an existing Session.
	 * 
	 * @param session
	 * @return an empty string if the proposed session can start at that time, or a String with the session that it conflicts with
	 */
    public static String canStart(int computerId, int userId, long timeToStart, long duration, int sessionType) {
        long now = Calendar.getInstance().getTimeInMillis();
        if (sessionType == SessionUtil.SCHEDULED_INDIVIDUAL_ID) {
            if (timeToStart <= now) {
                return "The new Session cannot start in the past: " + DateUtil.getForDisplay(new Date(timeToStart));
            }
        }
        ArrayList<IndividualSession> sessions = new ArrayList<IndividualSession>();
        sessions.addAll(IndividualSessionWarehouse.getActiveSessions());
        sessions.addAll(IndividualSessionWarehouse.getScheduledSessions());
        for (IndividualSession individualSession : sessions) {
            String clash = doTimesClash(timeToStart, duration, individualSession);
            if (computerId == individualSession.getComputerId()) {
                if (clash.length() > 0) {
                    return ("Reason: " + clash + ":\n" + getSessionDetails(individualSession));
                }
            } else if (userId == individualSession.getUserId()) {
                if (clash.length() > 0) {
                    return ("Reason: The Session would overlap with a\nSession that the user is already assigned to:\n" + getSessionDetails(individualSession));
                }
            }
        }
        return "";
    }

    /**
	 * Test the data from the proposed Session (ideally before the Session is actually created) to see if it clashes with an existing Session.
	 * 
	 * @param session
	 * @return an empty string if the proposed session can start at that time, or a String with the session that it conflicts with
	 */
    public static String canChangeScheduledStart(int session_id, int computerId, int userId, long timeToStart, long duration) {
        if (timeToStart < Calendar.getInstance().getTimeInMillis()) {
            return "The Session cannot start in the past: " + DateUtil.getForDisplay(new Date(timeToStart));
        }
        ArrayList<IndividualSession> sessions = new ArrayList<IndividualSession>();
        sessions.addAll(IndividualSessionWarehouse.getActiveSessions());
        sessions.addAll(IndividualSessionWarehouse.getScheduledSessions());
        for (IndividualSession individualSession : sessions) {
            if (individualSession.getSessionId() != session_id) {
                String clash = doTimesClash(timeToStart, duration, individualSession);
                if (computerId == individualSession.getComputerId()) {
                    if (clash.length() > 0) {
                        return ("Unable to change the start time.\nReason: " + clash + ".\n" + getSessionDetails(individualSession));
                    }
                } else if (userId == individualSession.getUserId()) {
                    if (clash.length() > 0) {
                        return ("Unable to change the start time.\nReason: The Session would overlap with a\nSession that the user is already assigned to:\n" + getSessionDetails(individualSession));
                    }
                }
            }
        }
        return "";
    }

    public static String canIncreaseDuration(Session individualSession, long timeToAdd) {
        long new_predicted_end = 0;
        long currentStart = 0;
        if (individualSession instanceof ScheduledIndividualSession) {
            ScheduledIndividualSession scheduledIndividualSession = (ScheduledIndividualSession) individualSession;
            new_predicted_end = scheduledIndividualSession.getScheduledTimeToStart() + individualSession.getDuration() + timeToAdd;
            currentStart = scheduledIndividualSession.getScheduledTimeToStart();
        } else {
            new_predicted_end = individualSession.getSession_start_timestamp() + individualSession.getDuration() + timeToAdd;
            currentStart = individualSession.getSession_start_timestamp();
        }
        for (ScheduledIndividualSession scheduledSession : IndividualSessionWarehouse.getScheduledSessions()) {
            if (individualSession.getSessionId() != scheduledSession.getSessionId()) {
                long scheduledSessionStart = scheduledSession.getScheduledTimeToStart();
                long scheduledSessionEnd = scheduledSessionStart + scheduledSession.getDuration();
                if (new_predicted_end >= scheduledSessionStart && new_predicted_end <= scheduledSessionEnd) {
                    return "Unable to increase the duration as it would clash with the Scheduled Session:\n" + getSessionDetails(scheduledSession);
                }
                if (scheduledSessionStart >= currentStart && scheduledSessionStart <= new_predicted_end) {
                    return "Unable to increase the duration as it would clash with the Scheduled Session:\n" + getSessionDetails(scheduledSession);
                }
            }
        }
        return "";
    }

    private static String doTimesClash(long timeToStart, long duration, IndividualSession individualSession) {
        long newSessionStart = timeToStart;
        long newSessionEnd = newSessionStart + duration;
        long existingSessionStart = individualSession.getSession_start_timestamp();
        if (individualSession instanceof ScheduledIndividualSession) {
            ScheduledIndividualSession scheduledIndividualSession = (ScheduledIndividualSession) individualSession;
            existingSessionStart = scheduledIndividualSession.getScheduledTimeToStart();
        }
        long existingSessionEnd = existingSessionStart + individualSession.getDuration() + 60000;
        if (newSessionStart >= existingSessionStart && newSessionStart <= existingSessionEnd) {
            if (individualSession instanceof ScheduledIndividualSession) {
                return "The Session starts during an existing Scheduled Session";
            }
            return "The Session starts during an existing Session";
        }
        if (newSessionEnd >= existingSessionStart && newSessionEnd <= existingSessionEnd) {
            if (individualSession instanceof ScheduledIndividualSession) {
                return "The Session is due to end during an existing Scheduled Session";
            }
            return "The Session is due to end during an existing Session";
        }
        if (existingSessionStart >= newSessionStart && existingSessionStart <= newSessionEnd) {
            if (individualSession instanceof ScheduledIndividualSession) {
                return "The Session overlaps an existing Scheduled Session";
            }
            return "The Session overlaps an existing Session";
        }
        return "";
    }

    private static String getSessionDetails(IndividualSession session) {
        Computer computer = ComputerWarehouse.getComputer(session.getComputerId());
        if (computer != null) {
            String computerName = ComputerWarehouse.getComputer(session.getComputerId()).getComputer_name();
            User user = UserWarehouse.getUser(session.getUserId());
            String userName = user.getFirstname() + " " + user.getSurname();
            String start = "";
            String predictedEnd = "";
            String duration = TimeUtil.convertMillisecondsToDuration(session.getDuration());
            if (session instanceof ScheduledIndividualSession) {
                ScheduledIndividualSession scheduledIndividualSession = (ScheduledIndividualSession) session;
                start = scheduledIndividualSession.getScheduledTimeAsString();
                predictedEnd = new Date(scheduledIndividualSession.getScheduledTimeToStart() + scheduledIndividualSession.getDuration()).toString();
            } else {
                start = DateUtil.getForDisplay(new Date(session.getSession_start_timestamp()));
                predictedEnd = DateUtil.getForDisplay(new Date(session.getSession_start_timestamp() + session.getDuration()));
            }
            if (session instanceof ScheduledIndividualSession) {
                return "\nScheduled Session id: " + session.getSessionId() + "\nComputer: " + computerName + "\nUser: " + userName + "\nStart: " + start + "\nDuration: " + duration + "\nEnd: " + predictedEnd + "\n\nPlease note: you must allow a gap of one minute inbetween Sessions,\ne.g.: if a Session ends at 10:04, another session cannot start until 10:05";
            }
            return "\nSession id: " + session.getSessionId() + "\nComputer: " + computerName + "\nUser: " + userName + "\nStart: " + start + "\nDuration: " + duration + "\nEnd: " + predictedEnd + "\n\nPlease note: you must allow a gap of one minute inbetween Sessions,\ne.g.: if a Session ends at 10:04, another session cannot start until 10:05";
        }
        System.err.println("Unable to find the computer of id: " + session.getComputerId());
        return "";
    }
}
