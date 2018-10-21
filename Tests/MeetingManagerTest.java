import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MeetingManagerTest {

    private MockPerson p1, p2, p3, p4, p5;
    private Vector<Person> attendees1, attendees2;
    private Meeting m1, m2;
    private MockDB db;



    @Before
    public void initialize(){
        p1 = new MockPerson ("Ali","Alavi");
        p2 = new MockPerson ("Reza","Razavi");
        p3 = new MockPerson ("Gojeh","Farangi");
        p4 = new MockPerson ("Javad","Javadi");
        p5 = new MockPerson ("Hasan","Hasani");
        attendees1 = new Vector<>();
        attendees1.add(p1);
        attendees1.add(p2);
        attendees1.add(p3);
        attendees2 = new Vector<>();
        attendees1.add(p3);
        attendees1.add(p4);
        attendees1.add(p5);
        m1 = new Meeting(attendees1, new Date());
        m2 = new Meeting(attendees2, new Date());
    }

    @Test(expected = Exception.class)
    public void bookMeeting_bookAMeetingWithBadDate_meetingNotSavedIntoDatabase() throws Exception {
        db = new MockDB();
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.setDateValidatorUrl("http://localhost:" + Utils.DateServerPort + "/false");
        meetingManager.bookMeeting(attendees1, 10);
    }

    @Test
    public void bookMeeting_bookMeetingWithGoodDate_meetingSavedIntoDatabase() throws Exception{
        db = new MockDB();
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.setDateValidatorUrl("http://localhost:" + Utils.DateServerPort + "/true");
        meetingManager.bookMeeting(attendees1, 10);
        assertTrue(db.isCalled());
    }

    @Test
    public void notifyMeetings_notifyDatabaseMeetings_notifyAUserInOneMeetingOnce() throws IOException{
        db = new MockDB();
        db.addMeeting(m1);
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.notifyMeetings();
        assertEquals(p1.getNotifiedCount(), 1);
    }

    @Test
    public void notifyMeetings_notifyDatabaseMeetings_notifyAUserInTwoMeetingTwice() throws IOException{
        db = new MockDB();
        db.addMeeting(m1);
        db.addMeeting(m2);
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.notifyMeetings();
        assertEquals(p3.getNotifiedCount(), 2);
    }
}
