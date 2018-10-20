import java.io.IOException;

public class MockPerson extends Person{
    private int notifiedCount;

    public MockPerson(String name, String email) {
        super(name, email);
        this.notifiedCount = 0;
    }

    @Override
    public void notifyUser() throws IOException{
        String url = "http://127.0.0.1:" + Utils.MailServerPort + "/success";
        sendNotifyRequest(url);
        notifiedCount++;
    }

    public int getNotifiedCount() {
        return notifiedCount;
    }
}
