package guru.sfg.brewery.web.controllers;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncodingTests {

	static final String PASSWORD ="password";
	
	@Test
	void hashingExample() {
		//traditional algorithms like MD5 will always generate same hash every time it is used on same string
		System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		
		//salt allow us to generate different hash, but with same salt value same hash got calculated
		String salted=PASSWORD+"MySaltValue";
		System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
		
	}
	
	@Test
	void testNoOp() {
		//NoOp will not generate Hash for password, it generate plain text only
		// Used for compatibility with legacy system
		PasswordEncoder noOp=NoOpPasswordEncoder.getInstance();
		System.out.println(noOp.encode(PASSWORD));
	}
	
	
	@Test
	void testLDAP() {
		PasswordEncoder ldap=new LdapShaPasswordEncoder();
		System.out.println(ldap.encode(PASSWORD));
		System.out.println(ldap.encode(PASSWORD));
		String encodedPassword=ldap.encode(PASSWORD);
		assertTrue(ldap.matches(PASSWORD, encodedPassword));
	}
	
	@Test
	void testLSHA256() {
		PasswordEncoder sha256=new StandardPasswordEncoder();
		System.out.println(sha256.encode(PASSWORD));
		System.out.println(sha256.encode(PASSWORD));
		String encodedPassword=sha256.encode(PASSWORD);
		assertTrue(sha256.matches(PASSWORD, encodedPassword));
	}
	
	@Test
	void testBcrypt() {
		PasswordEncoder bcrypt = new BCryptPasswordEncoder();
		System.out.println("Bcrypt Password Encoder");
		String bcryptPassword= bcrypt.encode(PASSWORD);
		System.out.println(bcryptPassword);
		String bcryptPasswordSecurity= bcrypt.encode("security");
		System.out.println("Security: "+bcryptPasswordSecurity);
	
		assertTrue(bcrypt.matches(bcryptPassword, PASSWORD));
	}	
	
	@Test
	void testBcrypt15() {
		PasswordEncoder bcrypt = new BCryptPasswordEncoder(15);
		System.out.println("Bcrypt Password Encoder with Strength15");
		String bcryptPassword= bcrypt.encode("tiger");
		System.out.println(bcryptPassword);
		assertTrue(bcrypt.matches(bcryptPassword, "tiger"));
	}
}
