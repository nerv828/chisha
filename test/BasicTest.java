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
	@Test 
	public void postComment(){
			User bob=new User("bob@gmail.com","secret","Bob").save();
			Post bobPost=new Post(bob,"My first Post","Hello world").save();
			new Comment(bobPost,"Jeff","Nice post").save();
			new Comment(bobPost,"Tom","I knew that!").save();
			List<Comment> bobPostComments=Comment.find("byPost",bobPost).fetch();
			assertEquals(2,bobPostComments.size());
			Comment firstComment=bobPostComments.get(0);
			assertNotNull(firstComment);
			assertEquals("Jeff",firstComment.author);
			assertEquals("Nice post",firstComment.content);
			assertNotNull(firstComment.postedAt);
		    Comment secondComment=bobPostComments.get(1);
            assertNotNull(secondComment);
			assertEquals(secondComment.post,bobPost);
			assertEquals("Tom",secondComment.author);
			assertEquals("I knew that!",secondComment.content);
			assertNotNull(secondComment.postedAt);


	}

	@Test
	public void  useTheCommentsRelation(){

			User bob=new User("bob@gmail.com","secret","Bob").save();
			Post bobPost=new Post(bob,"My first Post","Hello world").save();
			bobPost.addComment("Jeff","Nice post");
			bobPost.addComment("Tom","I knew that!");
			assertEquals(1,User.count());
			assertEquals(1,Post.count());
			bobPost=Post.find("byAuthor",bob).first();
			assertNotNull(bobPost);
			assertEquals(2,bobPost.comments.size());
			assertEquals("Jeff",bobPost.comments.get(0).author);
			assertEquals("Nice post",bobPost.comments.get(0).content);
			bobPost.delete();
			assertEquals(1,User.count());
			assertEquals(0,Post.count());
			assertEquals(0,Comment.count());
	}

	@Test
	public void fullTest(){
		Fixtures.loadModels("data1.yml");

		assertEquals(2,User.count());
		assertEquals(3,Post.count());
		assertEquals(3,Comment.count());
		assertNotNull(User.connect("bob@gmail.com","secret"));
		assertNotNull(User.connect("jeff@gmail.com","secret"));
		assertNull(User.connect("bob@gmail.com","badsecret"));
		assertNull(User.connect("tom@gmail.com","secret"));

		List<Post> bobPost=Post.find("author.email","bob@gmail.com").fetch();
		assertEquals(2,bobPost.size());
		List<Comment> bobPostComments=Comment.find("post.author.email","bob@gmail.com").fetch();
		assertEquals(3,bobPostComments.size());

		Post frontPost=Post.find("order by postedAt desc").first();
		assertEquals("About the model layer",frontPost.title);
		assertEquals(2,frontPost.comments.size());
		frontPost.addComment("Jim","Hello guys");
		assertEquals(3,frontPost.comments.size());
		assertEquals(4,Comment.count());
		assertEquals(1,1);
	}




}	
