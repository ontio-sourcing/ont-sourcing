package com.github.ontio.sourcing;

import ch.qos.logback.classic.Logger;
import com.github.ontio.sourcing.service.oauth.JWTService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OAuthTests {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(OAuthTests.class);

    @Autowired private JWTService oauthService;

    String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg";

    @Test
    public void example01() {

        //
        try {
            oauthService.verify(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
