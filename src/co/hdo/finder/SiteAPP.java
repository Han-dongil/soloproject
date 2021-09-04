package co.hdo.finder;

import java.util.Scanner;

public class SiteAPP  implements Num
{
	String user;
	Storage storage = Storage.getInstance();
	SearchMenu search=SearchMenu.getInstance();
	Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) 
	{
		SiteAPP siteApp=new SiteAPP();
		siteApp.start();
	}	
	public void start() 
	{
		
		storage.searchDB();  // 테이블 없을시 생성하고 시작.
		while(true)
		{			
			System.out.println("===============id/pw저장합시다===============");
			int choice1;
			while(true)
			{
				System.out.println("1.사용자등록 2.사용자접속 3.네이버검색 4.데이터센터");
				choice1=scanner.nextInt();
				if(choice1<1||choice1>4)
					System.out.println("메뉴선택이 잘못되었습니다");
				else break;
			}
			switch(choice1) 
			{
				case SELECT_1:storage.inputUser(); break;
				case SELECT_2:
					{
						System.out.println("사용자 이름을 입력하세요");
						user=scanner.next();
						storage.login(user);
						int choice2;
					while(true)
					{
						System.out.println("1.사이트 등록 2.조회 3.수정 4.삭제");
						choice2=scanner.nextInt();
						if(choice2<1||choice2>4)
							System.out.println("메뉴선택이 잘못되었습니다");
						else break;
					}
						switch(choice2)
						{
							case SELECT_1:storage.inputSite(user);break;
							case SELECT_2:storage.findSite(user);break;
							case SELECT_3:storage.update(user);break;
							case SELECT_4:storage.delete(user);break;
						}
					}break;
				case SELECT_3:search.searchTable(); search.webSearch();break;
				case SELECT_4:
				{
					int choice3;
					while(true)
					{
						System.out.println("1.사이트별 이용자수 2.성별 이용자수 3.연령별이용자수 4.지역별이용자수 5.검색어랭킹");
						choice3=scanner.nextInt();
						if(choice3<1||choice3>5)
						System.out.println("메뉴선택이 잘못되었습니다");
						else break;
					}
					switch(choice3)
					{
						case SELECT_1: storage.siteSearch();break;
						case SELECT_2: storage.genderSearch();break;
						case SELECT_3: storage.userAge();break;
						case SELECT_4: storage.locSearch();break;
						case SELECT_5: search.keywordRank();break;
					}
				}

			}
		}
	}
}
