package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uca.OracleWrapper;
public class NewWebPage extends HttpServlet
{

	private static final long serialVersionUID = 10L;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

///*************************************** All SQL Statements **************************************************************************************************************
		try {
			OracleWrapper.openConn();

			OracleWrapper.prepareStatement("SELECT STATUS,NAME FROM SERVICES WHERE SORTCATEGORY = 10 AND STATUS != 1");
			ResultSet rs = OracleWrapper.queryDB();
			ArrayList <String> googleServices = new ArrayList();
			ArrayList <Integer> googleStatus = new ArrayList();
			while(rs.next())
			{
				googleStatus.add(rs.getInt(1));
				googleServices.add(rs.getString(2));

			}//end while

			OracleWrapper.prepareStatement("SELECT COUNT(*) FROM SERVICES WHERE SORTCATEGORY = 10");
			ResultSet rs2 = OracleWrapper.queryDB();
			int totalGoogle = 0;
			while(rs2.next())
			{
				totalGoogle = rs2.getInt(1);
			}//end while

			OracleWrapper.prepareStatement("SELECT STATUS, NAME FROM SERVICES WHERE SORTCATEGORY = 20 AND STATUS != 1");
			ResultSet rs3 = OracleWrapper.queryDB();
			ArrayList <String> ucaServices = new ArrayList();
			ArrayList <Integer> ucaServicesStatus = new ArrayList();
			while(rs3.next())
			{
				ucaServicesStatus.add(rs3.getInt(1));
				ucaServices.add(rs3.getString(2));
			}//end while

			OracleWrapper.prepareStatement("SELECT COUNT(*) FROM SERVICES WHERE SORTCATEGORY = 20");
			ResultSet rs4 = OracleWrapper.queryDB();
			int totalUcaServices = 0;
			while(rs4.next())
			{
				totalUcaServices = rs4.getInt(1);
			}//end while

			OracleWrapper.prepareStatement("SELECT STATUS, NAME FROM SERVICES WHERE SORTCATEGORY = 30 AND STATUS != 1");
			ResultSet rs5 = OracleWrapper.queryDB();
			ArrayList <String> cobRemotes = new ArrayList();
			ArrayList <Integer> remoteStatus = new ArrayList();

			while(rs5.next())
			{
				remoteStatus.add(rs5.getInt(1));
				cobRemotes.add(rs5.getString(2));
			}//end while

			OracleWrapper.prepareStatement("SELECT COUNT(*) FROM SERVICES WHERE SORTCATEGORY = 30");
			ResultSet rs6 = OracleWrapper.queryDB();
			int totalRemotes = 0;
			while(rs6.next())
			{
				totalRemotes = rs6.getInt(1);
			}//end while

			OracleWrapper.prepareStatement("SELECT MESSAGE FROM MESSAGES ORDER BY CREATION ASC");
			ResultSet rs8 = OracleWrapper.queryDB();
			ArrayList <String> downMessages = new ArrayList();

			while(rs8.next())
			{
				downMessages.add(rs8.getString(1));
			}//end while

//***************************************Read my web.txt file*****************************************************************************************************************
			
			System.out.println(new java.io.File(".").getCanonicalPath());
			BufferedReader br = new BufferedReader(new FileReader("src/text/web.txt"));
			String allHTML = "";
			String oneLine = "";
			while ( (oneLine = br.readLine()) != null) 
			{
				allHTML += oneLine;
			} //end while
			br.close();
//***************************************Create a Dynamic HTML*****************************************************************************************************************
			//1- Message table
			
			String dynamicHTML = "";
			dynamicHTML += allHTML.substring(0, allHTML.indexOf("<<<placeHolder1>>>"));
			String placeHolder1="";
			if(downMessages.size()== 0)
			{
				placeHolder1 += "<table style = \"float:right; margin-right:600px; margin-top:10px;" +
						" border:2px solid #663399;\"><tr><td rowspan=\"100\">"+
						"<textarea maxlength=\"200\" rows=\"10\" cols=\"27\" style=\"resize: none; font-size:40px;background-color: #F0F0F0; value= \" \" readonly>";

				placeHolder1 += "All services are online and functioning normally!!!";
				placeHolder1 +=  "</textarea></td></tr></table>";
			}
			else {
				placeHolder1 = "<table style = \"float:right; margin-right:600px; margin-top:10px;" +
						" border:2px solid #663399;\"><tr><td rowspan=\"100\">"+
						"<textarea maxlength=\"200\" rows=\"20\" cols=\"60\" style=\"resize: none; font-size:20px; value= \" \" readonly>";

				for(int index = 0; index < downMessages.size(); index++)
				{
					placeHolder1 += downMessages.get(index) + "\n\n";
				}

				placeHolder1 +=  "</textarea></td></tr></table>";

			}

			//2- Services & Status table
			//2- a. Google Services
			
			String placeHolder2 = "<tr bgcolor = " + "> <td style = \"font-size:120%;" + 
					"width=\"200\" height=\"27\"> Google Services</td>" + 
					"<td width=\"100\" height=\"27\"><b><font color = \"#009900\">" + (totalGoogle - googleServices.size()) +  "/" + totalGoogle + " UP</font><b></td> </tr>"; 

			for(int index = 0; index < googleServices.size(); index++)
			{
				String bgcolor ="\"lightgray\"";
				if(googleStatus.get(index) ==2)
				{
					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"width=\"200\" height=\"27\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + googleServices.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"100\" height=\"27\"><font color = \"Yellow\">Degraded</font></td> </tr>";
				}
				else if(googleStatus.get(index) ==3)
				{

					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"width=\"200\" height=\"27\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + googleServices.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"100\" height=\"27\"><font color = \"Red\">DOWN</font></td> </tr>";
				}
				else
				{

					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"width=\"200\" height=\"27\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + googleServices.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"100\" height=\"27\"><font color = \"Orange\">Paused</font></td> </tr>";
				}
			}//end for

			//2-b.UCA Services
			
			placeHolder2 += "<tr bgcolor = " + "> <td style = \"font-size:120%;" + 
					" width=\"200\" height=\"27\"> UCA Services</td>" + 
					"<td width=\"100\" height=\"27\"><b><font color = \"009900\">" + (totalUcaServices - ucaServices.size()) + "/" + totalUcaServices + " UP</font><b></td> </tr>"; 

			for(int index = 0; index < ucaServices.size(); index++)
			{
				String bgcolor ="\"lightgray\"";

				if(ucaServicesStatus.get(index) == 2)
				{
					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"width=\"200\" height=\"27\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + ucaServices.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"100\" height=\"27\"><font color = \"Yellow\">Degraded</font></td> </tr>";	
				}

				else if(ucaServicesStatus.get(index) == 3)
				{
					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"width=\"200\" height=\"27\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + ucaServices.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"100\" height=\"27\"><font color = \"Red\">DOWN</font></td> </tr>";	
				}

				else
					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"width=\"200\" height=\"27\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + ucaServices.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"100\" height=\"27\"><font color = \"Orange\">Paused</font></td> </tr>";	
			}//end for

			//2-c. COB Remotes
			
			placeHolder2 += "<tr bgcolor = " + "> <td style = \"font-size:120%;" + 
					"\" width=\"200\" height=\"27\"> COB Remotes </td>" + 
					"<td width=\"100\" height=\"27\"><b><font color = \"#009900\">" + (totalRemotes - cobRemotes.size()) +  "/" + totalRemotes + " UP</font></b></td> </tr>"; 

			for(int index = 0; index < cobRemotes.size(); index++)
			{
				String bgcolor ="\"lightgray\"";
				if(remoteStatus.get(index)==2)
				{
					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"\" width=\"250\" height=\"27\">"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cobRemotes.get(index) + "</td>" +
							"<td style = font-size:140%; width=\"70\" height=\"27\"><font color = \"Yellow\">Degraded</font></td> </tr>";	
				}

				else if(remoteStatus.get(index)== 3)
				{
					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"\" width=\"260\" height=\"27\">"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cobRemotes.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"70\" height=\"27\"><font color = \"Red\">Down</font></td> </tr>";
				}

				else
				{
					placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:120%;" +
							"\" width=\"250\" height=\"27\">"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cobRemotes.get(index) + "</td>" +
							"<td style = font-size:120%; width=\"70\" height=\"27\"><font color = \"Orange\">Paused</font></td> </tr>";
				}
			}//end for

			placeHolder2 += "</table>"; 
			
//******************************************Emojis to express the Status Feeling***********************************************************************************************
			
			if((googleServices.size()+ ucaServices.size()+ cobRemotes.size())== 0)
			{
				placeHolder2 += "<table style = \"float:right; margin-right:20px; margin-top:-300px; background-color: #E6E6FA; border=\"0\" width=\"\">" +
						"<tr><td width=\"430\" height=\"50\"> <img src=\"images/allGood3.png\" width=\"300\" height=\"270\"> </td></tr>" +
						"</table>";

			}//end if

			else if (googleServices.size()+ ucaServices.size()+ cobRemotes.size()<= 3)
			{
				placeHolder2 += "<table style = \"float:right; margin-right:130px; margin-top:-210px; background-color: #E6E6FA; border=\"0\" width=\"\">" +
						"<tr><td width=\"300\" height=\"50\"> <img src=\"images/someBad2.png\" width=\"270\" height=\"240\"> </td></tr>" +
						"</table>";
			}//end else if

			else if (googleServices.size()+ ucaServices.size()+ cobRemotes.size() <= 5)
			{
				placeHolder2 += "<table style = \"float:right; margin-right:190px; margin-top:-105px; background-color: #E6E6FA; border=\"0\" width=\"\">" +
						"<tr><td width=\"200\" height=\"50\"> <img src=\"images/someBad2.png\" width=\"150\" height=\"135\"> </td></tr>" +
						"</table>";
			}//end else if

			dynamicHTML += placeHolder1;
			dynamicHTML += allHTML.substring(allHTML.indexOf("<<<placeHolder1>>>"), allHTML.indexOf("<<<placeHolder2>>>"));
			dynamicHTML += placeHolder2;
			
//**************************************Clean Up our DynamIc HTML*************************************************************************************************************
			dynamicHTML = dynamicHTML.replace("<<<placeHolder1>>>", "");
			dynamicHTML = dynamicHTML.replace("<<<placeHolder2>>>", "");
			dynamicHTML = dynamicHTML.replace("<<<placeHolder3>>>", "");

			out.print(dynamicHTML);

			OracleWrapper.closeConn();
		}catch (SQLException e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // end catch
	}//end doGet

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

	}//end goPost
	
//*****************************************End of the Program***************************************************************************************************************
}


