package muvis.view.main.filters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import muvis.Environment;
import muvis.database.EmptyStatement;
import muvis.view.filters.MuVisFilterNode;
import utils.Observable;

/**
 * Filter Decorator
 * @author Ricardo
 */
public class FilterDecorator extends TreemapFilter implements Cloneable {

    protected TreemapFilter parentFilter;

    protected Hashtable<String, MuVisFilterNode> selectedNodes;

    public FilterDecorator(TreemapFilter filter) {
        parentFilter = filter;
        selectedNodes = new Hashtable<String, MuVisFilterNode>();
    }

    public void setParentFilter(TreemapFilter filter) {
        parentFilter = filter;
    }

    @Override
    public int getCountFilteredTracks(String artistName) {
        try {
            String artist = artistName.replace("\'", "\''");
            String filterQuery = getQuery(artist);
            filterQuery = "SELECT COUNT(id) " + "FROM information_tracks_table " + "WHERE " + filterQuery;
            Statement st = new EmptyStatement();
            ResultSet rs = null;
            rs = Environment.getEnvironmentInstance().getDatabaseManager().query(filterQuery, st);
            if (rs.next()) {
                int tracksTotal = rs.getInt(1);
                st.close();
                return tracksTotal;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getCountFilteredAlbuns(String artistName) {
        try {
            String artist = artistName.replace("\'", "\''");
            String filterQuery = getQuery(artist);
            filterQuery = "SELECT COUNT(DISTINCT album_name) " + "FROM information_tracks_table " + "WHERE " + filterQuery;
            Statement st = new EmptyStatement();
            ResultSet rs = null;
            rs = Environment.getEnvironmentInstance().getDatabaseManager().query(filterQuery, st);
            if (rs.next()) {
                int albunsTotal = rs.getInt(1);
                st.close();
                return albunsTotal;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public List getFilteredTracks(String artistName) {
        try {
            String artist = artistName.replace("\'", "\''");
            String filterQuery = getQuery(artist);
            filterQuery = "SELECT id " + "FROM information_tracks_table " + "WHERE " + filterQuery;
            Statement st = new EmptyStatement();
            ResultSet rs = null;
            rs = Environment.getEnvironmentInstance().getDatabaseManager().query(filterQuery, st);
            ArrayList<Integer> filteredTracks = new ArrayList(getCountFilteredTracks(artistName));
            while (rs.next()) {
                int trackId = rs.getInt(1);
                filteredTracks.add(trackId);
            }
            st.close();
            return filteredTracks;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List getFilteredAlbuns(String artistName) {
        try {
            String artist = artistName.replace("\'", "\''");
            String filterQuery = getQuery(artist);
            filterQuery = "SELECT DISTINCT album_name " + "FROM information_tracks_table " + "WHERE " + filterQuery;
            Statement st = new EmptyStatement();
            ResultSet rs = null;
            rs = Environment.getEnvironmentInstance().getDatabaseManager().query(filterQuery, st);
            ArrayList<String> filteredTracks = new ArrayList(getCountFilteredAlbuns(artistName));
            while (rs.next()) {
                String albumName = rs.getString(1);
                filteredTracks.add(albumName);
            }
            st.close();
            return filteredTracks;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void reset() {
        for (MuVisFilterNode node : selectedNodes.values()) {
            node.setSelected(false);
        }
        selectedNodes.clear();
    }

    @Override
    protected String getQuery(String artistName) {
        return "";
    }

    @Override
    public void update(Observable obs, Object arg) {
    }
}
