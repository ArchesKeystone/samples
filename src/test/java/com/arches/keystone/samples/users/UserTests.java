package com.arches.keystone.samples.users;

import com.arches.keystone.model.ProfileAttribute;
import com.arches.keystone.model.Status;
import com.arches.keystone.model.User;
import com.arches.keystone.samples.util.RestClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("User Tests")
class UserTests {

    static String userName;
    static String password;
    static String keystoneUrl;
    static RestClient restClient;

    @BeforeAll
    public static void init() throws Exception {
        Properties props = new Properties();

        InputStream is = ClassLoader.getSystemResourceAsStream("application.properties");
        props.load(is);
        userName = (String) props.get("keystone.userName");
        password = (String) props.get("keystone.password");
        keystoneUrl = (String) props.get("keystone.url");
        restClient = new RestClient(keystoneUrl, userName, password);
    }

    @Test
    @DisplayName("Get Campaign details and verify credentials")
    void getCampaignDetails() throws Exception {
        User user = restClient.get("/user/details", User.class);
        assertNotNull(user.getUuid());
    }

    @Test
    @DisplayName("Get enrolled profiles")
    void getEnrolledProfiles() throws Exception {
        List<User> list = restClient.get("/user/profiles/*/*", UserList.class);
        assertTrue(list.size()>=0);
    }


    @Test
    @DisplayName("Get enabled profile attributes")
    void getEnabledProfileAttributes() throws Exception {
        List<ProfileAttribute> list = restClient.get("/user/profileattrs/enabled", ProfileAttributeList.class);
        assertTrue(list.size()>=0);
    }

    @Test
    @DisplayName("Get enroll form profile attributes")
    void getEnrollFormProfileAttributes() throws Exception {
        List<ProfileAttribute> list = restClient.get("/user/enrollProfileattrs", ProfileAttributeList.class);
        assertTrue(list.size()>=0);
    }

    @Test
    @DisplayName("Get profile details")
    void getUserProfileDetails() throws Exception {
        List<User> list = restClient.get("/user/profiles/*/*", UserList.class);
        assertTrue(list.size()>=0);
        if(list.size()>0){
            User u = list.get(0);
            User profile =  restClient.get("/user/"+u.getUuid()+"/uid", User.class);
            assertNotNull(profile);
        }
    }


    @Test
    @DisplayName("Enroll user")
    void enrollUser() throws Exception {
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");
        user.setEmail("test@test123.com");
        Status status = restClient.post("/user/enroll", user, Status.class);

    }

    @Test
    @DisplayName("Reprofile user")
    void reprofileUser() throws Exception {
        User user = new User();
        user.setFirstName("first name2");
        user.setLastName("last name");
        user.setEmail("test@test123.com");
        Status status = restClient.put("/user/", user, Status.class);


        status = restClient.put("/user/cancelByToken", status.getEntityUid(), Status.class);

    }


}

class UserList extends ArrayList<User> {

}


class ProfileAttributeList extends ArrayList<ProfileAttribute> {

}