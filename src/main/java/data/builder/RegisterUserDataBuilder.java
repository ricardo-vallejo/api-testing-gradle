package data.builder;

import com.github.javafaker.Faker;
import data.factory.RegisterUserDataFactory;
import model.RegisterUserRequest;

public class RegisterUserDataBuilder {

    private static final Faker FAKER = new Faker();
    private RegisterUserRequest userRequest;

    private void createUserDefault() {
        userRequest = new RegisterUserRequest();
        this.userRequest = RegisterUserDataFactory.validDefaultUser();
    }

    private RegisterUserDataBuilder() {
        createUserDefault();
    }

    public static RegisterUserDataBuilder registerUserDataBuilder(){
        return new RegisterUserDataBuilder();
    }

    public RegisterUserDataBuilder withPassword(String password) {
        this.userRequest.setPassword(password);
        return this;
    }

    public RegisterUserDataBuilder withEmail(String email) {
        this.userRequest.setEmail(email);
        return this;
    }

    public RegisterUserDataBuilder withEmptyEmail() {
        this.userRequest.setEmail("");
        return this;
    }

    public RegisterUserDataBuilder withEmptyPassword() {
        this.userRequest.setPassword("");
        return this;
    }

    public RegisterUserDataBuilder withWrongEmail() {
        this.userRequest.setEmail(FAKER.internet().emailAddress());
        return this;
    }

    public RegisterUserRequest build(){
        return userRequest;
    }

}
