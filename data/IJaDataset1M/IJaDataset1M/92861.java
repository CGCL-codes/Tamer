package net.sourceforge.brumtab.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Contains all data relating to a debate tournament, and methods for manipulating 
 * the tournament.
 * @author Pete
 *
 */
public class Tournament extends Observable implements Serializable {

    static final long serialVersionUID = -3669163236107653242L;

    private int numRounds;

    private int currentRound = -1;

    private Round[] rounds;

    public List<Team> teams;

    public List<AbsentTeam> absentTeams;

    public List<Team> swingTeams;

    private String tournamentName;

    public List<String> roomNames;

    private String baseFilename;

    /**
	 * Returns a Tournament object which is the top-level object for a competition. 
	 * All rounds, teams and judges are contained within.
	 * @param numRounds the number of rounds in the Tournament.
	 */
    public Tournament(int numRounds) {
        this.numRounds = numRounds;
        rounds = new Round[numRounds];
        teams = new ArrayList<Team>(32);
        absentTeams = new ArrayList<AbsentTeam>(4);
        swingTeams = new ArrayList<Team>(4);
        roomNames = new ArrayList<String>(32 / 4);
    }

    /**
	 * Validates and where possible fixes elements of the tournament
	 */
    public void sanityCheck() {
        Set<Integer> uniqueIDs = new HashSet<Integer>();
        for (Team team : teams) {
            uniqueIDs.add(team.getPosition());
        }
        for (AbsentTeam team : absentTeams) {
            uniqueIDs.add(team.absentTeam.getPosition());
        }
        if (uniqueIDs.size() != (teams.size() + absentTeams.size())) {
            System.err.println("Multiple teams have the same ID, or are missing IDs - fixing...");
            int teamCounter = 0;
            for (Team team : teams) {
                team.setPosition(teamCounter);
                teamCounter++;
            }
            for (AbsentTeam team : absentTeams) {
                team.absentTeam.setPosition(teamCounter);
                teamCounter++;
            }
        }
    }

    /**
	 * Adds an observer to the data, which is informed of changes. 
	 */
    public void addObserver(Observer o) {
        super.addObserver(o);
    }

    /**
	 * Creates a Round object, assigns it to the tournament and performs draw
	 * @param round the round number, indexed from 0.
	 * @param method the draw method. See Round class documentation.
	 * @return whether the draw was successful
	 */
    public int drawRound(int round, String method) {
        try {
            rounds[round] = new Round(teams.size() / 4, roomNames, round);
        } catch (Exception e) {
            System.err.println("Unable to create round " + e.getMessage() + " (0 indexing). Round number is out of range.");
        }
        int result = rounds[round].draw(method, this.teams);
        if (result > -1) {
            currentRound = round;
        } else {
            System.out.println("Tournament: Draw algorithm returned error");
        }
        return result;
    }

    /**
	 * Add team to tournament
	 * @param inst
	 * @param teamCode
	 * @param speaker1
	 * @param speaker2
	 */
    public void addTeam(String inst, String teamCode, String speaker1, String speaker2) {
        teams.add(new Team(teamCode, inst, numRounds, speaker1, speaker2, getNextID()));
        setChanged();
        notifyObservers();
    }

    /**
	 * Gets the ID of team with given name
	 * TODO Extend to absent teams and swing teams
	 * @param teamName the team's name in the form [institution code] (without braces).
	 * @return ID of the team if found, otherwise -1
	 * @deprecated
	 */
    public int findTeamID(String teamName) {
        String[] parts = teamName.split(" ");
        if (parts.length != 2) {
            System.err.println("Invalid team name provided to findTeamID - " + teamName);
            return -1;
        }
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getInstitution().equals(parts[0]) && teams.get(i).getTeamCode().equals(parts[1])) {
                return teams.get(i).getPosition();
            }
        }
        return -1;
    }

    /**
	 * Gets the Team with the given ID (inverse of <code>findTeamID</code>)
	 * @param ID the ID to search for
	 * @return the Team object if found, otherwise returns null
	 */
    public Team getTeamFromID(int ID) {
        for (int i = 0; i < teams.size(); i++) {
            if (this.teams.get(i).getPosition() == ID) return this.teams.get(i);
        }
        for (int i = 0; i < absentTeams.size(); i++) {
            if (this.absentTeams.get(i).absentTeam.getPosition() == ID) return this.absentTeams.get(i).absentTeam;
        }
        for (int i = 0; i < swingTeams.size(); i++) {
            if (this.swingTeams.get(i).getPosition() == ID) return this.swingTeams.get(i);
        }
        System.err.println("Team not found in getTeamWithID");
        return null;
    }

    /**
	 * Change the number of rounds in the tournament
	 * @param newNumRounds the new number of rounds
	 */
    public void changeNumRounds(int newNumRounds) {
        if (newNumRounds == this.numRounds) return;
        int oldNumRounds = this.numRounds;
        this.rounds = (Round[]) resizeArray(this.rounds, newNumRounds);
        ListIterator<Team> iterator = this.teams.listIterator();
        double[] points;
        Team theTeam;
        while (iterator.hasNext()) {
            theTeam = iterator.next();
            points = theTeam.getTeamPointsArray();
            points = (double[]) resizeArray(points, newNumRounds);
            theTeam.setTeamPointsArray(points);
            points = theTeam.getSpeaker(0).getPoints();
            points = (double[]) resizeArray(points, newNumRounds);
            theTeam.getSpeaker(0).setSpeakerPoints(points);
            points = theTeam.getSpeaker(1).getPoints();
            points = (double[]) resizeArray(points, newNumRounds);
            theTeam.getSpeaker(1).setSpeakerPoints(points);
        }
        this.numRounds = newNumRounds;
    }

    /**
	 * Resize an array. Source: http://www.source-code.biz/snippets/java/3.htm
	 */
    private static Object resizeArray(Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        return newArray;
    }

    public Iterator teamIterator() {
        Iterator it = teams.iterator();
        return it;
    }

    public Iterator absentTeamIterator() {
        Iterator it = absentTeams.iterator();
        return it;
    }

    public Iterator swingTeamIterator() {
        Iterator it = swingTeams.iterator();
        return it;
    }

    public void addSwingTeam(Team swing) {
        this.swingTeams.add(swing);
    }

    public boolean getTeamAvailability(Team theTeam) {
        return teams.contains(theTeam);
    }

    /**
	 * Get the highest current team ID + 1
	 * @return the ID
	 */
    public int getNextID() {
        if (this.teams.size() == 0) return 0;
        int max = -1;
        Team temp;
        AbsentTeam temp2;
        Iterator it = teams.iterator();
        while (it.hasNext()) {
            temp = (Team) it.next();
            if (temp.getPosition() > max) max = temp.getPosition();
        }
        it = absentTeams.iterator();
        while (it.hasNext()) {
            temp2 = (AbsentTeam) it.next();
            if (temp2.absentTeam.getPosition() > max) max = temp2.absentTeam.getPosition();
        }
        it = swingTeams.iterator();
        while (it.hasNext()) {
            temp = (Team) it.next();
            if (temp.getPosition() > max) max = temp.getPosition();
        }
        return max + 1;
    }

    public int getNumRounds() {
        return this.numRounds;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public int getNumTeams() {
        return this.teams.size();
    }

    public int getNumAbsentTeams() {
        return this.absentTeams.size();
    }

    public int getNumRooms() {
        return roomNames.size();
    }

    public Team getTeam(int index) {
        return this.teams.get(index);
    }

    public Team getAbsentTeam(int index) {
        return this.absentTeams.get(index).absentTeam;
    }

    public Team getTeamFromName(String inst, String code) {
        for (int i = 0; i < teams.size(); i++) {
            if (this.teams.get(i).getInstitution().equals(inst) && this.teams.get(i).getTeamCode().equals(code)) return this.teams.get(i);
        }
        for (int i = 0; i < absentTeams.size(); i++) {
            if (this.absentTeams.get(i).absentTeam.getInstitution().equals(inst) && this.absentTeams.get(i).absentTeam.getTeamCode().equals(code)) return this.absentTeams.get(i).absentTeam;
        }
        for (int i = 0; i < swingTeams.size(); i++) {
            if (this.swingTeams.get(i).getInstitution().equals(inst) && this.swingTeams.get(i).getTeamCode().equals(code)) return this.swingTeams.get(i);
        }
        return null;
    }

    public Round getRound(int index) {
        return this.rounds[index];
    }

    public void setTournamentName(String name) {
        this.tournamentName = name;
    }

    public String getTournamentName() {
        return this.tournamentName;
    }

    public double getTeamResult(Team team, int round) {
        return team.getScore(round);
    }

    public double getTotalSpeakerPoints(int team) {
        return this.teams.get(team).getTotalSpeakerPoints();
    }

    public void setTeamResult(int team, int round, double score) {
        this.teams.get(team).setResult(round, score);
        setChanged();
        notifyObservers();
    }

    public String getBaseFilename() {
        return baseFilename;
    }

    public void setBaseFilename(String baseFilename) {
        this.baseFilename = baseFilename;
    }

    public boolean setTeamAbsent(int ID, boolean absent, Team swingTeam) {
        Team theTeam = getTeamFromID(ID);
        if (theTeam == null) return false;
        return setTeamAbsent(theTeam, absent, swingTeam);
    }

    public boolean setTeamAbsent(Team theTeam, boolean absent, Team swingTeam) {
        if (theTeam == null) return false;
        if (absent == true) {
            AbsentTeam absentTeam = new AbsentTeam();
            absentTeam.absentTeam = theTeam;
            if (swingTeam != null) {
                absentTeam.swingTeam = swingTeam;
                teams.add(swingTeam);
            }
            absentTeams.add(absentTeam);
            teams.remove(theTeam);
            return true;
        } else {
            AbsentTeam absentTeam = new AbsentTeam();
            for (int i = 0; i < absentTeams.size(); i++) {
                if (absentTeams.get(i).absentTeam == theTeam) absentTeam = absentTeams.get(i);
            }
            if (absentTeam == null) return false;
            if (absentTeam.swingTeam != null) {
                Team swingTeamPointer = absentTeam.swingTeam;
                AbsentTeam absentSwingTeam = new AbsentTeam();
                absentSwingTeam.absentTeam = swingTeamPointer;
                absentTeams.add(absentSwingTeam);
                teams.remove(swingTeamPointer);
            }
            teams.add(absentTeam.absentTeam);
            absentTeams.remove(absentTeam);
            return true;
        }
    }

    public List<String> getRoomNames() {
        return roomNames;
    }

    public void setRoomNames(List<String> roomNames) {
        this.roomNames = roomNames;
    }

    /**
	 * Sorts the teams into ascending order, leaving the result in-situ
	 *
	 */
    public void sortTeams() {
        Collections.sort(teams);
        setChanged();
        notifyObservers();
    }

    public class AbsentTeam implements Serializable {

        public Team absentTeam;

        public Team swingTeam;
    }
}
