public class RegisterUserResponse{
	private int id;
	private String token;


	public RegisterUserResponse() {}

	public RegisterUserResponse(int id, String token) {
		this.id = id;
		this.token = token;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}
