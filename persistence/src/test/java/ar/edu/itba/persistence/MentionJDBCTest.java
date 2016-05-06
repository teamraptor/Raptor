package ar.edu.itba.persistence;

import ar.edu.itba.paw.models.Tweet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.MentionJDBC;
import ar.edu.itba.paw.persistence.TweetJDBC;
import ar.edu.itba.paw.persistence.UserJDBC;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class MentionJDBCTest {

    private static final String MESSAGE = "hola soy un tweet";
    private static final String USERNAME = "@raptorTest", PASSWORD = "raptor",
            EMAIL = "raptor@gmail.com ", FIRSTNAME = "rap", LASTNAME = "tor";
    @Autowired
    private DataSource ds;
    @Autowired
    private UserJDBC userJDBC;
    @Autowired
    private TweetJDBC tweetJDBC;
    @Autowired
    private MentionJDBC mentionJDBC;
    private JdbcTemplate jdbcTemplate;
    private User user;
    private Tweet tweet;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "mentions");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tweets");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        user = userJDBC.create(USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME);
        tweet = tweetJDBC.create(MESSAGE, user);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createTest() {
        mentionJDBC.create(user.getId(), tweet.getId());

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "mentions"));

    }

}
