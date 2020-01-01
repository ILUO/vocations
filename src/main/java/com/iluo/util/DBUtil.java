package com.iluo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@Component
public class DBUtil {

	private static Properties props;

	@Value("${spring.datasource.url}")
    private  String url;
	@Value("${spring.datasource.username}")
	private  String username;
	@Value("${spring.datasource.password}")
	private  String password;
	@Value("${spring.datasource.driver-class-name}")
	private  String driver;

//	static {
//		try {
//			InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.properties");
//			props = new Properties();
//			props.load(in);
//			in.close();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public Connection getConn() throws Exception{
//		String url = props.getProperty("spring.datasource.url");
//		String username = props.getProperty("spring.datasource.username");
//		String password = props.getProperty("spring.datasource.password");
//		String driver = props.getProperty("spring.datasource.driver-class-name");
		System.out.println(url);
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/miaosha?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=UTC",
				"root", "123456");
	}
}
