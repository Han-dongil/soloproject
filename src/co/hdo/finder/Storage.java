package co.hdo.finder;

import java.sql.SQLException;

import java.util.Scanner;

public class Storage extends DAO
{
	String year,date,birth;
	Scanner sr=new Scanner(System.in);
	static Storage instance = new Storage();
	public static Storage getInstance()
	{
		return instance;
	}
	public void searchDB() //테이블 없을시에 테이블생성.
	{ 
		String sql = "select count(*) from all_tables where table_name = 'USERPROFILE'";
		connect();
		try {
			stmt =conn.createStatement();		
			rs =stmt.executeQuery(sql);
			rs.next();
			if(rs.getString(1).equals("0"))
			{
				sql="create table userprofile (\r\n" + 
						"username varchar2(10) primary key not null,\r\n" + 
						"gender varchar2(6),\r\n" + 
						"age number,\r\n" + 
						"location varchar2(20),\r\n" + 
						"birth varchar2(10))";
				rs =stmt.executeQuery(sql);
				sql="create table siteinfo(\r\n" + 
						"sitename varchar2(30),\r\n" + 
						"siteadress varchar2(200),\r\n" + 
						"username varchar2(10) references userprofile(username),\r\n" + 
						"userid varchar2(20),\r\n" + 
						"pw varchar2(50),\r\n" + 
						"memo varchar2(200)\r\n" + 
						")";
				rs =stmt.executeQuery(sql);

			}
			sql="select to_char(sysdate,'yy') from dual";
			rs =stmt.executeQuery(sql);
			rs.next();
			year=rs.getString(1);
			
			sql="select to_char(sysdate,'mm/dd') from dual";
			rs =stmt.executeQuery(sql);
			rs.next();
			date=rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void inputUser()//사용자등록
	{
		String sql ="insert into USERPROFILE (username,gender,age,location,birth) \r\n" + 
				"values(?,?,?,?,?)";
		
		try {
			psmt= conn.prepareStatement(sql);
			System.out.println("이름을 입력하세요 > ");
			String username = sr.next();
			psmt.setString(1,username);
			System.out.println("성별을 입력하세요 > ");
			String gender = sr.next();
			if(gender.equals("남")||gender.equals("남성"))
			gender="남자";
			else if(gender.equals("여")||gender.equals("여성"))
			gender="여자";

			psmt.setString(2, gender);
			System.out.println("생년월일을 입력하세요 ex)yy/mm/dd > ");
			String a = sr.next();
			String birthyear=(a.split("/"))[0];
			int ybirth=Integer.parseInt(birthyear);
			int idate=Integer.parseInt(year);
			if(ybirth>idate)
			{
				ybirth=idate+101-ybirth;
			}
			else
			{
				ybirth=idate+1-ybirth;
			}
			int age=ybirth;
			psmt.setInt(3, age);
			System.out.println("지역을 입력하세요 > ");
			String location =sr.next();
			psmt.setString(4, location);
			psmt.setString(5, (a.split("/"))[1]+"/"+(a.split("/"))[2]);
			psmt.executeUpdate();
			conn.prepareStatement("commit");
			System.out.println("사용자 등록에 성공하였습니다");
			birth = (a.split("/"))[1]+"/"+(a.split("/"))[2];
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public boolean login(String user)  //로그인
	{
		String sql ="select * from userprofile where username=? "; //scanner 입력된값 검색 
		try {
				psmt= conn.prepareStatement(sql);
				psmt.setString(1,user);	
				rs=psmt.executeQuery();
				rs.next();

			if(rs.getString("username").equals(user))
			{
				System.out.println("접속되었습니다\n사용자정보 >");
				System.out.println("사용자 : "+rs.getString("username")+"\t나이 : "+rs.getString("age")+"\t지역 : "+rs.getString("location"));
				if(rs.getString("birth").equals(date))
				{
					System.out.println(user+"님 생일을 축하합니다");
				}
				return true;
			}
			else
			{
				System.out.println("사용자명이 틀렸습니다");
			}			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	public void inputSite(String user) //사이트 정보등록
	{
		String sql ="insert into SITEINFO (sitename,siteadress,username,userid,pw,memo) \r\n" + 
				"values(?,?,?,?,?,?)";
		try 
		{
			psmt= conn.prepareStatement(sql);
			System.out.println("사이트이름을 입력하세요 > ");
			String siteName = sr.next();
			psmt.setString(1,siteName);		
			System.out.println("사이트주소를 입력하세요 > ");
			String siteAdress = sr.next();
			psmt.setString(2,siteAdress);	
			psmt.setString(3,user);			
			System.out.println("id를 입력하세요 > ");
			String id =sr.next();
			psmt.setString(4,id);		
			System.out.println("pw를 입력하세요 > ");
			String pw =sr.next();
			psmt.setString(5,pw);		
			System.out.println("memo를 입력하세요 > ");
			String memo =sr.next();
			psmt.setString(6,memo);	
			psmt.executeUpdate();
			conn.prepareStatement("commit");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void findSite(String user) //등록한 전체사이트 조회
	{
		String sql ="select * from siteinfo where username = ?"; //scanner 입력된값 검색 
		try {
				psmt= conn.prepareStatement(sql);
				psmt.setString(1,user);	
				rs=psmt.executeQuery();
				while(rs.next())
				{
					System.out.print("사이트명 : "+rs.getString("sitename")+" ");
					System.out.print("\t"+"주소 : "+rs.getString("siteadress")+" ");
					System.out.print("\t"+"id : "+rs.getString("userid")+" ");
					System.out.print("\t"+"pw : "+rs.getString("pw")+" ");
					System.out.println("\t"+"메모 : "+rs.getString("memo")+" ");
				}
			} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void update(String user) //사이트정보 수정
	{
		System.out.println("수정할 사이트명을 입력하세요");
		String rename=sr.next();
		String sql ="update SITEINFO set sitename=?,siteadress=?,username=?,userid=?,pw=?,memo=? \r\n" + 
				"where username=? and sitename=?";
		try 
		{
			psmt= conn.prepareStatement(sql);

			psmt.setString(1,rename);		
			System.out.println("사이트주소를 입력하세요 > ");
			String siteAdress = sr.next();
			psmt.setString(2,siteAdress);	
			psmt.setString(3,user);			
			System.out.println("id를 입력하세요 > ");
			String id =sr.next();
			psmt.setString(4,id);		
			System.out.println("pw를 입력하세요 > ");
			String pw =sr.next();
			psmt.setString(5,pw);		
			System.out.println("memo를 입력하세요 > ");
			String memo =sr.next();
			psmt.setString(6,memo);	
			psmt.setString(7,user);	
			psmt.setString(8,rename);
			psmt.executeUpdate();
			conn.prepareStatement("commit");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void delete(String user)//삭제할 사이트
	{
		System.out.println("삭제할 사이트를 입력하세요");
		String delSite=sr.next();

		String sql ="delete from siteinfo \r\n" + 
				"where username=? and sitename=?";
		try 
		{
			psmt= conn.prepareStatement(sql);

			psmt.setString(1,user);		
			psmt.setString(2,delSite);	
			int r=psmt.executeUpdate();
			conn.prepareStatement("commit");
			System.out.println(r+"건 삭제됨.");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void siteSearch()//사이트별 이용자수조회
	{
		String sql = "select b.sitename , count(b.sitename)\r\n" + 
				"from userprofile a join siteinfo b\r\n" + 
				"on(a.username = b.username)\r\n" + 
				"group by sitename order by 2 desc";
		try {
				stmt =conn.createStatement();
				rs =stmt.executeQuery(sql);
				while(rs.next())
				{
					System.out.print("사이트명 : "+rs.getString("sitename"));
					System.out.println("\t이용자수 : "+rs.getString("count(b.sitename)"));
				}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
	}
	public void genderSearch()//성별 사이트 이용자수 조회
	{
		String sql = "select b.sitename , a.gender , count(a.gender)\r\n" + 
				"from  userprofile a join siteinfo b\r\n" + 
				"    on (a.username = b.username)\r\n" + 
				"where a.gender = ?" + 
				"group by sitename , a.gender\r\n" + 
				"order by 3 desc";		
			try 
			{
				psmt =conn.prepareStatement(sql);
				System.out.println("성별을 입력하세요 > ");
				String s=sr.nextLine();
				psmt.setString(1, s);
				System.out.println(s+"들이 많이 등록한 사이트!");
				rs =psmt.executeQuery();
				while(rs.next())
				{
					System.out.print(rs.getString("sitename"));
					System.out.println("\t이용자수"+rs.getString("count(a.gender)"));
				}
				
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
	}
	public void userAge() // 연령별 사이트 이용자수 조회
	{
		String sql = "select sitename,agegroup , count(agegroup) \r\n" + 
				"				from(select b.sitename ,  \r\n" + 
				"				       (case when a.age <20 then '10대'\r\n" + 
				"				            when a.age <30 then '20대'\r\n" + 
				"				            when a.age <40 then '30대'\r\n" + 
				"				            when a.age <50 then '40대'\r\n" + 
				"				            when a.age <60 then '50대' else '60대' end)as agegroup \r\n" + 
				"				from  userprofile a join siteinfo b \r\n" + 
				"				    on (a.username = b.username)) \r\n" + 
				"where agegroup=?" + 
				"group by sitename,agegroup\r\n" + 
				"order by 3 desc";
		try 
		{
			psmt = conn.prepareStatement(sql);
			System.out.println("검색할 연령대를 입력하세요 > ");
			String s=sr.nextLine();
			psmt.setString(1, s);
			rs=psmt.executeQuery();
			while(rs.next())
			{
				System.out.print(rs.getString("sitename"));
				System.out.print("\t"+rs.getString("agegroup"));
				System.out.println("\t"+rs.getString("count(agegroup)"));
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	public void locSearch() //지역별 사이트이름 조회
	{
		String sql = " select b.sitename , a.location , count(a.location) \r\n" + 
				"				from  userprofile a join siteinfo b\r\n" + 
				"				    on (a.username = b.username)\r\n" + 
				"				group by sitename , a.location\r\n" + 
				"				order by 2,3 desc";
		try 
		{
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			while(rs.next())
			{
				System.out.print("\t"+rs.getString("location"));
				System.out.print("\t"+rs.getString("sitename"));
				System.out.println("\t"+rs.getString("count(a.location)"));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
	}




}
