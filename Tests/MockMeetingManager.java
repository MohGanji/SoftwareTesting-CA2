import java.util.Vector;

public class MockMeetingManager extends MeetingManager{
    private MockDB db;

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
}