import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable{
	/*
	   * Stworz klasie User (login, password) stworz liste 5ciu userow i
		 * zapisz ja do pliku za pomoca serializacji jednak uwaga! password po
		 * otwarciu pliku powinien byc NIECZYTELNY (MD5, kodowanie, szyfowanie
		 * sswoje) dla u¿ytkownika. Ale powinien podlegaæ procesowi serializacji
		 * i deserializacji.
		 * 
		 * (wykorzystaj: serializable, writeObject, readObject (metody
		 * prywatne), oraz slowko: transient
	 */
	private String login;
	private String password;
	
	public User(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(getPassword().getBytes());
		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
		  sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		out.defaultWriteObject();
		out.writeObject(getLogin());
		out.writeObject(sb.toString());
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
	//	md.update(getPassword().getBytes());
		 byte[] dataBytes = new byte[1024];
		 int nread = 0;
	      while ((nread = in.read(dataBytes)) != -1) {
	          md.update(dataBytes, 0, nread);
	        };
	        byte[] mdbytes = md.digest();

	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
		in.defaultReadObject();
		setLogin((String) in.readObject());
		setPassword((String) in.readObject());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", password=" + password + "]";
	}
	
	
}
