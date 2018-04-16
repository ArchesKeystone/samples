package com.arches.keystone.samples.tenant;

import com.arches.keystone.model.*;
import com.arches.keystone.samples.util.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tenant Tests")
public class TenantTests {

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
    @DisplayName("Get Campaign list")
    void getCampaigns() throws Exception {
        List<Tenant> list = restClient.get("/tenant/campaign/", TenantList.class);
        assertTrue(list.size()>0);
    }

    @Test
    @DisplayName("tenant add/update/get operations ")
    void tenantOperations() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setName("new tenant");
        Status status = restClient.post("/tenant/", tenant, Status.class);
        assertNotNull(status.getEntityUid());
        tenant.setUuid(status.getEntityUid());

        tenant.setName("updated tenant");
        status = restClient.put("/tenant/", tenant, Status.class);
        assertNotNull(status.getEntityUid());

        Tenant dbtenant = restClient.get("/tenant/"+tenant.getUuid(), Tenant.class);
        assertEquals(dbtenant.getName(),tenant.getName());

        User user = new User();
        user.setEmail("testtenant@test.com");
        user.setPassword("password");
        user.setFirstName("test");
        user.setLastName("tenant user");
        status = restClient.post("/tenant/"+tenant.getUuid()+"/user", user, Status.class);
        assertNotNull(status.getEntityUid());
    }

    @Test
    @DisplayName("client add/update/get operations ")
    void clientOperations() throws Exception {

        TenantList tenantList = restClient.get("/tenant/client", TenantList.class);
        assertTrue(tenantList.size()>0);

        Tenant tenant = tenantList.get(tenantList.size()-1);

        Tenant client = new Tenant();
        tenant.setName("new client");
        Status status = restClient.post("/tenant/client/"+tenant.getUuid(), client, Status.class);
        assertNotNull(status.getEntityUid());

        client.setUuid(status.getEntityUid());
        tenant.setName("updated client");
        status = restClient.put("/tenant/client/"+tenant.getUuid(), tenant, Status.class);
        assertNotNull(status.getEntityUid());

        Tenant dbclient = restClient.get("/tenant/client/"+tenant.getUuid(), Tenant.class);
        assertEquals(dbclient.getName(),tenant.getName());

        User user = new User();
        user.setEmail("testclient@test.com");
        user.setPassword("password");
        user.setFirstName("test");
        user.setLastName("client user");
        status = restClient.post("/tenant/client/"+client.getUuid()+"/user", user, Status.class);
        user.setUuid(status.getEntityUid());
        assertNotNull(status.getEntityUid());

        user.setFirstName("updated name");
        status = restClient.put("/tenant/client/"+client.getUuid()+"/user", user, Status.class);

    }


    @Test
    @DisplayName("brand add/update/get operations ")
    void brandOperations() throws Exception {

        TenantList clientList = restClient.get("/tenant/brand", TenantList.class);
        assertTrue(clientList.size()>0);

        Tenant client = clientList.get(clientList.size()-1);

        Tenant brand = new Tenant();
        brand.setName("new brand");
        Status status = restClient.post("/tenant/brand/"+client.getUuid(), brand, Status.class);
        assertNotNull(status.getEntityUid());

        brand.setUuid(status.getEntityUid());
        brand.setName("updated brand");
        status = restClient.put("/tenant/brand/"+client.getUuid(), brand, Status.class);
        assertNotNull(status.getEntityUid());

        Tenant dbbrand = restClient.get("/tenant/brand/"+client.getUuid(), Tenant.class);
        assertEquals(dbbrand.getName(),brand.getName());

        User user = new User();
        user.setEmail("testbrand@test.com");
        user.setPassword("password");
        user.setFirstName("test");
        user.setLastName("brand user");
        status = restClient.post("/tenant/brand/"+brand.getUuid()+"/user", user, Status.class);
        user.setUuid(status.getEntityUid());
        assertNotNull(status.getEntityUid());

        user.setFirstName("updated name");
        status = restClient.put("/tenant/brand/"+brand.getUuid()+"/user", user, Status.class);

    }

    @Test
    @DisplayName("campaign add/update/get operations ")
    void campaignOperations() throws Exception {

        TenantList brandList = restClient.get("/tenant/brand", TenantList.class);
        assertTrue(brandList.size()>0);

        Tenant brand = brandList.get(brandList.size()-1);

        Tenant campaign = new Tenant();
        brand.setName("new campaign");
        Status status = restClient.post("/tenant/campaign/"+brand.getUuid(), campaign, Status.class);
        assertNotNull(status.getEntityUid());

        campaign.setUuid(status.getEntityUid());
        campaign.setName("updated campaign");
        status = restClient.put("/tenant/brand/"+campaign.getUuid(), campaign, Status.class);
        assertNotNull(status.getEntityUid());

        Tenant dbcampign = restClient.get("/tenant/campaign/"+campaign.getUuid(), Tenant.class);
        assertEquals(dbcampign.getName(), campaign.getName());

        User user = new User();
        user.setEmail("testcampaign@test.com");
        user.setPassword("password");
        user.setFirstName("test");
        user.setLastName("campaign user");
        status = restClient.post("/tenant/campaign/"+campaign.getUuid()+"/user", user, Status.class);
        user.setUuid(status.getEntityUid());
        assertNotNull(status.getEntityUid());

        user.setFirstName("updated name");
        status = restClient.put("/tenant/campaign/"+campaign.getUuid()+"/user", user, Status.class);
        //assertNotNull(status.getEntityUid());

        status = restClient.delete("/user/"+user.getUuid()+"/close", Status.class);

    }

    @Test
    @DisplayName("add campaign")
    void addCampaign() throws Exception {
        Tenant campaign = new Tenant();
        String brandUId = "testbrand";
        campaign.setProgramName("new campaign");
        Status status = restClient.post("/tenant/campaign/"+brandUId, campaign, Status.class);
        assertNotNull(status.getEntityUid());

        User user = new User();
        user.setEmail("testcampaign@test.com");
        user.setPassword("password");
        user.setFirstName("test");
        user.setLastName("user");
        status = restClient.post("/tenant/campaign/"+brandUId+"/user", user, Status.class);
        assertNotNull(status.getEntityUid());
    }

    @Test
    @DisplayName("get system users")
    void getSystemUsers() throws Exception {
        UserList users = restClient.get("/user/system", UserList.class);
        assertTrue(users.size()>0);

    }


}

class TenantList extends ArrayList<Tenant> {

}

class UserList extends ArrayList<User> {

}