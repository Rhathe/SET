package gui;

import java.net.*;

public class RoomList {
	public String[] roomMember = new String[10];
	private int numOfMembers = 0;
	public String[] ipAddr = new String[10];
	
	//constructor
	public RoomList(String[] people){
		if (people == null)
			return;
		
		for(int i=0; i<10; i++){
			roomMember[i]=" ";
			if(i<people.length){
				roomMember[i]=people[i];
				System.out.println(roomMember[i]);
				numOfMembers++;
			}
		}
	}

	//add a member
	public void addMember(String newMember){
		if(numOfMembers>9){
			System.err.println("shit dawg, i can't do that!");
		}
		else if (findMember(newMember) == true) {
			System.err.println("already all up in this grill");
		}
		else{
			roomMember[numOfMembers]=newMember;
			try {
				InetAddress thisIp = InetAddress.getLocalHost();
				System.out.println("IP:"+thisIp.getHostAddress());
				ipAddr[numOfMembers] = thisIp.getHostAddress();
			}
			catch(Exception e) {
					e.printStackTrace();
			}
			numOfMembers++;
		}
	}
	
	private Boolean findMember(String newMember) {
		for (String test : roomMember) {
			if (newMember.equals(test)) {
				return true;
			}
		}
		return false;
	}
	
	//remove a member
	public void removeMember(String leavingMember){
		boolean foundIt=false;
		for(int i=0; i<9; i++){
			if(roomMember[i]==leavingMember){
				--numOfMembers;
				while(i<9){
					roomMember[i]=roomMember[i+1];//moves every name down the list
					ipAddr[i] = ipAddr[i+1];
					i++;
					foundIt=true;
				}
			}
		}
		if(foundIt==false){
			System.err.println("that sucka was never here");
			return;
		}
		roomMember[9]=""; //too get around the i = i+1
		ipAddr[9]=null;
	}
	
	public int getNumPlayers() {
		return numOfMembers;
	}
}
