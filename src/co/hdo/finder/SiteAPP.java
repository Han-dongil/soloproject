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
		
		storage.searchDB();  // ���̺� ������ �����ϰ� ����.
		while(true)
		{			
			System.out.println("===============id/pw�����սô�===============");
			int choice1;
			while(true)
			{
				System.out.println("1.����ڵ�� 2.��������� 3.���̹��˻� 4.�����ͼ���");
				choice1=scanner.nextInt();
				if(choice1<1||choice1>4)
					System.out.println("�޴������� �߸��Ǿ����ϴ�");
				else break;
			}
			switch(choice1) 
			{
				case SELECT_1:storage.inputUser(); break;
				case SELECT_2:
					{
						System.out.println("����� �̸��� �Է��ϼ���");
						user=scanner.next();
						storage.login(user);
						int choice2;
					while(true)
					{
						System.out.println("1.����Ʈ ��� 2.��ȸ 3.���� 4.����");
						choice2=scanner.nextInt();
						if(choice2<1||choice2>4)
							System.out.println("�޴������� �߸��Ǿ����ϴ�");
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
						System.out.println("1.����Ʈ�� �̿��ڼ� 2.���� �̿��ڼ� 3.���ɺ��̿��ڼ� 4.�������̿��ڼ� 5.�˻��ŷ");
						choice3=scanner.nextInt();
						if(choice3<1||choice3>5)
						System.out.println("�޴������� �߸��Ǿ����ϴ�");
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
