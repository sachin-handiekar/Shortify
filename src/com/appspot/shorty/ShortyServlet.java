/**
 * Shorty 
 * 		A simple API for the Goo.gl URL shortener
 *
 * @author Sachin Handiekar
 * @version 1.0
 */

package com.appspot.shorty;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ShortyServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        PrintWriter   out          = resp.getWriter();
        StringBuilder sb           = null;
        String        line         = null;
        String        urlStr       = req.getParameter("url");
        String        errorMessage = "{\"error_message\":\"Internal Error Occurred\"}";

        // If there is empty parameter, just change the urlStr to empty http url.
        if (urlStr == null) {
            urlStr = "http://";
        }

        try {
            URL               url        = new URL("http://goo.gl/api/url");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "toolbar");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            writer.write("url=" + URLEncoder.encode(urlStr, "UTF-8"));
            writer.close();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            sb = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }

            out.println(sb.toString());
        } catch (MalformedURLException e) {
            out.println(errorMessage);
        } catch (IOException e) {
            out.println(errorMessage);
        }
    }
}
