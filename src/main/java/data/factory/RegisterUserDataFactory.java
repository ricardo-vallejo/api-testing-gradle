package data.factory;

import com.github.javafaker.Faker;
import model.RegisterUserRequest;

import static model.RegisterUserReqBuilder.registerUser;

public class RegisterUserDataFactory {

    private static final Faker FAKER = new Faker();
    private static final String DEFAULT_EMAIL = "eve.holt@reqres.in";
    private static final String DEFAULT_PASS = "pistol";

    public static RegisterUserRequest missingAllInformation() {
        return registerUser().withEmail("").withPassword("").build();
    }

    public static RegisterUserRequest nullInformation(){
        return registerUser().withEmail(null).withPassword(null).build();
    }

    public static RegisterUserRequest validDefaultUser(){
        return registerUser().withEmail(DEFAULT_EMAIL).withPassword(DEFAULT_PASS).build();
    }

    public static RegisterUserRequest validUserDiffEmail(){
        return registerUser().withEmail(FAKER.internet().emailAddress()).withPassword(DEFAULT_PASS).build();
    }

    public static RegisterUserRequest validUserDiffPass(){
        return registerUser().withEmail(DEFAULT_EMAIL).withPassword(FAKER.internet().password()).build();
    }
}
