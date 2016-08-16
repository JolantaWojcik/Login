import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import sun.rmi.runtime.Log;

public class Main {
	/*
	 * new User("Jola", "sobota54321zaq12wsx");
		 * 
		 * liste 10-userow i stworzyla zestawienie ktorej kluczem jest dlugosc hasla a wartoscia liczba userow z dana dlugoscia hasla
		 * 2) kluczem jest TRUDNOSC hasla (latwe, srednie, trudne) -> liczba userow z danya trudnoscia hasla
		 * jak ocenic czy haslo jest latwe czy trdne:
		 * latwe: ma do 5znakow i same male litery
		 * srednie: ma powyzej 5 znakow i litery lub cyfry
		 * trudne: ma powyzej 8 znakow i ma litery cyfry lub znaki
		 * 
		 * co jesli haslo ma 5 znakow ale ma litery cyfry i znaki: srednie
		 */

	public static void main(String[] args)  throws Exception{
		// TODO Auto-generated method stub
		List<User> users = new ArrayList<>();
		users.add(new User("ala", "12345"));
		users.add(new User("ala", "12345"));
		users.add(new User("kasia", "kasia12345"));
		users.add(new User("jan", "asd.zxc"));
		users.add(new User("jakub", "zxcvb"));
		users.add(new User("adam", "password"));
		users.add(new User("adam", "pass"));
		users.add(new User("karol", "Asd.zxc55!"));
		users.add(new User("basia", "haslo123"));
		users.add(new User("ula", "pass9900"));
		users.add(new User("ola", "somePass9900"));
		System.out.println(users.toString());
		
		Map<Integer, Integer> sumOfUsersWithPassLength = new HashMap<>();
		//nie udalo sie 
		//Map<Integer, Integer> sumOfUsersWithPassLength = passwords.stream().collect(
        //Collectors.groupingBy(User::getPassword.length(), Collectors.counting()));
		//czy jest mozliwe User::getPassword.length()???
		int passLength = 0;
		for (User u: users){
			passLength = u.getPassword().length();
			if (!sumOfUsersWithPassLength.containsKey(passLength)) {
				sumOfUsersWithPassLength.put(passLength, 1);
			}else{
				sumOfUsersWithPassLength.put(passLength,
				sumOfUsersWithPassLength.get(passLength) + 1);
			}
		}
		sumOfUsersWithPassLength.forEach((k, v) -> 
		System.out.println("length of pass " + k + " nr of users with pass " + v));
		System.out.println("\n");
			
		//metod !Character.isLetter(ch) to sprawdzasz tylko bie¿¹cy znak z has³a - bez wiedzy o poprzednich czy nastepnych
		//a to trzeba najpierw przeanalizowaæ cale haslo a dopiero potem je ocenic
		String passDifficultyLevel =null;
		List<String> passwords = new ArrayList<>();
		for (User u: users){
			   for (int i=1; i<u.getPassword().length(); i++){
				   
                  if(u.getPassword().length()<=5 && containsNumbers(u.getPassword())==false
                		  &&containsLoweCaseLetters(u.getPassword())==true){
                	  passDifficultyLevel = "easy";
                	  System.out.println("**easy** " + u.getPassword().toString());
                  }
                  //srednie: ma powyzej 5 znakow i litery lub cyfry
                  else if(u.getPassword().length()>=5 && containsSpecialCharacters(u.getPassword())==false){
                	  passDifficultyLevel = "middle";
                	  System.out.println("**middle** " + u.getPassword().toString());
                  }
               // trudne: ma powyzej 8 znakow i ma litery cyfry lub znaki
                //problem z **unknown pass** asd.zxc
                  //lub z **hard** Asd.zxc55! wiêc "||" zamiast "&&"
                  else if(u.getPassword().length()>=8 || containsSpecialCharacters(u.getPassword())==true){
                	  passDifficultyLevel = "hard";
                	  System.out.println("**hard** " + u.getPassword().toString());
                  }else{
                	  passDifficultyLevel = "**unknown**";
                	  System.out.println("**unknown pass** " + u.getPassword().toString());
                  }
               }
			   passwords.add(passDifficultyLevel);
		}
		   Map<String, Long> sumOfUsersWithPassLevel = passwords.stream().collect(
	              Collectors.groupingBy(Function.identity(), Collectors.counting()));

		 sumOfUsersWithPassLevel.forEach((k, v) -> 
			System.out.println("level of pass " + k + " nr of users with pass " + v));
	        
	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_data.txt"));
	oos.writeObject(users);
	oos.close();
	
	ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_data.txt"));
	List<User> readed = (List<User>) ois.readObject();
	ois.close();
	
	System.out.println("\n");
	readed.forEach(System.out::println);
}
	
	public final static boolean containsNumbers(String s) {
		 Pattern p = Pattern.compile(".*[0-9].*");
	     Matcher m = p.matcher(s);
	    if (s != null && !s.isEmpty()) {if (m.find()) {
	                return true;
	            }
	    }
	    return false;
	}
	public final static boolean containsLoweCaseLetters(String s) {
	    if (s != null && !s.isEmpty()) {
	        for (char c : s.toCharArray()) {
	            if (Character.isLowerCase(c)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	//Character.isSpaceChar(c)
	public final static boolean containsSpecialCharacters(String s) {
		 Pattern p = Pattern.compile("[\\p{Alpha}]*[\\p{Punct}][\\p{Alpha}]*");
	     Matcher m = p.matcher(s);
	    if (s != null && !s.isEmpty()) {
	            if (m.find()) {
	                return true;
	            }
	    }
	    return false;
	}
}
