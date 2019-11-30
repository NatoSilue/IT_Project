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
			rs.close();

			OracleWrapper.prepareStatement("SELECT COUNT(*) FROM SERVICES WHERE SORTCATEGORY = 10");
			rs = OracleWrapper.queryDB();
			int totalGoogle = 0;
			while(rs.next())
			{
				totalGoogle = rs.getInt(1);
			}//end while
			rs.close();

			OracleWrapper.prepareStatement("SELECT STATUS, NAME FROM SERVICES WHERE SORTCATEGORY = 20 AND STATUS != 1");
			rs = OracleWrapper.queryDB();
			ArrayList <String> ucaServices = new ArrayList();
			ArrayList <Integer> ucaServicesStatus = new ArrayList();
			while(rs.next())
			{
				ucaServicesStatus.add(rs.getInt(1));
				ucaServices.add(rs.getString(2));
			}//end while
			rs.close();

			OracleWrapper.prepareStatement("SELECT COUNT(*) FROM SERVICES WHERE SORTCATEGORY = 20");
			rs = OracleWrapper.queryDB();
			int totalUcaServices = 0;
			while(rs.next())
			{
				totalUcaServices = rs.getInt(1);
			}//end while
			rs.close();

			OracleWrapper.prepareStatement("SELECT STATUS, NAME FROM SERVICES WHERE SORTCATEGORY = 30 AND STATUS != 1");
			rs = OracleWrapper.queryDB();
			ArrayList <String> cobRemotes = new ArrayList();
			ArrayList <Integer> remoteStatus = new ArrayList();

			while(rs.next())
			{
				remoteStatus.add(rs.getInt(1));
				cobRemotes.add(rs.getString(2));
			}//end while
			rs.close();

			OracleWrapper.prepareStatement("SELECT COUNT(*) FROM SERVICES WHERE SORTCATEGORY = 30");
			rs = OracleWrapper.queryDB();
			int totalRemotes = 0;
			while(rs.next())
			{
				totalRemotes = rs.getInt(1);
			}//end while
			rs.close();

			OracleWrapper.prepareStatement("SELECT MESSAGE FROM MESSAGES ORDER BY CREATION ASC");
			rs = OracleWrapper.queryDB();
			ArrayList <String> downMessages = new ArrayList();

			while(rs.next())
			{
				downMessages.add(rs.getString(1));
			}//end while
			rs.close();
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

			placeHolder1 += "<table style = \"float:right; margin-right:48vw; margin-top:4vh;" +
					" border-width: 5%; border-style: solid; border-color: #8B008B;\"><tr><td rowspan=\"100\">";

			if(downMessages.size()== 0)
			{
				placeHolder1 += "<textarea style=\"maxlength:100%; height:68vh; width:45vw; font-size:2.5vw ;background-color: #F0F0F0; value= \" \" readonly>";
				placeHolder1 += "All services are online and functioning normally!!!";
			}
			else {
				placeHolder1 += "<textarea style=\"maxlength:100%; height:68vh; width:45vw; font-size:1.3vw ;background-color: #F0F0F0; value= \" \" readonly>";

				for(int index = 0; index < downMessages.size(); index++)
				{
					placeHolder1 += downMessages.get(index) + "\n\n";
				}
			}

			placeHolder1 +=  "</textarea></td></tr></table>";

			//2- Services & Status table
			//2- a. Google Services

			String placeHolder2 = "<tr bgcolor = " + "> <td style = \"font-size:1.5vw;" + 
					"width:20vw; height:4vh;\"> Google Services</td>" + 
					"<td style = \"font-size:1.5vw; width: 30vw; height: 4vh;\"><b><font color = \"#009900\">" + (totalGoogle - googleServices.size()) +  "/" + totalGoogle + " UP</font></b></td> </tr>"; 

			for(int index = 0; index < googleServices.size(); index++)
			{
				String bgcolor ="\"lightgray\"";
				String status = " ";
				if(googleStatus.get(index) ==2)
				{
					status = "<font color = \"Yellow\">Degraded</font></td> </tr>";
				}
				else if(googleStatus.get(index) ==3)
				{
					status ="<font color = \"Red\">Down</font></td> </tr>";
				}
				else
				{
					status ="<font color = \"Orange\">Paused</font></td> </tr>";
				}

				placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:1.5vw;"  + 
						"width:50vw; height:4vh;\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + googleServices.get(index) + "</td>" +
						"<td style = font-size:1.5vw; width:30vw; height:4vh\">" + status;
			}//end for

			//2-b.UCA Services

			placeHolder2 += "<tr bgcolor = " + "> <td style = \"font-size:1.5vw;" + 
					" width:50vw; height:4vh\"> UCA Services</td>" + 
					"<td style = \"font-size:1.5vw; width:30vw; height:4vh;\"><b><font color = \"009900\">" + (totalUcaServices - ucaServices.size()) + "/" + totalUcaServices + " UP</font></b></td> </tr>"; 

			for(int index = 0; index < ucaServices.size(); index++)
			{
				String bgcolor ="\"lightgray\"";
				String status= "";
				if(ucaServicesStatus.get(index) == 2)
				{
					status = "<font color = \"Yellow\">Degraded</font></td> </tr>";	
				}

				else if(ucaServicesStatus.get(index) == 3)
				{
					status ="<font color = \"Red\">Down</font></td> </tr>";	
				}

				else
				{
					status= "<font color = \"Orange\">Paused</font></td> </tr>";	
				}

				placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:1.5vw;" +
						"width: 50vw; height: 4vh;\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + ucaServices.get(index) + "</td>" +
						"<td style = \"font-size:1.5vw; width:30vw; height:4vh\">" + status;
			}//end for

			//2-c. COB Remotes

			placeHolder2 += "<tr bgcolor = " + "> <td style = \"font-size:1.5vw;" + 
					"width: 50vw; height: 4vh;\"> COB Remotes </td>" + 
					"<td style = \" font-size:1.5vw; width: 30vw; height: 4vh;\"><b><font color = \"#009900\">" + (totalRemotes - cobRemotes.size()) +  "/" + totalRemotes + " UP</font></b></td> </tr>"; 

			for(int index = 0; index < cobRemotes.size(); index++)
			{
				String bgcolor ="\"lightgray\"";
				String status = " ";
				if(remoteStatus.get(index)==2)
				{
					status = "<font color = \"Yellow\">Degraded</font></td> </tr>";	
				}

				else if(remoteStatus.get(index)== 3)
				{
					status ="<font color = \"Red\">Down</font></td> </tr>";
				}

				else
				{
					status ="<font color = \"Orange\">Paused</font></td> </tr>";
				}

				placeHolder2 += "<tr bgcolor = " +bgcolor+ "> <td style = \"font-size:1.5vw;" +
						" width: 50vw; height: 4vh;\">"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cobRemotes.get(index) + "</td>" +
						"<td style = font-size:1.5vw; width: 30vw; height: 4vh;\">" + status;
			}//end for

			placeHolder2 += "</table>"; 

			//******************************************Emojis to express the Status Feeling***********************************************************************************************

			if((googleServices.size()+ ucaServices.size()+ cobRemotes.size())== 0)
			{
				placeHolder2 += "<table style = \"float:right; margin-right:8vw; margin-top:-45vh; background-color: #E6E6FA; border=\"0\">" +
						"<tr><td style = \"width: 32vw; height: 42vh;\"> <img src=\"images/allGood3.png\" style = \"width:28vw; height:34vh;\"> </td></tr>" +
						"</table>";

			}//end if

			else if (googleServices.size()+ ucaServices.size()+ cobRemotes.size()<= 3)
			{
				placeHolder2 += "<table style = \"float:right; margin-right:8vw; margin-top:-32vh; background-color: #E6E6FA; border=\"0\" width=\"\">" +
						"<tr><td style= \"width:30vw; height:40vh;\"> <img src=\"images/someBad2.png\"style = \"width:26vw; height:40vh;\"> </td></tr>" +
						"</table>";
			}//end else if

			else if (googleServices.size()+ ucaServices.size()+ cobRemotes.size()<= 5)
			{
				placeHolder2 += "<table style = \"float:right; margin-right:8vw; margin-top:-24vh; background-color: #E6E6FA; border=\"0\" width=\"\">" +
						"<tr><td style = \"width:30vw; height:33vh;\"> <img src=\"images/someBad2.png\"style =\" width:26vw; height:32vh;\"> </td></tr>" +
						"</table>";
			}//end else if

			dynamicHTML += placeHolder1;
			dynamicHTML += allHTML.substring(allHTML.indexOf("<<<placeHolder1>>>"), allHTML.indexOf("<<<placeHolder2>>>"));
			dynamicHTML += placeHolder2;
			dynamicHTML += "</div id=\"body\"></body></html>";

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


