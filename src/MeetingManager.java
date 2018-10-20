import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Vector;

public class MeetingManager {
    private static MeetingManager ourInstance = new MeetingManager();
    private static String DATE_VALIDATOR_URL = "http://localhost:8000/checkdate";

    public static MeetingManager getInstance() {
        return ourInstance;
    }

    public void bookMeeting(Vector<Person> attendees, int day_delta) throws Exception {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day_delta);
        if (!this.checkDate(c.getTime())) {
            throw new Exception();
        }
        Meeting meeting = new Meeting(attendees, c.getTime());
        this.saveMeeting(meeting);
    }

    public void setDateValidatorUrl(String url) {
        DATE_VALIDATOR_URL = url;
    }

    private boolean checkDate(java.util.Date date) {
        try {
            URL obj = new URL(MeetingManager.DATE_VALIDATOR_URL);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            OutputStreamWriter outputWriter = new OutputStreamWriter(con.getOutputStream());
            outputWriter.write(String.format("{date=%s}", date.getTime()));
            outputWriter.flush();
            outputWriter.close();

            return 200 == con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void notifyMeetings() throws IOException {
        for (Meeting meeting : this.getAllMeetings()) {
            meeting.notifyUsers();
        }
    }

    protected Vector<Meeting> getAllMeetings() {
        Vector<Meeting> meetings = new Vector<Meeting>();
        String url = "jdbc:sqlite:/tmp/sqlite/ImportantMeetings.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            String sql = "SELECT attendees, date_of_meeting FROM meetings";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                Vector<Person> attendees = new Vector<Person>();
                for (String nameemail : rs.getString("attendees").split("\\s*,\\s*")) {
                    attendees.add(new Person(nameemail.split("\\s*\\|\\s*")[0], nameemail.split("\\s*\\|\\s*")[1]));
                }
                meetings.add(new Meeting(attendees, rs.getDate("date_of_meeting")));
            }
            return meetings;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected void saveMeeting(Meeting meeting) {
        String url = "jdbc:sqlite:/tmp/sqlite/ImportantMeetings.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            String sql = "INSERT INTO meetings(attendees,date_of_meeting) VALUES(?,?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.join(meeting.getAttendees().toString(), ", "));
            pstmt.setDate(2, new Date(meeting.getDate().getTime()));
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
