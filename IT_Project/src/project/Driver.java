package project;

import uca.jetty.Container;

public class Driver 
{
	public static void main(String[] args) 
	{
		
		Container.startServer(80);
		//Container.addHandler(new WebPage(),"/webpage");	
		Container.addHandler(new NewWebPage(),"/newpage");
	}	//end main
}//Driver


