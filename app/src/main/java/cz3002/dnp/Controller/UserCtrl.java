package cz3002.dnp.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cz3002.dnp.Constants;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 24/2/2016.
 */
public class UserCtrl implements Constants {
    private static UserCtrl instance;

    public static User currentUser = new User();

    public static UserCtrl getInstance() {
        if (instance == null) {
            instance = new UserCtrl();
        }
        return instance;
    }

    // Query users from server
    public User getUser(int id) {
        User user = new User();
        // Query user data from the server
        try {
            String query = String.format("select * from `user` inner join `patient` on user.id=patient.userid where id=%d", id);
            boolean isDoctor = false;
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // If try to get id from patient fails
                // Try to get id from doctor
                query = String.format("select * from `user` inner join `doctor` on user.id=doctor.userid where id=%d", id);
                isDoctor = true;
                document = Jsoup.connect(SERVER + query).get();
                queryJson = document.body().html();
                if (queryJson.equals("0")) { // If still fails
                    return user;
                }
            }
            // Process JSON format
            JSONArray queryResultArr = new JSONArray(queryJson);
            JSONObject queryResultObj = queryResultArr.getJSONObject(0);

            // Read information
            String usernameString = queryResultObj.getString("username");
            String passwordString = queryResultObj.getString("password");
            String emailString = queryResultObj.getString("email");
            String fullnameString = queryResultObj.getString("fullname");

            // Set all information to object User
            user.setId(id);
            user.setUsername(usernameString);
            user.setEmail(emailString);
            user.setPassword(passwordString);
            user.setFullname(fullnameString);
            user.setType(isDoctor);

            return user;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    //Query user information using username
    public User getUser(String username) {
        User user = new User();
        // Query user data from the server
        try {
            String query = String.format("select * from `user` inner join `patient` on user.id=patient.userid where username='%s'", username);
            boolean isDoctor = false;
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // If try to get id from patient fails
                // Try to get id from doctor
                query = String.format("select * from `user` inner join `doctor` on user.id=doctor.userid where username='%s'", username);
                isDoctor = true;
                document = Jsoup.connect(SERVER + query).get();
                queryJson = document.body().html();
                if (queryJson.equals("0")) { // If still fails
                    return user;
                }
            }
            // Process JSON format
            JSONArray queryResultArr = new JSONArray(queryJson);
            JSONObject queryResultObj = queryResultArr.getJSONObject(0);

            // Read information
            int id = Integer.parseInt(queryResultObj.getString("id"));
            String passwordString = queryResultObj.getString("password");
            String emailString = queryResultObj.getString("email");
            String fullnameString = queryResultObj.getString("fullname");

            // Set all information to object User
            user = createUser(id, username, passwordString, emailString, fullnameString, isDoctor);

            return user;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Quickly set all the information for a particular user
    public User createUser(int id, String username, String password, String email, String fullname, boolean type) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFullname(fullname);
        user.setType(type);

        return user;
    }
}
