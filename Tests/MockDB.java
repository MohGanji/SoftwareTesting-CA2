import java.util.Vector;

public class MockDB {
    private Vector<Meeting> meetings = new Vector<>();
    private boolean called = false;

    public boolean isCalled() {
        return called;
    }

    public Vector<Meeting> getAllMeetings(){
        return this.meetings;
    }

    public void addMeeting(Meeting meeting){
        meetings.add(meeting);
        called = true;
    }
}
