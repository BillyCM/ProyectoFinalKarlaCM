import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Post;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;


public class PostTests extends BaseTest {

    private static String resourcePath = "/v1/post";

    private static Integer createdPost = 0;
    private static String titlePost = new String();
    private static String contentPost = new String();

    @BeforeGroups("create_post")
    public void createPost(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath);

        JsonPath jsonPathEvaluator = response.jsonPath();
        createdPost = jsonPathEvaluator.get("id");
        titlePost = testPost.getTitle();
        contentPost = testPost.getContent();
    }

    @Test(groups = "create_post")
    public void Test_Delete_Post_Success(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .delete(resourcePath + "/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString("Post deleted"));
    }

    @Test(groups = "create_post")
    public void Test_Delete_Post_No_Token(){

        Response response = given()
                .spec(RequestSpecs.generateFakeToken())
                .delete(resourcePath + "/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test(groups = "create_post")
    public void Test_Delete_Post_Fail(){

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .delete(resourcePath + "/" + true);

        assertThat(response.statusCode(), not(200));
        assertThat(response.asString(), containsString("Invalid parameter"));
    }

    @Test
    public void Test_Create_Post_Success(){
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath);

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString("Post created"));
    }

    @Test
    public void Test_Create_Post_No_Token(){
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .post(resourcePath);

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test
    public void Test_Create_Post_Fail(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(8)
                .post(resourcePath);

        assertThat(response.statusCode(), not(200));
        assertThat(response.asString(), containsString("Invalid form"));
    }

    @Test
    public void Test_Get_All_Post_Success(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath+"s");

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString("results"));
    }

    @Test
    public void Test_Get_All_Post_No_Token(){
        Response response = given()
                .spec(RequestSpecs.generateFakeToken())
                .get(resourcePath + "s");

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test
    public void Test_Get_All_Post_Fail(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath);

        assertThat(response.statusCode(), not(200));
    }

    @Test(groups = "create_post")
    public void Test_Get_Post_Success(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString(titlePost));
    }

    @Test(groups = "create_post")
    public void Test_Get_Post_Fail(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "//" + createdPost.toString());

        assertThat(response.statusCode(), not(200));
    }

    @Test(groups = "create_post")
    public void Test_Get_Post_No_Token(){
        Response response = given()
                .spec(RequestSpecs.generateFakeToken())
                .get(resourcePath + "/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test(groups = "create_post")
    public void Test_Put_Post_Success(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .put(resourcePath + "/" + createdPost.toString());

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.asString(), containsString("Post updated"));
    }

    @Test(groups = "create_post")
    public void Test_Put_Post_No_Token(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .put(resourcePath + "/" +createdPost.toString());

        assertThat(response.statusCode(), equalTo(401));
        assertThat(response.asString(), containsString("Please login first"));
    }

    @Test(groups = "create_post")
    public void Test_Put_Post_Fail(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(8)
                .put(resourcePath + "/" +createdPost.toString());

        assertThat(response.statusCode(), not(200));
        assertThat(response.asString(), containsString("Invalid form"));
    }
}
