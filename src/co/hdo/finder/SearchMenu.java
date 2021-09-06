package co.hdo.finder;

import java.awt.Desktop;
import java.net.URI;
import java.sql.SQLException;
import java.util.Scanner;

public class SearchMenu extends DAO 
{

	Scanner scanner = new Scanner(System.in);
	static SearchMenu instance = new SearchMenu();
	public static SearchMenu getInstance()
	{
		return instance;
	}
	
	public void searchTable() //테이블 없을시에 테이블생성.
	{ 
		String sql = "select count(*) from all_tables where table_name = 'SEARCHTABLE'";
		connect();
		try {
			stmt =conn.createStatement();		
			rs =stmt.executeQuery(sql);
			rs.next();
			if(rs.getString(1).equals("0"))
			{
				sql="create table searchtable\r\n" + 
						"(keyword varchar2(100))";
				rs =stmt.executeQuery(sql);
				conn.prepareStatement("commit");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void webSearch()
	{
		try 
		{ 
			String sql = "insert into searchtable(keyword)values(?)";
			psmt=conn.prepareStatement(sql);
			System.out.println("검색어를 입력하세요");
			String search=scanner.next();
			psmt.setString(1, search);
			Desktop.getDesktop().browse(new URI("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+search)); 
			psmt.executeUpdate();
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
		} 
	}
	public void keywordRank()
	{
		try 
		{
			String sql = "select keyword,count(keyword)\r\n" + 
					"from searchtable\r\n" + 
					"group by keyword\r\n" + 
					"order by 2 desc";

			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			while(rs.next())
			{
				System.out.print(rs.getString("keyword"));
				System.out.println("\t"+rs.getString("count(keyword)"));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
	}
}
