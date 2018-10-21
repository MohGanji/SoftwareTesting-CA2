import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MeetingManagerTest {

    //    private MockPerson p1, p2, p3, p4, p5;
    private Vector<MockPerson> persons = new Vector<>();
    private Vector<Person> attendees1, attendees2, attendees3;
    private Meeting m1, m2, m3;
    private FakeDB db;



    @Before
    public void initialize(){
        persons.add(new MockPerson ("Ali","Alavi"));
        persons.add(new MockPerson ("Reza","Razavi"));
        persons.add(new MockPerson ("Gojeh","Farangi"));
        persons.add(new MockPerson ("Javad","Javadi"));
        persons.add(new MockPerson ("Hasan","Hasani"));
        attendees1 = new Vector<>();
        attendees1.add(persons.get(0));
        attendees1.add(persons.get(1));
        attendees1.add(persons.get(2));
        attendees2 = new Vector<>();
        attendees2.add(persons.get(2));
        attendees2.add(persons.get(3));
        attendees2.add(persons.get(4));
        attendees3 = new Vector<>();
        m1 = new Meeting(attendees1, new Date());
        m2 = new Meeting(attendees2, new Date());
        m3 = new Meeting(attendees3, new Date());

    }

    @Test(expected = Exception.class)
    public void bookMeeting_bookAMeetingWithBadDate_meetingNotSavedIntoDatabase() throws Exception {
        db = new FakeDB();
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.setDateValidatorUrl("http://localhost:" + Utils.DateServerPort + "/false");
        meetingManager.bookMeeting(attendees1, 10);
    }

    @Test
    public void bookMeeting_bookMeetingWithGoodDate_meetingSavedIntoDatabase() throws Exception{
        db = new FakeDB();
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.setDateValidatorUrl("http://localhost:" + Utils.DateServerPort + "/true");
        meetingManager.bookMeeting(attendees1, 10);
        assertTrue(db.isCalled());
    }

    @Test
    public void notifyMeetings_notifyDatabaseMeetings_notifyAUserInOneMeetingOnce() throws IOException{
        db = new FakeDB();
        db.addMeeting(m1);
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.notifyMeetings();
        assertEquals(persons.get(0).getNotifiedCount(), 1);
    }

    @Test
    public void notifyMeetings_notifyDatabaseMeetings_notifyAUserInTwoMeetingTwice() throws IOException{
        db = new FakeDB();
        db.addMeeting(m1);
        db.addMeeting(m2);
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.notifyMeetings();
        assertEquals(persons.get(2).getNotifiedCount(), 2);
    }

    @Test
    public void notifyMeetings_notifyDatabaseMeetings_notifyAMeetingWithNoUsers() throws IOException {
        db = new FakeDB();
        db.addMeeting(m3);
        MockMeetingManager meetingManager = new MockMeetingManager(db);
        meetingManager.notifyMeetings();
        for (MockPerson person: persons){
            assertEquals(person.getNotifiedCount(), 0);
        }
    }
}
