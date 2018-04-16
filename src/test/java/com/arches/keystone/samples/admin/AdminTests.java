package com.arches.keystone.samples.admin;

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

class AdminTests {


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
    @DisplayName("Get segments list")
    void getSegments() throws Exception {
        List<Segment> list = restClient.get("/admin/segment/", SegmentList.class);
        assertTrue(list.size()>0);
    }

    @Test
    @DisplayName("add segment")
    void addSegment() throws Exception {
        Segment segment = new Segment();
        segment.setCode("A");
        segment.setName("A");
        segment.setSegmentRule("1==1");
        segment.setOrdinal(1);
        Status status = restClient.post("/admin/segment/", segment, Status.class);
        assertNotNull(status.getEntityUid());

        segment.setUuid(status.getEntityUid());
        segment.setCode("A2");
        status = restClient.put("/admin/segment/", segment, Status.class);
    }


    @Test
    @DisplayName("get enabled profile attributes ")
    void getEnabledProfileAttributes() throws Exception {
        List<ProfileAttribute> list = restClient.get("/admin/profileattrs/enabled", ProfileAttributeList.class);
        assertTrue(list.size()>0);
    }

    @Test
    @DisplayName("Enable profile attribute ")
    void enableProfileAttribute() throws Exception {
        List<ProfileAttribute> list = restClient.get("/admin/profileattrs", ProfileAttributeList.class);
        assertTrue(list.size()>0);

        ProfileAttribute attribute = list.get(0);
        Status status = restClient.put("/admin/profileattrs/"+attribute.getUuid()+"/enable", null, Status.class);


        status = restClient.put("/admin/profileattrs/"+attribute.getUuid()+"/disable", null, Status.class);


    }

}

class SegmentList extends ArrayList<Segment> {

}


class ProfileAttributeList extends ArrayList<ProfileAttribute> {

}