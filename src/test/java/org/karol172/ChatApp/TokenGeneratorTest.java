package org.karol172.ChatApp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.karol172.ChatApp.component.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TokenGeneratorTest {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Test
    public void tokenGeneratorShouldReturnStringWithLength32() {
        Assert.assertEquals(tokenGenerator.generateToken().length(), 32);
    }

}
