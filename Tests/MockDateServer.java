import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.RegexBody;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class MockDateServer {
    private ClientAndServer mockServer;
    @BeforeClass
    public void startServer() {
        mockServer = startClientAndServer(Utils.DateServerPort);
//        initDateServer();
    }

    @AfterClass
    public void stopServer() {
        mockServer.stop();
    }

    public static void main(String[] args) {
        MockDateServer m = new MockDateServer();
        m.startServer();
        m.initDateServer();
    }

    private void initDateServer() {
        new MockServerClient("127.0.0.1", Utils.DateServerPort)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/true")
                                .withHeader("\"User-Agent\", \"Mozilla/5.0\"")
                                .withHeader("\"Accept\", \"*/*\"")
//                                .withBody(RegexBody.regex("{date=[0-9]*}"))
                        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("{ message: 'success' }")
                                .withDelay(TimeUnit.MILLISECONDS,10)
                );
        new MockServerClient("127.0.0.1", Utils.DateServerPort)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/false")
                                .withHeader("\"User-Agent\", \"Mozilla/5.0\"")
                                .withHeader("\"Accept\", \"*/*\"")
//                                .withBody(RegexBody.regex("{date=[0-9]*}"))
                )
                .respond(
                        response()
                                .withStatusCode(400)
                                .withBody("{ message: 'failure' }")
                                .withDelay(TimeUnit.MILLISECONDS,10)
                );
        }
}