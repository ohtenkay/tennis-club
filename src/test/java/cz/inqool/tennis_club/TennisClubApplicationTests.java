package cz.inqool.tennis_club;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class TennisClubApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
    }

    @Test
    public void testControllerIsInContext() {
        boolean isPresent = applicationContext.containsBean("reservationController");
        assertTrue(isPresent, "The ReservationController is not present in the application context");
    }

}
