import java.util.Date;
import java.util.Vector;

public class MockMeetingManager extends MeetingManager{
    private MockDB db;

    private static String DATE_VALIDATOR_URL = "http://localhost:" + Utils.DateServerPort;

    public MockMeetingManager(MockDB db) {
        this.db = db;
    }

    @Override
    protected Vector<Meeting> getAllMeetings() {
        return db.getAllMeetings();
    }
    @Override
    protected void saveMeeting(Meeting meeting) {
        db.addMeeting(meeting);
    }
    @Override
    protected boolean checkDate(Date date){
        return this.checkDateRequest(date, DATE_VALIDATOR_URL);
    }
}