package tests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Users.User;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static io.restassured.RestAssured.*;
import org.json.JSONObject;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class UsersTests {



	@BeforeMethod
	public void setup() {
		baseURI = "https://reqres.in/api/users";
	}

	@Test
	public void  createUser(){
		JSONObject requestParams = new JSONObject();
		requestParams.put("name", "Moath");
		requestParams.put("job", "Engineer");

		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.body(requestParams.toString())
				.when()
				.post("/")
				.then().statusCode(201)
				.extract().response();

		System.out.println(response.toString());

		assertEquals("Moath", response.jsonPath().getString("name"));
		assertEquals("Engineer", response.jsonPath().getString("job"));
	
	}

	@Test
	public void verifyUserDataWithID() {

		Response response = given().contentType(ContentType.JSON).get("/7").then().statusCode(200).body("data.id", equalTo(7)).extract().response();

		User user = new User(response.path("data.id").toString() , response.path("data.first_name").toString(),response.path("data.last_name").toString(),response.path("data.email").toString());

		System.out.println(user.toString());

	}
	

	@Test
	public void getNonExistingUserID() {

		Response response = given().contentType(ContentType.JSON).get("/205");
		System.out.println("Response of wrong ID: "+response.then().extract().response().asString());

		assertEquals(404, response.statusCode());

	}
}
