import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;



public class MockMailServer {
    private ClientAndServer mockServer;
    @BeforeClass
    public void startServer() {
        mockServer = startClientAndServer(Utils.MailServerPort);
        initMailServer();
    }

    @AfterClass
    public void stopServer() {
        mockServer.stop();
    }

    public static void main(String[] args) {
        MockMailServer m = new MockMailServer();
        m.startServer();
        m.initMailServer();
    }

    private void initMailServer() {
        new MockServerClient("127.0.0.1", Utils.MailServerPort)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/success")
                                .withHeader("\"Accept-Language\", \"en-US,en;q=0.5\"")
//                                .withQueryStringParameter("email", "")
                        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("{ message: 'success' }")
                                .withDelay(TimeUnit.MILLISECONDS,10)
                );
//        new MockServerClient("127.0.0.1", Utils.MailServerPort)
//                .when(
//                        request()
//                                .withMethod("POST")
//                                .withPath("/fail")
//                                .withHeader("\"Accept-Language\", \"en-US,en;q=0.5\"")
//                                .withQueryStringParameter("email", "")
//                )
//                .respond(
//                        response()
//                                .withStatusCode(200)
//                                .withBody("{ message: 'success' }")
//                                .withDelay(TimeUnit.MILLISECONDS,10)
//                );

    }
    // ...
}
