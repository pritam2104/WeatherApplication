package weatherservlete;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class weatherservlete
 */
@WebServlet("/weatherservlete")
public class weatherservlete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public weatherservlete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.sendRedirect("index.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String apikey ="e006026113fe95cc8ac7fe8d0676e1d2";
		String city = request.getParameter("city");
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city +"&appid=" + apikey;
		try {
			URL url = new URL (apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			InputStream inputStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			
			Scanner scanner = new Scanner(reader);
			StringBuilder resopnseContent = new StringBuilder();
			
			while (scanner.hasNext()) {
				resopnseContent.append(scanner.nextLine());
			}
			scanner.close();
			Gson gson = new Gson ();
			JsonObject jsonObject = gson.fromJson(resopnseContent.toString(), JsonObject.class);
			
			long dateTimestamp = jsonObject.get ("dt") .getAsLong() * 1000;
			Date date = new Date(dateTimestamp);
			
			double temperatureKelvin = jsonObject.getAsJsonObject("main") . get ("temp") .getAsDouble();
			int temperatureCelsius = (int) (temperatureKelvin - 273.15);
			
			int humidity = jsonObject.getAsJsonObject ("main"). get ("humidity") .getAsInt();
			
			double windSpeed = jsonObject.getAsJsonObject ("wind") .get ("speed" ) .getAsDouble();
			
			String weatherCondition = jsonObject.getAsJsonArray("weather") .get (0) .getAsJsonObject () .get ("main").getAsString();
			
			request. setAttribute("date" , date);
			request. setAttribute("city", city);
			request. setAttribute("temperature", temperatureCelsius);
			request. setAttribute("weatherCondition", weatherCondition); 
			request. setAttribute("humidity", humidity);
			request. setAttribute("windSpeed", windSpeed);
			request. setAttribute( "weatherData",resopnseContent.toString());
			
			connection. disconnect();
							
		}catch (IOException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
				
	}

}
