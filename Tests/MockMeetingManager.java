import java.util.Date;
import java.util.Vector;

public class MockMeetingManager extends MeetingManager{
    private MockDB db;

    @Override
    public void setDateValidatorUrl(String dateValidatorUrl) {
        this.DATE_VALIDATOR_URL = dateValidatorUrl;
    }

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
        return this.checkDateRequest(date);
    }
}