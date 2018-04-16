package com.arches.keystone.samples.triggerdcomm;

import com.arches.keystone.model.*;
import com.arches.keystone.samples.util.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Triggered Communications Tests")
class TriggeredCommTests {

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
    @DisplayName("Get triggered communications list")
    void getTriggeredCommList() throws Exception {
        List<ArticleTriggerConfig> list = restClient.get("/tricomm/", ArticleTriggerConfigList.class);
        assertTrue(list.size()>0);
    }


    @Test
    @DisplayName("Fire triggered communication ")
    void fireTriggeredCommunication() throws Exception {
        List<ArticleTriggerConfig> list = restClient.get("/tricomm/", ArticleTriggerConfigList.class);
        assertTrue(list.size()>0);

        if(list.size()>0) {
            ArticleTriggerConfig config = list.get(0);
            Status status = restClient.put("/tricomm/"+config.getUuid()+"/execute", null, Status.class);
        }
    }


}

class ArticleTriggerConfigList extends ArrayList<ArticleTriggerConfig> {

}
