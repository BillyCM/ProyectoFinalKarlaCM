import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import model.Post;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import specifications.RequestSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class CommentTests extends BaseTest {

    private static String resourcePathPost = "/v1/post";
    private static String resourcePathComment = "/v1/comment";
    private static Integer createdPost = 0;
    private static Integer createdComment = 0;
    private static String comment_name = new String();
    private static String comment_content = new String();


    @BeforeGroups("create_post")
    public void createPost(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePathPost);

        JsonPath jsonPathEvaluator = response.jsonPath();
        createdPost = jsonPathEvaluator.get("id");
    }

    @BeforeGroups("create_comment")
    public void createComment(){

        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("testuser", "testpass")
                .body(testComment)
                .post(resourcePathComment + "/" + createdPost.toString());

        JsonPath jsonPathEvaluator = response.jsonPath();
        createdComment = jsonPathEvaluator.get("id");
        comment_name = testComment.getName();
        comment_content = testComment.getComment();
    }

    @Test(groups = {"create_post","create_comment"})
    public void Test_Delete_Comment_Success(){
        Response response = given()
                .auth().basic("testuser", "testpass")
                .delete(resourcePathComment + "/" + createdPost.toString() + "/" + createdComment.toString());

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString("Comment deleted"));
    }

    @Test(groups = {"create_post","create_comment"})
    public void Test_Delete_Comment_No_Auth(){
        Response response = given()
                .auth().basic("wrong", "creds")
                .delete(resourcePathComment + "/" + createdPost.toString() + "/" + createdComment.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test(groups = {"create_post","create_comment"})
    public void Test_Delete_Comment_Fail(){

        Response response = given()
                .auth().basic("testuser", "testpass")
                .delete(resourcePathComment + "/" + createdPost.toString());

        assertThat(response.statusCode(), not(200));
    }

    @Test(groups = "create_post")
    public  void Test_Create_Comment_Success(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("testuser", "testpass")
                .body(comment)
                .post(resourcePathComment + "/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(200));
    }

    @Test(groups = "create_post")
    public  void Test_Create_Comment_No_Auth(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("wrong", "creds")
                .body(comment)
                .post(resourcePathComment + "/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test(groups = "create_post")
    public  void Test_Create_Comment_Fail(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("testuser", "testpass")
                .post(resourcePathComment + "/" + createdPost.toString());

        assertThat(response.statusCode(), not(200));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Get_Comment_Success(){

        Response response = given()
                .auth().basic("testuser", "testpass")
                .get(resourcePathComment + "/" + createdPost.toString() + "/" + createdComment.toString());

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString(comment_content));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Get_Comment_No_Auth(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("wrong", "creds")
                .get(resourcePathComment + "/" + createdPost.toString() + "/" + createdComment.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Get_All_Comments_Fail(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("testuser", "testpass")
                .get(resourcePathComment + "//" + createdPost.toString() + "//" + createdComment.toString());

        assertThat(response.statusCode(), not(200));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Get_All_Comments_Success(){

        Response response = given()
                .auth().basic("testuser", "testpass")
                .get(resourcePathComment + "s/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString(comment_content));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Get_All_Comments_No_Auth(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("wrong", "creds")
                .get(resourcePathComment + "s/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Get_Comment_Fail(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("testuser", "testpass")
                .get(resourcePathComment + "/" + createdPost.toString());

        assertThat(response.statusCode(), not(200));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Put_Comment_Success(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("testuser", "testpass")
                .body(comment)
                .put(resourcePathComment + "/" + createdPost.toString() + "/" + createdComment.toString());

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString("Comment updated"));
    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Put_Comment_No_Auth(){
        Comment comment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .auth().basic("wrong", "creds")
                .body(comment)
                .put(resourcePathComment + "/" + createdPost.toString() + "/" + createdComment.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));

    }

    @Test(groups = {"create_post", "create_comment"})
    public  void Test_Put_Comment_Fail(){
        Response response = given()
                .auth().basic("testuser", "testpass")
                .body(8)
                .put(resourcePathComment + "/" + createdPost.toString() + "/" + createdComment.toString());

        assertThat(response.statusCode(), not(200));
        assertThat(response.asString(), containsString("Invalid form"));
    }
}
