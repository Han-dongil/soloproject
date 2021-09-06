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
	public void searchDB() //���̺� �����ÿ� ���̺����.
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
	public void inputUser()//����ڵ��
	{
		String sql ="insert into USERPROFILE (username,gender,age,location,birth) \r\n" + 
				"values(?,?,?,?,?)";
		
		try {
			psmt= conn.prepareStatement(sql);
			System.out.println("�̸��� �Է��ϼ��� > ");
			String username = sr.next();
			psmt.setString(1,username);
			System.out.println("������ �Է��ϼ��� > ");
			String gender = sr.next();
			if(gender.equals("��")||gender.equals("����"))
			gender="����";
			else if(gender.equals("��")||gender.equals("����"))
			gender="����";

			psmt.setString(2, gender);
			System.out.println("��������� �Է��ϼ��� ex)yy/mm/dd > ");
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
			System.out.println("������ �Է��ϼ��� > ");
			String location =sr.next();
			psmt.setString(4, location);
			psmt.setString(5, (a.split("/"))[1]+"/"+(a.split("/"))[2]);
			psmt.executeUpdate();
			conn.prepareStatement("commit");
			System.out.println("����� ��Ͽ� �����Ͽ����ϴ�");
			birth = (a.split("/"))[1]+"/"+(a.split("/"))[2];
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public boolean login(String user)  //�α���
	{
		String sql ="select * from userprofile where username=? "; //scanner �ԷµȰ� �˻� 
		try {
				psmt= conn.prepareStatement(sql);
				psmt.setString(1,user);	
				rs=psmt.executeQuery();
				rs.next();

			if(rs.getString("username").equals(user))
			{
				System.out.println("���ӵǾ����ϴ�\n��������� >");
				System.out.println("����� : "+rs.getString("username")+"\t���� : "+rs.getString("age")+"\t���� : "+rs.getString("location"));
				if(rs.getString("birth").equals(date))
				{
					System.out.println(user+"�� ������ �����մϴ�");
				}
				return true;
			}
			else
			{
				System.out.println("����ڸ��� Ʋ�Ƚ��ϴ�");
			}			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	public void inputSite(String user) //����Ʈ �������
	{
		String sql ="insert into SITEINFO (sitename,siteadress,username,userid,pw,memo) \r\n" + 
				"values(?,?,?,?,?,?)";
		try 
		{
			psmt= conn.prepareStatement(sql);
			System.out.println("����Ʈ�̸��� �Է��ϼ��� > ");
			String siteName = sr.next();
			psmt.setString(1,siteName);		
			System.out.println("����Ʈ�ּҸ� �Է��ϼ��� > ");
			String siteAdress = sr.next();
			psmt.setString(2,siteAdress);	
			psmt.setString(3,user);			
			System.out.println("id�� �Է��ϼ��� > ");
			String id =sr.next();
			psmt.setString(4,id);		
			System.out.println("pw�� �Է��ϼ��� > ");
			String pw =sr.next();
			psmt.setString(5,pw);		
			System.out.println("memo�� �Է��ϼ��� > ");
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
	public void findSite(String user) //����� ��ü����Ʈ ��ȸ
	{
		String sql ="select * from siteinfo where username = ?"; //scanner �ԷµȰ� �˻� 
		try {
				psmt= conn.prepareStatement(sql);
				psmt.setString(1,user);	
				rs=psmt.executeQuery();
				while(rs.next())
				{
					System.out.print("����Ʈ�� : "+rs.getString("sitename")+" ");
					System.out.print("\t"+"�ּ� : "+rs.getString("siteadress")+" ");
					System.out.print("\t"+"id : "+rs.getString("userid")+" ");
					System.out.print("\t"+"pw : "+rs.getString("pw")+" ");
					System.out.println("\t"+"�޸� : "+rs.getString("memo")+" ");
				}
			} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void update(String user) //����Ʈ���� ����
	{
		System.out.println("������ ����Ʈ���� �Է��ϼ���");
		String rename=sr.next();
		String sql ="update SITEINFO set sitename=?,siteadress=?,username=?,userid=?,pw=?,memo=? \r\n" + 
				"where username=? and sitename=?";
		try 
		{
			psmt= conn.prepareStatement(sql);

			psmt.setString(1,rename);		
			System.out.println("����Ʈ�ּҸ� �Է��ϼ��� > ");
			String siteAdress = sr.next();
			psmt.setString(2,siteAdress);	
			psmt.setString(3,user);			
			System.out.println("id�� �Է��ϼ��� > ");
			String id =sr.next();
			psmt.setString(4,id);		
			System.out.println("pw�� �Է��ϼ��� > ");
			String pw =sr.next();
			psmt.setString(5,pw);		
			System.out.println("memo�� �Է��ϼ��� > ");
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
	public void delete(String user)//������ ����Ʈ
	{
		System.out.println("������ ����Ʈ�� �Է��ϼ���");
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
			System.out.println(r+"�� ������.");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void siteSearch()//����Ʈ�� �̿��ڼ���ȸ
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
					System.out.print("����Ʈ�� : "+rs.getString("sitename"));
					System.out.println("\t�̿��ڼ� : "+rs.getString("count(b.sitename)"));
				}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
	}
	public void genderSearch()//���� ����Ʈ �̿��ڼ� ��ȸ
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
				System.out.println("������ �Է��ϼ��� > ");
				String s=sr.nextLine();
				psmt.setString(1, s);
				System.out.println(s+"���� ���� ����� ����Ʈ!");
				rs =psmt.executeQuery();
				while(rs.next())
				{
					System.out.print(rs.getString("sitename"));
					System.out.println("\t�̿��ڼ�"+rs.getString("count(a.gender)"));
				}
				
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
	}
	public void userAge() // ���ɺ� ����Ʈ �̿��ڼ� ��ȸ
	{
		String sql = "select sitename,agegroup , count(agegroup) \r\n" + 
				"				from(select b.sitename ,  \r\n" + 
				"				       (case when a.age <20 then '10��'\r\n" + 
				"				            when a.age <30 then '20��'\r\n" + 
				"				            when a.age <40 then '30��'\r\n" + 
				"				            when a.age <50 then '40��'\r\n" + 
				"				            when a.age <60 then '50��' else '60��' end)as agegroup \r\n" + 
				"				from  userprofile a join siteinfo b \r\n" + 
				"				    on (a.username = b.username)) \r\n" + 
				"where agegroup=?" + 
				"group by sitename,agegroup\r\n" + 
				"order by 3 desc";
		try 
		{
			psmt = conn.prepareStatement(sql);
			System.out.println("�˻��� ���ɴ븦 �Է��ϼ��� > ");
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
	public void locSearch() //������ ����Ʈ�̸� ��ȸ
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
