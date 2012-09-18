package models;
import java.utils.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
@Table(name="TAB_USER")
public class User extends Model{

	public String email;
	public String password;
	public String fullname;
	public String isAdmin;
	public User(String email,String password,String fullname){
		this.email=email;
		this.password=password;
		this.fullname=fullname;
	} 
	public static User connect(String email,String password){
			return find("byEmailAndPassword",email,password).first();
	}

}
