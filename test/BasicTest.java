import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	@Before
	public void setup(){
			Fixtures.deleteDatabase();
	}
    @Test
    public void createAndRetrieveUser() {
    		new User("bob@gmail.com","secret","Bob").save();
    		User bob=User.find("byEmail","bob@gmail.com").first();
    		assertNotNull(bob);
    		assertEquals("Bob",bob.fullname);
    }
    @Test	
    public  void tryConnectAsUser(){
     		new  User("bob@gmail.com","secret","Bob").save();
     		assertNotNull(User.connect("bob@gmail.com","secret"));
     		assertNull(User.connect("bob@gmail.com","badsecret"));
     		assertNull(User.connect("toy@gmail.com","secret"));
    }
	@Test
	public void createPost(){
			User bob=new User("bob@gmail.com","secret","Bob").save();
			new Post(bob,"My first Post","Hello world!!!").save();
			assertEquals(1,Post.count());
			List<Post> bobPost=Post.find("byAuthor",bob).fetch();
			assertEquals(1,bobPost.size());
			Post firstPost=bobPost.get(0);
			assertNotNull(firstPost);
			assertEquals(bob,firstPost.author);
			assertEquals("My first Post",firstPost.title);
			assertEquals("Hello world!!!",firstPost.content);
			assertNotNull(firstPost.postedAt);
	}
}	
