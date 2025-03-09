import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import com.tahabugra.Follower;
import com.tahabugra.Following;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		/*
		 1-Biografi bilgisini gösterelim
		 2-Takipçi sayısını göster.
		 3-Profil resminin bilgisini versin.
		 4-Takipçi listesini getirsin.
		 5-Takip ettiği kişileri getir.
		 6-Çıkış
		 */
		
		//Kullanıcı adı:
		Scanner scanner = new Scanner(System.in);
		String username = null;
		String password = null;
		String islemler = "1-Biografi bilgisini göster\n"
				       +  "2-Takipçi sayısını göster\n"
				       +  "3-Profil resminin linkini al\n"
				       +  "4-Takipçi listesini getir\n"
				       +  "5-Takip ettiği kişilerin listesini getir\n"
				       +  "6-Çıkış\n"
				       +  "Seçiminiz: ";
		
		System.out.println("INSTAGRAM PROJESİNE HOŞGELDİNİZ");
		
		System.out.print("Kullanıcı adınızı giriniz: ");
		username = scanner.nextLine();
		
		System.out.print("Şifrenizi giriniz: ");
		password = scanner.nextLine();
		if(username.equals("kimbusik9192") && password.equals("bugra44.")) {
			Instagram4j instagram = Instagram4j.builder().username(username).password(password).build();
			instagram.setup();
			try {
				Thread.sleep(30000); // 30 saniye beklet
				instagram.login();
				InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
				System.out.println(islemler);
				String secim = scanner.nextLine();
				
				System.out.println("API Yanıtı: " + userResult);
				
				if(secim.equals("6")) {
					System.out.println("Uygulama sonlandırılıyor...");
					Thread.sleep(1000);
					System.out.println("Uygulama sonlandırıldı!");
				}
				else if(secim.equals("1")) {
					System.out.println("Biography: " + userResult.getUser().biography);
				}
				else if(secim.equals("2")) {
					System.out.println("Takipçi Sayısı: " + userResult.getUser().follower_count);
				}
				else if(secim.equals("3")) {
					System.out.println("Profil Resminin Linki: " + userResult.getUser().profile_pic_url);
				}
				else if(secim.equals("4")) {
					InstagramGetUserFollowersResult followerList = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
					
					String takipciIslemler = "1-Mail Gönder\n"
							               + "2-Dosyaya Yazdır\n"
							               + "3-Console Yazdır\n"
							               + "4-Hiçbir şey yapma";
					System.out.println(takipciIslemler);
					String takipciSecim = scanner.nextLine();
					
					if(takipciSecim.equals("1")) {
						//Mail Gönder
						StringBuffer buffer = new StringBuffer();
						int i = 1;
						for (InstagramUserSummary follower : followerList.getUsers()) {
							buffer.append(i+")" + follower.getPk() + " " + follower.getUsername() + " " + follower.getFull_name() + "\n");
							i++;
						}
						mailGonder("tahabugra@gmail.com", buffer.toString());
					}
					else if(takipciSecim.equals("2")) {
						//Dosyaya Yazdır
						List<Follower> followers = new ArrayList<>();
						for (InstagramUserSummary fw : followerList.getUsers()) {
							Follower follower = new Follower();
							follower.setPk(fw.getPk());
							follower.setUsername(fw.getUsername());
							follower.setFullname(fw.getFull_name());
							followers.add(follower);
						}
						
						File file = new File("C:\\Users\\tahab\\OneDrive\\Masaüstü\\İnstagram-Api IO\\followers.bin");
						if(!file.exists()) {
							file.createNewFile();
						}
						
						 writeFollowersToFile(file, followers);
						
					}
					else if(takipciSecim.equals("3")) {
						//Console Yazdır
						int i = 1;
						for (InstagramUserSummary follower : followerList.getUsers()) {
							System.out.println(i + ")" + follower.getPk() + follower.getUsername() + follower.getFull_name());
							i++;
						}
					}
					else if(takipciSecim.equals("4")) {
						//Hiçbir şey yapma
						System.out.println("Yapılacak işlem yok...");
					}
					else {
						System.out.println("Lütfen 1 ile 4 arasında bir seçim yapınız!");
					}
				}
				else if(secim.equals("5")) {
					InstagramGetUserFollowersResult followingList = instagram.sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk()));
					String takipIslemler = "1-Mail Gonder"
							              +"2-Dosyaya yazdır"
							              +"3-Console yazdır"
							              +"Hiçbir şey yapma";
					System.out.println(takipIslemler);
					String takipEttiklerimSecim = scanner.nextLine();
					if(takipEttiklerimSecim.equals("1")) {
						//Mail Gonder
						int i = 1;
						StringBuffer buffer = new StringBuffer();
						for (InstagramUserSummary following : followingList.getUsers()) {
							buffer.append(i+")" + following.pk + " " + following.username + " " + following.full_name + "\n" );
							i++;
						}
						mailGonder("tahabugra@gmail.com", buffer.toString());
					}
					else if(takipEttiklerimSecim.equals("2")) {
						//Dosyaya yazdır
						List<Following> followings = new ArrayList<Following>();
						for (InstagramUserSummary fw : followingList.getUsers()) {
							Following following = new Following(fw.getPk(),fw.getUsername(),fw.getFull_name());
							followings.add(following);
						}
						File file = new File("C:\\Users\\tahab\\OneDrive\\Masaüstü\\İnstagram-Api IO\\following.bin");
						if(!file.exists()) {
							file.createNewFile();
							System.out.println("Dosya oluşturuluyor...");
						}
						writeFollowingToFile(file, followings);
					}
					else if(takipEttiklerimSecim.equals("3")) {
						//Console yazdır
						int i = 1;
						for (InstagramUserSummary following : followingList.getUsers()) {
							System.out.println(i + ")" + following.getPk() + " " + following.getUsername() + " " + following.getFull_name());
						}
					}
					else if(takipEttiklerimSecim.equals("4")) {
						System.out.println("Hiçbir şey yapılmayacaktır...");
					}
					else {
						System.out.println("Lütfen 1 ile 4 arasında bir değer girişi yapınız!");
					}
				}
			} catch (ClientProtocolException e) {
				System.out.println("Error: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
		else {
			System.out.println("Kullanıcı adınız veya şifreniz yanlıştır!");
		}
	}
	
	public static void writeFollowersToFile (File file, List<Follower> followers) {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
			out.writeObject(followers);
			System.out.println("Takipçi listesi başarılı bir şekilde dosyaya yazdırıldı...");
		} catch (FileNotFoundException e) {
			System.out.println("Hata: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Hata: " + e.getMessage());
		}
	}
	
	public static void mailGonder(String to, String icerik) {
		String fromEmail = "tahabugra@gmail.com";
		String fromPassword = "tahabugra1.";
		
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(fromEmail, fromPassword);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Takipci Listesi");
			message.setText(icerik);
			Transport.send(message);
			System.out.println("Mail başarılı bir şekilde gönderildi...");
		}catch (Exception e) {
			System.out.println("Hata: Mail gönderilirken bir hata oluştu! " + e.getMessage());
		}
		
	}
	
	public static void writeFollowingToFile (File file, List<Following> following) {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
			out.writeObject(following);
			System.out.println("Takipçi listesi başarılı bir şekilde dosyaya yazdırıldı...");
		} catch (FileNotFoundException e) {
			System.out.println("Hata: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Hata: " + e.getMessage());
		}
	}

}
