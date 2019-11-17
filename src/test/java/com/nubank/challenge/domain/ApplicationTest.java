package com.nubank.challenge.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {Application.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationTest {

    @Autowired
    private Application application;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    private void setup(){
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void shouldGenerateAccount() {
        //GIVEN

        final String accountCreation = "{ \"account\": { \"activeCard\": true, \"availableLimit\": 100 } }";
        final String expectedResult = "{\"account\":{\"activeCard\":true,\"availableLimit\":100},"
                                      + "\"violations\":[]}\n";
        //WHEN
        application.process(accountCreation);
        //THEN
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void shouldDecrementAvailableAmountAccount() {
        //GIVEN

        final String accountCreation = "{\"account\": { \"activeCard\": true, \"availableLimit\": 100 } }";

        final String transaction = "{\"transaction\": { \"merchant\": \"Burger King\", "
                                   + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String expectedResult = "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":100},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":80},\"violations\":[]}\n";
        //WHEN
        application.process(accountCreation);
        application.process(transaction);
        //THEN
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void cardNotActiveRuleShouldNotPass() {
        //GIVEN

        final String accountCreation = "{\"account\": { \"activeCard\": false, "
                                       + "\"availableLimit\": 100 } }";

        final String transaction = "{\"transaction\": { \"merchant\": \"Burger King\", "
                                   + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String expectedResult = "{\"account\":{\"activeCard\":false,"
                                      + "\"availableLimit\":100},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":false,"
                                      + "\"availableLimit\":100},\"violations\":[\"card-not-active\"]}\n";
        //WHEN
        application.process(accountCreation);
        application.process(transaction);
        //THEN
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void insufficientLimitRuleShouldNotPass() {
        //GIVEN

        final String accountCreation = "{\"account\": { \"activeCard\": true, "
                                       + "\"availableLimit\": 50 } }";

        final String transaction = "{\"transaction\": { \"merchant\": \"Burger King\", "
                                   + "\"amount\": 60, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String expectedResult = "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":50},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":50},"
                                      + "\"violations\":[\"insufficient-limit\"]}\n";
        //WHEN
        application.process(accountCreation);
        application.process(transaction);
        //THEN
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void doubleTransactionRuleShouldNotPass() {
        //GIVEN

        final String accountCreation = "{\"account\": { \"activeCard\": true, "
                                       + "\"availableLimit\": 100 } }";

        final String transaction1 = "{\"transaction\": { \"merchant\": \"Burger King\", "
                                   + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String transaction2 = "{\"transaction\": { \"merchant\": \"Burger King\", "
                                    + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String transaction3 = "{\"transaction\": { \"merchant\": \"Burger King\", "
                                    + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String expectedResult = "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":100},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":80},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":60},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":60},"
                                      + "\"violations\":[\"doubled-transaction\"]}\n";
        //WHEN
        application.process(accountCreation);
        application.process(transaction1);
        application.process(transaction2);
        application.process(transaction3);

        //THEN
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    public void highFrequencySmallIntervalRuleShouldNotPass() {
        //GIVEN

        final String accountCreation = "{\"account\": { \"activeCard\": true, "
                                       + "\"availableLimit\": 100 } }";

        final String transaction1 = "{\"transaction\": { \"merchant\": \"Burger King\", "
                                    + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String transaction2 = "{\"transaction\": { \"merchant\": \"McDonald's\", "
                                    + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String transaction3 = "{\"transaction\": { \"merchant\": \"Mostaza\", "
                                    + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String transaction4 = "{\"transaction\": { \"merchant\": \"Wendys\", "
                                    + "\"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";

        final String expectedResult = "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":100},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":80},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":60},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":40},\"violations\":[]}\n"

                                      + "{\"account\":{\"activeCard\":true,"
                                      + "\"availableLimit\":40},"
                                      + "\"violations\":[\"high-frequency-small-interval\"]}\n";
        //WHEN
        application.process(accountCreation);
        application.process(transaction1);
        application.process(transaction2);
        application.process(transaction3);
        application.process(transaction4);

        //THEN
        assertEquals(expectedResult, outContent.toString());
    }
}
