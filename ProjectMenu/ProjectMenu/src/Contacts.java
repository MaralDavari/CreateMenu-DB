import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Contacts {
	
	int id;
	String firstName;
	String lastName;
	String phoneNumber;
	
	Contacts(){
		
	}
	
	Contacts(int id,String firstName, String lastName,String phoneNumber){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;	
	}
	
	Contacts(String firstName, String lastName,String phoneNumber){
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;	
	}

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	// get all items for Menu
	
	public static void myMenu() throws Exception{
		Scanner scan = new Scanner(System.in);
		boolean choice=false;
		
		System.out.println("\n**************\nWelcome to my Program! Choose one option...\n************");
		while(choice == false) {
			System.out.println("1: Create new Contact\n2: Find a contact\n3: Find all contacts\n"
					+ "4: Delete a contact\n5: Delete all Contacts\n6: Exit the program");
			int selectedMenuItem = scan.nextInt();
			if(selectedMenuItem == 1) {
				ArrayList myList = new ArrayList();
				myList = Contacts.getMenu();
				Contacts.addNewContacts(myList);
			}else if(selectedMenuItem == 2){
				System.out.println("Find by id, enter contact id: ");
				int id = scan.nextInt();
				Contacts contact = Contacts.getContact(id);
				System.out.println(contact.getId() + " " + contact.getFirstName() + " " + 
									contact.getLastName() + " " + contact.getPhoneNumber());
				scan.nextLine();
			}else if(selectedMenuItem == 3) {
				ArrayList myList = new ArrayList();
				myList = Contacts.getAllContacts();
				Contacts.show(myList);
				scan.nextLine();
			}else if(selectedMenuItem == 4) {
				System.out.println("Enter id of contact you want to delete");
				Contacts.deleteContact(scan.nextInt());
				scan.nextLine();
			}else if(selectedMenuItem == 5) {
				Contacts.deleteAllContacts();
				System.out.println("All contacts have been deleted...");
				scan.nextLine();
			}else if(selectedMenuItem == 6) {
				choice = true;
				break;
			}
			
		}
	}
	
	// get initial Menu	
	public static ArrayList getMenu() {
		Scanner scan = new Scanner(System.in);
		
		ArrayList myList = new ArrayList();
		boolean choice = false;
		
		// Menu create new Contact
		System.out.println("\n**************\nCreate new contact...\n************");
		while(choice == false) {
			Contacts contact = new Contacts();
			System.out.println("Enter First Name: ");
			String fname = scan.nextLine();
			System.out.println("Enter Last Name: ");
			String lname = scan.nextLine();
			System.out.println("Enter Phone Number: ");
			String phoneNum = scan.nextLine();
			
			contact.setFirstName(fname);
			contact.setLastName(lname);
			contact.setPhoneNumber(phoneNum);
			
			myList.add(contact);
			
			System.out.println("Continue?");
			String enteredChoice = scan.nextLine();
			if(enteredChoice.equalsIgnoreCase("No")) {
				choice = true;
				break;
			}		
		}
		return myList;
	}
	
	// add new contacts
	public static int addNewContacts(ArrayList list) throws Exception {
		int count = 0;
		Connection conn = ConnectionDB.startDB();
		String query = "insert into contacts values(my_contacts_sequencedd.nextval,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(query);
		
		for(Object element: list) {
			Contacts contact = (Contacts) element;
			pst.setString(1, contact.getFirstName());
			pst.setString(2, contact.getLastName());
			pst.setString(3, contact.getPhoneNumber());
			
			conn.prepareStatement("commit");
			pst.executeUpdate();
			
			count ++;
		}
		pst.close();	
		conn.close();
		
		return count;	
	}
	
	
	// get all contacts
	public static ArrayList getAllContacts() throws Exception {
		Connection conn = ConnectionDB.startDB();
		String query = "Select * from contacts order by id asc";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		ArrayList myList = new ArrayList();
		while(rs.next()) {
			
			int id = rs.getInt(1);
			String firstName = rs.getString(2);
			String lastName = rs.getString(3);
			String phoneNumber = rs.getString(4);
			
			Contacts contact = new Contacts(id,firstName,lastName,phoneNumber);
			myList.add(contact);		
			
		}
		rs.close();
		conn.close();
		return myList;			
	}
	
	// get specific contact by fname
	public static Contacts getContact(int id) throws Exception{
		Connection conn = ConnectionDB.startDB();
		PreparedStatement pst = conn.prepareStatement("Select * from contacts where id = " + id);
		ResultSet rs = pst.executeQuery();
		rs.next();
		int id2 = rs.getInt(1);
		String firstname = rs.getString(2);
		String lastName = rs.getString(3);
		String phonenumber = rs.getString(4);
		
		Contacts contact = new Contacts(id2,firstname,lastName,phonenumber);
		
		rs.close();
		conn.close();
		
		return contact;
		
	}
	
	// Delete all contacts
	public static int deleteAllContacts() throws Exception {
		Connection conn = ConnectionDB.startDB();
		PreparedStatement pst = conn.prepareStatement("delete from contacts");
		conn.prepareStatement("commit");
		int rows = pst.executeUpdate();
		pst.close();
		conn.close();
		
		return rows;	
	}
	
	// delete specific contact
	public static void deleteContact(int id) throws Exception {
		Connection conn = ConnectionDB.startDB();
		String query = "delete from contacts where id = " + id;
		PreparedStatement pst = conn.prepareStatement(query);
		conn.prepareStatement("commit");
		pst.executeUpdate();
		
		pst.close();
		conn.close();
	}
	
	// show
	public static void show(ArrayList list) {
		
		for(Object element : list) {
			Contacts contact = (Contacts) element;
			System.out.println(
					contact.getId() + " " +
					contact.getFirstName() + " " +
					contact.getLastName() + " "+
					contact.getPhoneNumber());	
		}
	}
	

}
