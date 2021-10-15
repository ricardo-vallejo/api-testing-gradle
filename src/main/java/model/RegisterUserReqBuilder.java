package model;

public class RegisterUserReqBuilder {

    private final RegisterUserRequest userRequest;

    private RegisterUserReqBuilder() {
        userRequest = new RegisterUserRequest();
    }

    public static RegisterUserReqBuilder registerUser() {
        return new RegisterUserReqBuilder();
    }

    public RegisterUserReqBuilder withPassword(String password) {
        this.userRequest.setPassword(password);
        return this;
    }

    public RegisterUserReqBuilder withEmail(String email) {
        this.userRequest.setEmail(email);
        return this;
    }

    public RegisterUserRequest build(){
        return userRequest;
    }
}
