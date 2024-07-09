package au.edu.sydney.soft3202.task1;

import org.junit.jupiter.api.*;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;


public class ShoppingBasketTest {


    private ApplicationContext context;
    private String sessionToken;

    @BeforeEach// Fresh server each time
    public void serverStart() {
        context = SpringApplication.run(ShoppingServiceApplication.class); // Literally just run our application.
    }

    @AfterEach // Need to stop the server or else port will remain in use next test
    public void serverStop() {
        SpringApplication.exit(context);
    }


    public void login() {
        try {
            HttpRequest req = HttpRequest.newBuilder(new URI("http://localhost:8080/login?user=A"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            List<String> cookies = resp.headers().allValues("Set-Cookie");
            String cookie = cookies.get(0);
            String sessionValue = cookie.split(";")[0].split("=")[1];
            sessionToken = sessionValue;

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail();
        }
    }

    // Do whatever


    @Test
    public void loginTestValid() {
        try {

            HttpRequest req = HttpRequest.newBuilder(new URI("http://localhost:8080/login?user=A"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();


            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(302, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void loginTestInvalid() {
        try {

            HttpRequest req = HttpRequest.newBuilder(new URI("http://localhost:8080/login?user=F"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(401, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void getCartTestInvalidSessionToken() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/cart"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }


    @Test
    public void getCartTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/cart"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void postCartTestSuitableQtyValidSessionToken() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/cart?name=apple&qty=2&cost=2.5&name=pear&qty=12&cost=5.75"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postCartInvalidSessionToken() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/cart?name=apple&qty=2&cost=2.5&name=pear&qty=12&cost=5.75"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postCartTestUnsuitableQty1() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/cart?name=apple&qty=-2&cost=2.5&name=pear&qty=12&cost=5.75"))
//                    .POST(HttpRequest.BodyPublishers.ofFile(Path.of("src/main/resources/templates/test.html")))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postCartTestUnsuitableQty2() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/cart?name=apple&qty=2147483648&cost=2.5&name=pear&qty=12&cost=5.75"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(400, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postCartTestUnsuitableQty3() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/cart?name=apple&qty=1&name=pear&cost=2&qty="))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postCartTestUnsuitableQty4() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/cart?name=apple&qty="))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }


    @Test
    public void getNewNameTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/newname"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void getNewNameInvalidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/newname"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postNewNameInvalidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/newname"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postNewNameValidInputTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/newname?name=mango&cost=14.60"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postNewNameInvalidInputTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/newname?name=apple&cost=14.60"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void getDelNameInvalidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/delname"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void getDelNameValidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/delname"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postDelNameValidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/delname"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postDelNameInvalidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/delname"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postDelNameValidDelTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/delname?item=apple&delete=checked"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postDelNameInvalidDelTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/delname?item=mango&delete=checked"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postDelNameDelAllTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue)
                    .uri(new URI("http://localhost:8080/delname" +
                            "?item=apple&delete=checked" +
                            "&item=pear&delete=checked" +
                            "&item=banana&delete=checked" +
                            "&item=orange&delete=checked"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void getUpdateNameValidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue).uri(new URI("http://localhost:8080/updatename"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void getUpdateNameInvalidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/updatename"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postUpdateNameInvalidSessionTokenTest() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol").uri(new URI("http://localhost:8080/updatename"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postUpdateNameValidSessionTokenTestDupsInput() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue)
                    .uri(new URI("http://localhost:8080/updatename" +
                            "?name=apple&cost=3.0" +
                            "&name=apple&cost=3.0"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postUpdateNameUpdateInvalidQtyAndPrice() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue)
                    .uri(new URI("http://localhost:8080/updatename" +
                            "?name=apple&cost=&qty=1" +
                            "&name=mango&cost=3.0&qty=-1"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void postUpdateNameUpdateSingleNameAndPrice() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue)
                    .uri(new URI("http://localhost:8080/updatename" +
                            "?name=apple&cost=5.0&qty=0" +
                            "&name=mango&cost=3.0&qty=1"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(200, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void getLogoutValidSessionId() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue)
                    .uri(new URI("http://localhost:8080/logout"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(302, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void getLogoutInvalidSessionId() {
        try {
            this.login();

            String setCookieHeaderValue = String.format("session=%s", sessionToken);
            HttpRequest req = HttpRequest.newBuilder().headers("Cookie", setCookieHeaderValue + "lol")
                    .uri(new URI("http://localhost:8080/logout"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Status Code: " + resp.statusCode());
            System.out.println("Response Headers: " + resp.headers());
            System.out.println("Request Headers:  " + req.headers());
            System.out.println("Response Body: " + resp.body());

            assertEquals(401, resp.statusCode());

        } catch (URISyntaxException | IOException e) {
            fail(e);
        } catch (InterruptedException e) {
            fail(e);
        }
    }





}






